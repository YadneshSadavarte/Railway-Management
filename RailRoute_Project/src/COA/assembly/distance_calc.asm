; ============================================================
; RailRoute - COA CO2: 64-bit Assembly-Based Application Processing
; Task 2 - Operation 1: 64-bit Route-Distance Aggregation
; Task 2 - Operation 2: 64-bit Route Comparison (Shortest Route)
;
; Target: Windows x64 (NASM win64 object format, linked via MinGW-w64 gcc)
; Calling convention: Microsoft x64 ABI
;   Integer args: RCX, RDX, R8, R9 (left to right)
;   Every CALL site must reserve 32 bytes of "shadow space" on the stack
;   Callee-saved registers: RBX, RBP, RDI, RSI, RSP, R12-R15
;
; Data source: RailRoute C++ RouteManager (stations STN001-STN005)
; Distances originate in kilometers (see RouteManager.cpp) and are
; scaled to METERS (km * 1000) for a genuinely processed 64-bit domain,
; consistent across both operations.
;
; Build (Windows, VS Code terminal, NASM + MinGW-w64 already on PATH):
;   nasm -f win64 distance_calc.asm -o distance_calc.obj
;   gcc distance_calc.obj -o distance_calc.exe
;   distance_calc.exe
; ============================================================

default rel

section .data
    ; ---------- Operation 1 data: multi-segment route ----------
    ; Route: Nagpur(STN003) -> Pune(STN001) -> Nashik(STN004) -> Mumbai(STN002)
    seg1_nagpur_pune   dq 720000   ; 720 km  -> meters
    seg2_pune_nashik   dq 210000   ; 210 km  -> meters
    seg3_nashik_mumbai dq 165000   ; 165 km  -> meters

    msg_op1_title  db "-- Operation 1: Route-Distance Aggregation --", 10, 0
    msg_op1_result db "Nagpur->Pune->Nashik->Mumbai total distance = %lld meters", 10, 0

    ; ---------- Operation 2 data: two candidate routes ----------
    ; Route A: Pune(STN001) -> Mumbai(STN002) direct
    routeA_distance dq 192000   ; 192 km -> meters
    ; Route B: Pune(STN001) -> Nashik(STN004) -> Mumbai(STN002)
    routeB_distance dq 375000   ; 210+165 km -> meters

    msg_op2_title db 10, "-- Operation 2: Route Comparison --", 10, 0
    msg_a_shorter db "Result: Route A is SHORTER. Difference = %lld meters", 10, 0
    msg_b_shorter db "Result: Route B is SHORTER. Difference = %lld meters", 10, 0
    msg_equal     db "Result: Route A and Route B have EQUAL distance.", 10, 0

    extern printf

section .text
    global main

main:
    push rbp
    mov rbp, rsp
    sub rsp, 32                    ; 32-byte shadow space (required before ANY call)

    ; ============================================================
    ; OPERATION 1: 64-bit cumulative route-distance addition
    ;   RAX = running 64-bit accumulator
    ; ============================================================
    lea rcx, [msg_op1_title]
    call printf

    mov rax, [seg1_nagpur_pune]    ; RAX = 720000  (64-bit load)
    add rax, [seg2_pune_nashik]    ; RAX = 930000  (64-bit ADD)
    add rax, [seg3_nashik_mumbai]  ; RAX = 1095000 (64-bit ADD)

    lea rcx, [msg_op1_result]
    mov rdx, rax                   ; result -> 2nd printf arg
    call printf

    ; ============================================================
    ; OPERATION 2: 64-bit comparison of two candidate routes
    ;   RAX = Route A distance, RBX = Route B distance (callee-saved,
    ;   safely survives the printf calls above and below)
    ;   RDX = |difference|
    ; ============================================================
    lea rcx, [msg_op2_title]
    call printf

    mov rax, [routeA_distance]     ; RAX <- Route A (full 64-bit)
    mov rbx, [routeB_distance]     ; RBX <- Route B (full 64-bit)

    cmp rax, rbx                   ; 64-bit compare, sets flags (ZF, SF, OF)
    je  routes_equal
    jl  route_a_shorter
    jg  route_b_shorter

route_a_shorter:
    mov rdx, rbx
    sub rdx, rax                   ; RDX = B - A (positive difference)
    lea rcx, [msg_a_shorter]
    call printf
    jmp op2_done

route_b_shorter:
    mov rdx, rax
    sub rdx, rbx                   ; RDX = A - B (positive difference)
    lea rcx, [msg_b_shorter]
    call printf
    jmp op2_done

routes_equal:
    lea rcx, [msg_equal]
    call printf

op2_done:
    add rsp, 32
    mov eax, 0
    pop rbp
    ret
