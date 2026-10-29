default rel

extern printf
extern exit

section .data
    routeA_leg1_m dq 64000
    routeA_leg2_m dq 74000
    routeA_leg3_m dq 54000
    routeB_leg1_m dq 210000
    routeB_leg2_m dq 165000
    elevation_factor_A dq 16
    elevation_factor_B dq 15

    fmt_border db "========================================================================================", 10, 0
    fmt_header db "     COA CO2/CO3/CO4: 64-BIT X86-64 ROUTE COMPARISON & HARDWARE MULTIPLICATION (IMUL)   ", 10, 0
    fmt_sec1 db "1. 64-BIT REGISTER ACCUMULATION & QUADWORD STATE:", 10, 0
    fmt_rax db "   RAX (Route A Shortest Total) : %lld meters (0x%016llX)", 10, 0
    fmt_rbx db "   RBX (Route B Alternative)    : %lld meters (0x%016llX)", 10, 0
    fmt_rcx db "   RCX (Distance Differential)  : %lld meters (0x%016llX)", 10, 0
    fmt_sec2 db "2. ALU STATUS FLAGS & CONDITIONAL BRANCHING:", 10, 0
    fmt_cmp db "   Instruction: CMP RAX, RBX", 10, 0
    fmt_jl db "   Branch Taken: JL .route_a_shorter (ZF=0, SF=1, CF=1, OF=0)", 10, 0
    fmt_jg db "   Branch Taken: JG .route_b_shorter (ZF=0, SF=0, CF=0, OF=0)", 10, 0
    fmt_je db "   Branch Taken: JE .routes_equal (ZF=1, SF=0, CF=0, OF=0)", 10, 0
    fmt_sec3 db "3. HARDWARE MULTIPLICATION (IMUL) OPERATIONAL ENERGY SCORE:", 10, 0
    fmt_efa db "   Elevation Gradient Factor A  : %lld (Peak Altitude Gradient)", 10, 0
    fmt_efb db "   Elevation Gradient Factor B  : %lld (Peak Altitude Gradient)", 10, 0
    fmt_r8 db "   R8 (Energy Score Route A)    : %lld (0x%016llX)", 10, 0
    fmt_r9 db "   R9 (Energy Score Route B)    : %lld (0x%016llX)", 10, 0
    fmt_savings db "   Energy Score Efficiency      : Route A saves %lld energy units vs Route B", 10, 0

    fmt_sec4 db "4. ADD-AND-SHIFT & SUCCESSIVE ADDITION MULTIPLICATION (CO3):", 10, 0
    fmt_route_a_head db "   ROUTE A (Shortest Route - 192000 m * 16):", 10, 0
    fmt_route_b_head db "   ROUTE B (Alternative Route - 375000 m * 15):", 10, 0
    fmt_shift_res db "   - Add-and-Shift Result       : %lld (0x%016llX)", 10, 0
    fmt_succ_res db "   - Successive Addition Result : %lld (0x%016llX)", 10, 0
    fmt_imul_res db "   - Hardware IMUL Result       : %lld (0x%016llX)", 10, 0
    fmt_multi_match db "   - Verification Status        : %s (All 3 multiplication algorithms agree)", 10, 0
    fmt_multi_mismatch db "   - Verification Status        : %s (Mismatch detected)", 10, 0

    fmt_sec5 db "5. 64-BIT STRING PROCESSING & STRING-INSTRUCTION FAMILY (CO3):", 10, 0
    fmt_str_src db "   - Source String (LODSB)      : %s", 10, 0
    fmt_str_dst db "   - Converted String (STOSB)   : %s", 10, 0
    fmt_str_len db "   - String Length (SCASB)      : %lld characters", 10, 0
    fmt_str_csum db "   - Quadword Checksum (ADD)    : %lld (0x%016llX)", 10, 0
    fmt_str_cmp db "   - Block Copy & CMP (CMPSB)   : %s (rep movsb duplicate verified via repe cmpsb)", 10, 0

    ticket_tag db "pnr:tkt001|pune-ami", 0
    str_match db "MATCH", 0
    str_mismatch db "MISMATCH", 0

section .bss
    buffer_upper resb 64
    buffer_copy resb 64
    str_len resq 1
    str_csum resq 1

section .text
    global main

main:
    push rbx
    push rsi
    push rdi
    push r12
    push r13
    push r14
    push r15
    sub rsp, 96

    mov rax, [routeA_leg1_m]
    add rax, [routeA_leg2_m]
    add rax, [routeA_leg3_m]

    mov rbx, [routeB_leg1_m]
    add rbx, [routeB_leg2_m]

    mov rcx, rbx
    sub rcx, rax

    mov [rsp+32], rax
    mov [rsp+40], rbx
    mov [rsp+48], rcx

    lea rcx, [fmt_border]
    call printf

    lea rcx, [fmt_header]
    call printf

    lea rcx, [fmt_border]
    call printf

    lea rcx, [fmt_sec1]
    call printf

    lea rcx, [fmt_rax]
    mov rdx, [rsp+32]
    mov r8, [rsp+32]
    call printf

    lea rcx, [fmt_rbx]
    mov rdx, [rsp+40]
    mov r8, [rsp+40]
    call printf

    lea rcx, [fmt_rcx]
    mov rdx, [rsp+48]
    mov r8, [rsp+48]
    call printf

    lea rcx, [fmt_sec2]
    call printf

    lea rcx, [fmt_cmp]
    call printf

    mov rax, [rsp+32]
    mov rbx, [rsp+40]
    cmp rax, rbx
    jl .route_a_is_shorter
    jg .route_b_is_shorter
    lea rcx, [fmt_je]
    call printf
    jmp .energy_calc

.route_a_is_shorter:
    lea rcx, [fmt_jl]
    call printf
    jmp .energy_calc

.route_b_is_shorter:
    lea rcx, [fmt_jg]
    call printf

.energy_calc:
    lea rcx, [fmt_sec3]
    call printf

    lea rcx, [fmt_efa]
    mov rdx, [elevation_factor_A]
    call printf

    lea rcx, [fmt_efb]
    mov rdx, [elevation_factor_B]
    call printf

    mov rax, [rsp+32]
    imul rax, [elevation_factor_A]
    mov [rsp+32], rax

    mov rbx, [rsp+40]
    imul rbx, [elevation_factor_B]
    mov [rsp+40], rbx

    lea rcx, [fmt_r8]
    mov rdx, [rsp+32]
    mov r8, [rsp+32]
    call printf

    lea rcx, [fmt_r9]
    mov rdx, [rsp+40]
    mov r8, [rsp+40]
    call printf

    mov r10, [rsp+40]
    sub r10, [rsp+32]
    lea rcx, [fmt_savings]
    mov rdx, r10
    call printf

    lea rcx, [fmt_border]
    call printf

    lea rcx, [fmt_sec4]
    call printf

    lea rcx, [fmt_route_a_head]
    call printf

    mov r12, 192000
    mov r13, [elevation_factor_A]
    xor r14, r14

.shift_loop_a:
    test r13, 1
    jz .skip_add_a
    add r14, r12
.skip_add_a:
    shl r12, 1
    shr r13, 1
    jnz .shift_loop_a

    mov [rsp+56], r14

    mov rcx, [elevation_factor_A]
    xor r15, r15
.succ_loop_a:
    add r15, 192000
    dec rcx
    jnz .succ_loop_a

    mov [rsp+64], r15

    mov r12, [rsp+32]
    mov [rsp+72], r12

    lea rcx, [fmt_shift_res]
    mov rdx, [rsp+56]
    mov r8, [rsp+56]
    call printf

    lea rcx, [fmt_succ_res]
    mov rdx, [rsp+64]
    mov r8, [rsp+64]
    call printf

    lea rcx, [fmt_imul_res]
    mov rdx, [rsp+72]
    mov r8, [rsp+72]
    call printf

    mov rax, [rsp+56]
    cmp rax, [rsp+64]
    jne .mismatch_a
    cmp rax, [rsp+72]
    jne .mismatch_a
    lea rcx, [fmt_multi_match]
    lea rdx, [str_match]
    call printf
    jmp .done_verify_a

.mismatch_a:
    lea rcx, [fmt_multi_mismatch]
    lea rdx, [str_mismatch]
    call printf

.done_verify_a:
    lea rcx, [fmt_route_b_head]
    call printf

    mov r12, 375000
    mov r13, [elevation_factor_B]
    xor r14, r14

.shift_loop_b:
    test r13, 1
    jz .skip_add_b
    add r14, r12
.skip_add_b:
    shl r12, 1
    shr r13, 1
    jnz .shift_loop_b

    mov [rsp+56], r14

    mov rcx, [elevation_factor_B]
    xor r15, r15
.succ_loop_b:
    add r15, 375000
    dec rcx
    jnz .succ_loop_b

    mov [rsp+64], r15

    mov r12, [rsp+40]
    mov [rsp+72], r12

    lea rcx, [fmt_shift_res]
    mov rdx, [rsp+56]
    mov r8, [rsp+56]
    call printf

    lea rcx, [fmt_succ_res]
    mov rdx, [rsp+64]
    mov r8, [rsp+64]
    call printf

    lea rcx, [fmt_imul_res]
    mov rdx, [rsp+72]
    mov r8, [rsp+72]
    call printf

    mov rax, [rsp+56]
    cmp rax, [rsp+64]
    jne .mismatch_b
    cmp rax, [rsp+72]
    jne .mismatch_b
    lea rcx, [fmt_multi_match]
    lea rdx, [str_match]
    call printf
    jmp .done_verify_b

.mismatch_b:
    lea rcx, [fmt_multi_mismatch]
    lea rdx, [str_mismatch]
    call printf

.done_verify_b:
    lea rcx, [fmt_sec5]
    call printf

    cld
    lea rdi, [ticket_tag]
    xor al, al
    mov rcx, -1
    repne scasb
    not rcx
    dec rcx
    mov [str_len], rcx

    mov r12, rcx
    lea rsi, [ticket_tag]
    lea rdi, [buffer_upper]
.upper_loop:
    lodsb
    cmp al, 'a'
    jb .not_lower
    cmp al, 'z'
    ja .not_lower
    sub al, 32
.not_lower:
    stosb
    dec r12
    jnz .upper_loop
    xor al, al
    stosb

    lea rsi, [buffer_upper]
    mov rcx, [str_len]
    xor r13, r13
.csum_loop:
    lodsb
    movzx rax, al
    add r13, rax
    dec rcx
    jnz .csum_loop
    mov [str_csum], r13

    cld
    lea rsi, [buffer_upper]
    lea rdi, [buffer_copy]
    mov rcx, [str_len]
    inc rcx
    rep movsb

    lea rsi, [buffer_upper]
    lea rdi, [buffer_copy]
    mov rcx, [str_len]
    inc rcx
    repe cmpsb
    jne .str_mismatch_label
    lea r14, [str_match]
    jmp .str_cmp_done

.str_mismatch_label:
    lea r14, [str_mismatch]

.str_cmp_done:
    lea rcx, [fmt_str_src]
    lea rdx, [ticket_tag]
    call printf

    lea rcx, [fmt_str_dst]
    lea rdx, [buffer_upper]
    call printf

    lea rcx, [fmt_str_len]
    mov rdx, [str_len]
    call printf

    lea rcx, [fmt_str_csum]
    mov rdx, [str_csum]
    mov r8, [str_csum]
    call printf

    lea rcx, [fmt_str_cmp]
    mov rdx, r14
    call printf

    lea rcx, [fmt_border]
    call printf

    add rsp, 96
    pop r15
    pop r14
    pop r13
    pop r12
    pop rdi
    pop rsi
    pop rbx

    xor eax, eax
    ret
