.data 
buffer: .space 20
length: .word 8
sorry: .asciiz "I'm sorry, I couldn't finish this assignment in time :( \n"
prompt1: .asciiz "Please enter machine instructions (in hexadecimal without 0x):\n"
short: .asciiz "The input is too short\n"
long: .asciiz "The input is too long\n"
invalid: .asciiz "The input has invalid characters\n"
bin: .asciiz "opcode: "
apology: .asciiz "\nI couldn't finish past this, I apologize"
emptied: .asciiz "\nEmpty string... exiting program"
.text
	
main:
    	#ask user for input	
	la $a0, prompt1
	li $v0, 4
	syscall

	#read hex from user
	li $v0, 8 		#take input
        la $a0, buffer 		#load byte space
        li $a1, 20 		#allocate byte space for string
        move $s0, $a0
	syscall

loop:				#get length of hex
        lb $t1, ($a0) 		#load the contents of the address
        beq $t1, $zero, done    #go to done if equal

        addi $t0, $t0, 1 	#add to the counter
        addi $a0, $a0, 1 	#go to the next byte

        j loop 
    
done:
	sub $t0, $t0, 1		#subtract 1 from length to get actual length
	beq $t0, $zero, empty 	#exit the program if the string was empty
    	blt $t0, 8, tooshort	#branch to tooshort if the string is less than 8
    	bgt $t0, 8, toolong	#branch to toolong if the string is greater than 8
    	beq $t0, 8, equal	#branch to tooshort if the string is equal to 8
    
tooshort:
	#tell the user the string is too short
    	move $t0, $0
    	li $v0, 4
    	la $a0, short
    	syscall
    
    	#prompt user for input again
    	b main
    
toolong:
	#tell the user the string is too long
    	move $t0, $0
    	li $v0, 4
    	la $a0, long
    	syscall
    
    	#prompt user for input again
    	b main
    
equal:
	move $t5, $zero
	li $v0, 4
	la $a0, bin
    	syscall
	move $t4, $zero
	li $v0, 1
    	la $a0, ($s0)
	lb $t0 0($a0)
	li $t1, 7 #no. of bits to shift
	b convloop
	
convloop:
	beq $t1, 3, EndLoop
	srlv $t2, $t0, $t1 #shift the bit to right most position
	and $t2, 1 #extract the bit by ANDING 1 with it
	#display the extracted bit
	li $v0, 1
	move $a0, $t2
	addu $t5, $t5, $t2
	syscall
	#decrement the bit no.
	sub $t1, $t1, 1
	b convloop
	
	EndLoop:
	add $t4, $t4, 4
	b equal2

equal2:
	beq $t4, 8, end
	li $v0, 1
    	la $a0, ($s0)
	lb $t0 1($a0)
	li $t1, 5 #no. of bits to shift
	b convloop

end:
	li $v0, 4
	la $a0, apology
    	syscall
	b exit

empty:
	move $t0, $0
    	li $v0, 4
    	la $a0, emptied
    	syscall
    	
    	b exit
    	
exit:
	#exit the program when called
    	li $v0, 10
	syscall
    
syscall
