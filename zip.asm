.data
prompt: .asciiz "\nGive me your zip code (0 to stop): " #prompt for string
prompt2: .asciiz "The sum of all digits in your zip code is"
rec: .asciiz "\nRECURSIVE: "
iter: .asciiz "\nITERATIVE: "

.text

main:
	#prompt user for input
	li $v0, 4
	la $a0, prompt 
	syscall

	#read in user input
	li $v0, 5
	syscall 

	#store user input in two separate variables, one for each function
	move $t1, $v0
	move $t2, $t1

	#exit program if input is zero
	beqz $t1, end

	#initialize sum variable as zero
	li $s0,0
	
	#get ready to print results
	li $v0, 4
	la $a0, prompt2
	syscall
		
	#display program output
	printSum:
		li $v0, 4
		la $a0, prompt2
		syscall

		j rec_digits_sum
		

	j main

#use a combination of multiplying and dividing by 10 to access each digit in zip code, recursively loop
rec_digits_sum:
	blez $t1, printRec
	div $s1, $t1, 10
	mul $s1, $s1, 10
	sub $s3, $t1, $s1
	add $s0, $s0, $s3
	div $t1, $t1, 10

	j rec_digits_sum

#do 5 iterations to access each of the 5 digits
int_digits_sum:
	#1st iteration
	div $s1, $t2, 10
	mul $s1, $s1, 10
	sub $s3, $t2, $s1
	add $s0 $s0, $s3
	div $t2, $t2, 10
		
	#2nd iteration
	div $s1, $t2, 10
	mul $s1, $s1, 10
	sub $s3, $t2, $s1
	add $s0 $s0, $s3
	div $t2, $t2, 10
		
	#3rd iteration
	div $s1, $t2, 10
	mul $s1, $s1, 10
	sub $s3, $t2, $s1
	add $s0 $s0, $s3
	div $t2, $t2, 10
		
	#4th iteration
	div $s1, $t2, 10
	mul $s1, $s1, 10
	sub $s3, $t2, $s1
	add $s0 $s0, $s3
	div $t2, $t2, 10
		
	#5th iteration
	div $s1, $t2, 10
	mul $s1, $s1, 10
	sub $s3, $t2, $s1
	add $s0 $s0, $s3
	div $t2, $t2, 10
		
	j printIter

#print recursive value	
printRec:
	li $v0, 4
	la $a0, rec
	syscall

	li $v0, 1
	move $a0, $s0 
	syscall
	
	li $s0,0
	j int_digits_sum

#print iterative value		
printIter:
	li $v0, 4
	la $a0, iter
	syscall

	li $v0, 1
	move $a0, $s0 
	syscall
	
	j main
	
#exit program
end:
	li $v0, 10
