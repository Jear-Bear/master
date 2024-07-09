.data
pi: .float 3.14159 #pi
radius: .float 12.0 #12 inch radius
square: .float 5.33333333333 #Square feet of a square pizza
sqft: .float 144.0 #1 sqft = 144 sq inches
prompt1: .asciiz "How many round pizzas? "
prompt2: .asciiz "How many square pizzas? "
prompt3: .asciiz "Estimate? "
roundmsg: .asciiz "\nSquare feet of round pizzas: "
squaremsg: .asciiz "\nSquare feet of square pizzas: "
totalmsg: .asciiz "\nTotal square feet: "
r: .word 0 #The number of round pizzas
s: .word 0 #The number of square pizzas
yes: .asciiz "\nYeah!"
no: .asciiz "\nBummer  "
guess: .float 0.0 #guess for square feet sold
.text

lwc1 $f11, guess

#prompt the user for round input
li $v0, 4
la $a0, prompt1
syscall

#store input
li $v0 , 5
syscall
sw $v0, r

#prompt the user for square input
li $v0, 4
la $a0, prompt2
syscall

#store input
li $v0 , 5
syscall
sw $v0, s

#prompt the user for guess
li $v0, 4
la $a0, prompt3
syscall

#store input
li $v0 , 6
syscall
add.s $f11, $f0, $f11

#calcualte the area of the square pizzas
l.s $f7, square
lwc1 $f8, s
cvt.s.w $f8, $f8
mul.s $f5, $f7, $f8

#calcualte the area of round pizzas
l.s $f1, pi
l.s $f2, radius
mul.s $f3, $f1, $f2
mul.s $f3, $f3, $f2
lwc1 $f4, r
cvt.s.w $f4, $f4
mul.s $f3, $f3, $f4
l.s $f4, sqft
div.s $f3, $f3, $f4

#display the round area
li $v0, 4
la $a0, roundmsg
syscall
li $v0, 2
mov.s $f12, $f3
syscall

#display the square area
li $v0, 4
la $a0, squaremsg
syscall
li $v0, 2
mov.s $f12, $f5
syscall

#display the total area
li $v0, 4
la $a0, totalmsg
syscall

#compare guess to actual total square feet
li $v0, 2
add.s $f8, $f5, $f3
mov.s $f12, $f8
syscall
c.lt.s $f11, $f12

#branch
bc1t yeah
b bummer

exit:
	#exit the program
	li $v0, 10
	syscall
	
bummer:
	#bummer
	li $v0, 4
	la $a0, no
	syscall
	b exit
	
yeah:
	#yeah!
	li $v0, 4
	la $a0, yes
	syscall
	b exit
	
