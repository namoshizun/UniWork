Machine Learning Algorithms
============================

Authors:
 * Di Lu
 * Chenrui Liu
 
**Introduction**
------------
Decision Tree (ID3) and Naive Bayesian Classifier implemented in Java. 

IMPORTANT TODO:
- program is working properly already but HARD TO READ, will do further refactorisation and documentation; 
- Currently only binary yes/no classes can be processed, will generalise the algorithm to accpet arbitrary class types; 

**How to use**
-----------
Training data are contained in csv files.<br />
pima-indians-diabetes.csv is the training data for DT<br />
pima.csv is used to train NB;<br />
<br />
Examples:
> java Main pima-indians-diabetes.csv _TEST_.csv DT <br />
> java Main pima.csv _TEST_.csv NB

Then the program should ouput a list of classes, each corresponds to an example in test data file. 



