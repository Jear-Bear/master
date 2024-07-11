library(kknn)

#make a random number between 0 and 1 for each row in iris
set.seed(3425)
gp = runif(nrow(iris))

#order rows base on random number (randomize date)
iris = iris[order(gp),]

#check how balanced min/max are between each attribute
#kknn uses a distance function, so we want the scales to be normalized (equal)
summary(iris[, c(1, 2, 3, 4)])

#normalize formula: (nth value - minimum) / (max - min)
normalize = function(x){
  + return((x - min(x)) / (max(x) - min(x)))
}

#example of normalizing a vector with values 1:5
normalize(c(1, 2, 3, 4, 5))

#normalized iris by the 4 numerical attributes (notice the min and max for all features are 0:1)
iris_n = as.data.frame(lapply(iris[,c(1, 2, 3, 4)], normalize))
summary(iris_n)

#hold at least %10 of data for testing

#create training dataframe (majority of data)
iris_train = iris_n[1:129, ]

#create test dataframe (minority of data)
iris_test = iris_n[130:150, ]

#isolate the species column (target that the ML will be predicting)
iris_train_target = iris[1:129, 5]
iris_test_target = iris[130:150, 5]

require(class)

#k: how many nearest neighbors should the algorithm use? 
#(sqrt of total # of observations -> sqrt(150))
#nice to use odd number, as knn uses majority vote
m1 = knn(train = iris_train, test = iris_test, cl=iris_train_target, k = 13)
m1

#display results
table(iris_test_target, m1)