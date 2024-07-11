#install.packages('ggplot2')  # use once
#install.packages('sampling')  # use once
#install.packages('scatterplot3d')  # use once
#install.packages('arules')  # use once
#install.packages('Matrix')
library(ggplot2) 
library(sampling) 
library(scatterplot3d) 
library(Matrix) 
library(arules)

#read data and store in dataset
dataset = read.table("hepatitis_csv.csv", sep = ",", header = TRUE)
dataset

#boxplot the alk_phosphate vs sex
boxplot(alk_phosphate ~ sex, data = dataset, horizontal = TRUE)

#get mean of bilirubin, and albumin grouped by sex
aggregate(cbind(bilirubin, albumin) ~ sex, data = dataset, FUN = mean, na.rm = TRUE)

euclidean <- function(a, b) sqrt(sum((a - b)^2))

nsBil = data.frame(dataset$bilirubin)
nsBil = na.omit(nsBil)

sBil = scale(data.frame(dataset$bilirubin))
sBil = na.omit(sBil)

nsAlb = data.frame(dataset$albumin)
nsAlb = na.omit(nsAlb)

sAlb = scale(data.frame(dataset$albumin))
sAlb = na.omit(sAlb)

euclidean(nsBil, sBil)

euclidean(nsAlb, sAlb)

