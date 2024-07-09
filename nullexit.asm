.data
	#store string
	str:	.asciiz "Comets are awesome!"
	
	#null terminate
	prefix: .asciiz "\n0x"
	o: .asciiz "6F"
	m: .ascii "6D"
	
.text

#load string
la $t0, str

#move
move $a0, $t0

#load
li $v0, 4

#terminate at null character
la $a0, prefix
li $v0, 4
syscall

#proceed to load the next character
lb $t1, 0($t0)

#loop through the string
loop:

beq $t1, 0,end
beq $t1, 67,printC
beq $t1, 111,printo
beq $t1, 109,printm
beq $t1, 101,printe
beq $t1, 116,printt
beq $t1, 115,prints
beq $t1, 32,printSpace
beq $t1, 97,printa
beq $t1, 114,printr
beq $t1, 119,printw
beq $t1, 33,printEx

#store and increment
add $t0, $t0, 1
lb $t1, 0, ($t0)
j end

#display hex values for each letter
printC:
li $a0, 43
li $v0, 1
syscall
add $t0, $t0, 1
lb $t1, 0, ($t0)
j loop

printo:
li $a0, 75
li $v0, 1
syscall
add $t0, $t0, 1
lb $t1, 0, ($t0)
j loop

printm:
li $a0, 73
li $v0, 1
syscall
add $t0, $t0, 1
lb $t1, 0, ($t0)
j loop

printe:
li $a0, 65
li $v0, 1
syscall
add $t0, $t0, 1
lb $t1, 0, ($t0)
j loop

printt:
li $a0, 74
li $v0, 1
syscall
add $t0, $t0, 1
lb $t1, 0, ($t0)
j loop

prints:
li $a0, 73
li $v0, 1
syscall
add $t0, $t0, 1
lb $t1, 0, ($t0)
j loop

printSpace:
li $a0, 20
li $v0, 1
syscall
add $t0, $t0, 1
lb $t1, 0, ($t0)
j loop

printa:
li $a0, 61
li $v0, 1
syscall
add $t0, $t0, 1
lb $t1, 0, ($t0)
j loop

printr:
li $a0, 72
li $v0, 1
syscall
add $t0, $t0, 1
lb $t1, 0, ($t0)
j loop

printw:
li $a0, 77
li $v0, 1
syscall
add $t0, $t0, 1
lb $t1, 0, ($t0)
j loop

printEx:

li $a0, 21
li $v0, 1

syscall

add $t0, $t0, 1
lb $t1, 0 ($t0)

#end loop
j loop

#display null-terminated value
end:
li $v0, 10

syscall