.386
.model small
.stack 100h

.data
    ; User prompts and display messages (standard DOS format terminating with '$')
    msg_title db 0Dh, 0Ah, "==========================================================", 0Dh, 0Ah
              db "     RAILROUTE LOW-LEVEL COAL MODULE - 64-BIT ADDER       ", 0Dh, 0Ah
              db "==========================================================$"
    msg1      db 0Dh, 0Ah, "Enter First Track Segment Distance (16 hex chars): $"
    msg2      db 0Dh, 0Ah, "Enter Second Track Segment Distance (16 hex chars): $"
    msg_sum   db 0Dh, 0Ah, "Total Calculated Route Distance (Sum in Hex):     $"
    newline   db 0Dh, 0Ah, "$"
    
    ; Memory buffers to store 64-bit values (split into two 32-bit double words)
    num1_high dd 0          ; Upper 32 bits of Segment A
    num1_low  dd 0          ; Lower 32 bits of Segment A
    num2_high dd 0          ; Upper 32 bits of Segment B
    num2_low  dd 0          ; Lower 32 bits of Segment B
    sum_high  dd 0          ; Upper 32 bits of Total Distance
    sum_low   dd 0          ; Lower 32 bits of Total Distance

.code
main proc
    ; Initialize the Data Segment register
    mov ax, @data
    mov ds, ax
    
    ; Display program title
    mov dx, offset msg_title
    mov ah, 09h
    int 21h
    
    ; Prompt for and read first 64-bit hex distance
    mov dx, offset msg1
    mov ah, 09h
    int 21h
    call read_hex_64
    mov num1_high, edx      ; EDX holds upper 32 bits of input
    mov num1_low, eax       ; EAX holds lower 32 bits of input
    
    ; Prompt for and read second 64-bit hex distance
    mov dx, offset msg2
    mov ah, 09h
    int 21h
    call read_hex_64
    mov num2_high, edx      ; EDX holds upper 32 bits of input
    mov num2_low, eax       ; EAX holds lower 32 bits of input
    
    ; =========================================================================
    ; 64-BIT ADDITION CORE PROCESS (ALU OPERATIONS)
    ; =========================================================================
    
    ; Step 1: Add the lower 32 bits
    mov eax, num1_low       ; Load lower 32 bits of first number into EAX
    add eax, num2_low       ; Add lower 32 bits of second number (updates Carry Flag CF)
    mov sum_low, eax        ; Store the lower 32-bit sum in memory
    
    ; Step 2: Add the upper 32 bits along with the carry from Step 1
    mov ebx, num1_high      ; Load upper 32 bits of first number into EBX
    adc ebx, num2_high      ; Add upper 32 bits of second number + CF (Carry Flag)
    mov sum_high, ebx       ; Store the upper 32-bit sum in memory
    
    ; =========================================================================
    ; DISPLAY RESULTS
    ; =========================================================================
    
    ; Display output label
    mov dx, offset msg_sum
    mov ah, 09h
    int 21h
    
    ; Load the 64-bit result into EDX:EAX for printing
    mov edx, sum_high       ; EDX holds upper 32 bits of sum
    mov eax, sum_low        ; EAX holds lower 32 bits of sum
    call print_hex_64       ; Call the printing subroutine
    
    ; Print final formatting newline
    mov dx, offset newline
    mov ah, 09h
    int 21h
    
    ; Terminate the program and return control to DOS
    mov ax, 4C00h
    int 21h
main endp

; =============================================================================
; SUBROUTINE: read_hex_64
; Reads up to 16 hexadecimal digits from keyboard, translating ASCII to binary.
; Outputs: EDX = Upper 32 bits (High), EAX = Lower 32 bits (Low)
; =============================================================================
read_hex_64 proc
    push ecx
    push ebx
    
    xor edx, edx            ; Clear EDX (to accumulate high 32 bits)
    xor eax, eax            ; Clear EAX (to accumulate low 32 bits)
    mov ecx, 16             ; Read exactly 16 hex digits (16 * 4 bits = 64 bits)
    
read_loop:
    ; Read a single character with echo (INT 21H, AH=01H)
    mov ah, 01h
    int 21h
    
    ; Check and convert character to corresponding 4-bit binary value
    cmp al, '0'
    jb invalid_char
    cmp al, '9'
    jbe process_digit
    
    cmp al, 'A'
    jb invalid_char
    cmp al, 'F'
    jbe process_upper_letter
    
    cmp al, 'a'
    jb invalid_char
    cmp al, 'f'
    jbe process_lower_letter
    
invalid_char:
    xor ebx, ebx            ; If character is invalid, substitute 0
    jmp shift_accumulators

process_digit:
    sub al, '0'             ; Convert '0'-'9' to 0-9
    movzx ebx, al
    jmp shift_accumulators

process_upper_letter:
    sub al, 'A'             ; Convert 'A'-'F' to 10-15
    add al, 10
    movzx ebx, al
    jmp shift_accumulators

process_lower_letter:
    sub al, 'a'             ; Convert 'a'-'f' to 10-15
    add al, 10
    movzx ebx, al

shift_accumulators:
    ; Shift current 64-bit value in EDX:EAX left by 4 bits
    shld edx, eax, 4        ; Shift EDX left, moving top 4 bits of EAX into bottom of EDX
    shl eax, 4              ; Shift EAX left by 4 bits to make room for new digit
    
    or eax, ebx             ; Combine the new 4-bit digit into EAX
    
    dec ecx                 ; Decrement character counter
    jnz read_loop           ; Continue until all 16 digits are read
    
    pop ebx
    pop ecx
    ret
read_hex_64 endp

; =============================================================================
; SUBROUTINE: print_hex_64
; Prints the 64-bit value stored in EDX:EAX as 16 hexadecimal characters.
; Inputs: EDX = Upper 32 bits, EAX = Lower 32 bits
; =============================================================================
print_hex_64 proc
    push ebx
    push ecx
    push edx
    push eax
    
    ; Print upper 32 bits first (EDX)
    mov ebx, edx
    call print_hex_32
    
    ; Restore EAX from stack and print lower 32 bits (EAX)
    pop eax
    push eax                ; Maintain stack balance
    mov ebx, eax
    call print_hex_32
    
    pop eax
    pop edx
    pop ecx
    pop ebx
    ret
print_hex_64 endp

; =============================================================================
; HELPER SUBROUTINE: print_hex_32
; Prints the 32-bit value in EBX as 8 hexadecimal characters.
; Input: EBX = Value to display
; =============================================================================
print_hex_32 proc
    push ecx
    push edx
    push ebx
    
    mov ecx, 8              ; Set loop counter for 8 hex digits
    
print_loop:
    rol ebx, 4              ; Rotate EBX left by 4 bits (places highest nibble in lowest bits of BL)
    mov dl, bl
    and dl, 0Fh             ; Mask to isolate the lower 4 bits (nibble)
    
    ; Convert binary nibble to ASCII equivalent
    cmp dl, 9
    jbe convert_ascii_digit
    add dl, 7               ; Offset adjustment for 'A'-'F'
    
convert_ascii_digit:
    add dl, '0'             ; Convert to ASCII code
    
    ; Display character on console (INT 21H, AH=02H)
    mov ah, 02h
    int 21h
    
    dec ecx                 ; Decrement digit counter
    jnz print_loop          ; Continue loop until 8 characters are printed
    
    pop ebx
    pop edx
    pop ecx
    ret
print_hex_32 endp

end main
