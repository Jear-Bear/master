.data #contains strings to format the result output
prompt1: .asciiz "Please enter machine instructions (in hexadecimal):\n"
res: .asciiz "The difference of "
and: .asciiz " and "
parth1: .asciiz " ("
parth2: .asciiz ") is "
minus: .asciiz " - "

.text

main:
	#ask user for first number
	li $v0, 4	
	la $a0, prompt1
	syscall

	#read first integer from user
	li $v0, 5
	syscall 
	move $t0, $v0
	li $v0, 4
	syscall

	
	#read second integer from user
	move $t1, $v0
	 
	#subtract the two integers
	sub $t2, $t0, $t1

	#print the result
	li  $v0, 4
	la  $a0, res
	syscall
	
	li, $v0, 1
	move $a0, $t0
	syscall
	
	li, $v0, 4
	la $a0, and
	syscall
	
	li, $v0, 1
	move $a0, $t1
	syscall
	
	li, $v0, 4
	la $a0, parth1
	syscall
	
	li, $v0, 1
	move $a0, $t0
	syscall
	
	li, $v0, 4
	la $a0, minus
	syscall

	li, $v0, 1
	move $a0, $t1
	syscall

	li, $v0, 4
	la $a0, parth2
	syscall

	li, $v0, 1
	move $a0, $t2
	syscall

exit:
	#terminate and exit program
	li $v0, 10
	syscall
