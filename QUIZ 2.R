#QUIZ 2
library(arules)
library(ggplot2)

#Create a data frame that has 7 attributes (StudentID, student name, grade1, grade2, grade3, sex, and study.hours.per.week)
studentID = c(3245345, 3684523, 9787605, 6459876, 5679874, 2469875)
studentName = c("Jared Perlmutter", "Hannah Valena", "Bruce Wayne", "Dude Perfect", "Babe Ruth", "Sarah Johnson")
grade1 = c(98.3, 75.3, 87.3, 76.2, 87.4, 98.1)
grade2 = c(80.4, 67.3, 87.4, 65.3, 87.3, 64.2)
grade3 = c(76.3, 87.5, 76.4, 87.4, 85.2, 54.2)
sex = c("Male", "Female", "Male", "Male", "Male", "Female")
studyHoursPerWeek = c(6, 2, 5, 6, 3, 7)
studentFrame = data.frame(studentID, studentName, grade1, grade2, grade3, sex, studyHoursPerWeek)

#Add GPA to the data frame and calculate GPA for the students based on grade1, grade2, and grade3 (gpa is the mean of grade_1, grade_2, and grade_3 for each student)
studentFrame$gpa = rowMeans(studentFrame[, c("grade1", "grade2", "grade3")])

#Create a discretized data frame using equal intervals by discretizing grade1, grade2, and grade3 to 3 categories (bad, good, very good) and draw 3 plots to show the distribution of the grades
discreteStudentFrame = data.frame(
  grade1 = discretize(studentFrame$grade1, "interval", breaks = 3, labels = c("bad", "good", "very good")),
  grade2 = discretize(studentFrame$grade2, "interval", breaks = 3, labels = c("bad", "good", "very good")),
  grade3 = discretize(studentFrame$grade3, "interval", breaks = 3, labels = c("bad", "good", "very good"))
)

#Show the distributions for grade1, grade2, and grade3 with ggplot()
ggplot(discreteStudentFrame, aes(x = grade1)) + geom_bar()

#Draw a plot to show the distribution of a continuous attribute in the original dataset
ggplot(studentFrame, aes(x = gpa)) + geom_histogram()

