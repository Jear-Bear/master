#QUIZ 1
id = c(3812636, 2273526, 2360834, 2460684, 2595745, 3525678, 2347584, 9758504, 3466975, 2047457)
fname = c("Jared", "Hannah", "Ben", "Daddy", "Mommy", "Brother", "Sister", "Sun", "Moon", "Lake")
lname = c("Perlmutter", "Valena", "Brutacao", "Doe", "Moe", "Boe", "Soe", "Tzu", "Dzu", "River")
zipcode = c(12636, 23526, 23634, 75093, 25745, 75093, 23584, 97504, 75093, 20457)
gpa = c(3.4, 2.8, 1.0, 4.0, 3.8, 3.9, 3.2, 3.7, 2.3, 2.9)

studentFrame.data = data.frame(id, fname, lname, zipcode, gpa)

numGPA = sum(studentFrame.data$gpa >= 3.2)
print(paste("There are", numGPA, "students with a GPA higher than 3.2"))

numZIP = sum(studentFrame.data$zip == 75093)
print(paste("There are", numZIP, "students who live in the area code 75093"))

#Show students that have GPA less than 3.2
#gpaFilter = studentFrame.data$gpa <= 3.2
#studentFrame.data[gpaFilter,]