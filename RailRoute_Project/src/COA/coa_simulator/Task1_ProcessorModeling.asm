; ============================================================
; RailRoute - COA CO1: Processor-Level Modeling of Application
; Operation: 2-segment route distance addition
; Route: Pune (STN001) -> Nashik (STN004) -> Mumbai (STN002)
; Data source: RouteManager (RailRoute C++ module) distances in km
;
; NOTE: Exact syntax for your specific CPU Simulator ("YASMIN") was
; not verifiable from available documentation. This uses standard
; 8086-style ALP syntax (.MODEL/.DATA/.CODE, MOV/ADD, DW) accepted
; by most common teaching CPU simulators (e.g. EMU8086). Confirm
; against your actual simulator before submission and report back
; if the syntax needs adjustment.
; ============================================================

.MODEL SMALL
.STACK 100H

.DATA
    SEG1_PUNE_NASHIK   DW 210      ; Pune -> Nashik distance (km)
    SEG2_NASHIK_MUMBAI DW 165      ; Nashik -> Mumbai distance (km)
    TOTAL_DISTANCE     DW ?        ; Result: combined route distance (km)

.CODE
MAIN PROC
    MOV AX, @DATA
    MOV DS, AX              ; initialize data segment

    ; ---- Control Unit fetches operand 1 from memory into register ----
    MOV AX, SEG1_PUNE_NASHIK   ; AX <- 210  (memory -> register)

    ; ---- ALU performs the addition ----
    ADD AX, SEG2_NASHIK_MUMBAI ; AX <- AX + 165  (ALU: AX = 375)

    ; ---- Control Unit stores ALU result back to memory ----
    MOV TOTAL_DISTANCE, AX     ; register -> memory

    MOV AH, 4CH
    INT 21H                    ; terminate program
MAIN ENDP
END MAIN
