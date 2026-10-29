section .data
    leg1_pune_lonavala dw 64
    leg2_lonavala_kalyan dw 74
    leg3_kalyan_mumbai dw 54
    total_distance_km dw 0
    alt_route_distance dw 375

section .text
    global _start

_start:
    mov ax, [leg1_pune_lonavala]
    mov bx, [leg2_lonavala_kalyan]
    add ax, bx
    mov cx, [leg3_kalyan_mumbai]
    add ax, cx
    mov [total_distance_km], ax
    mov dx, [alt_route_distance]
    cmp ax, dx
    mov eax, 1
    xor ebx, ebx
    int 0x80
