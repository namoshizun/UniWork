Inference on Baysian network
============================

Authors:
 * Di Lu
 * Chenrui Liu
 
Introduction
------------

This is a Java implementation of the general Baysian Network and its inference methods Variable elimination and Markov Chain Monte Carlo

The implementation has a demon Bayesian Network on whicn user can perform queries or benchmark two inference methods. The default Bayesian network is deciped as following but it can also be extended to a more complex network:
  
  P (m) = 0.20

  P(i|m) = 0.80 
  P(i|¬m) = 0.20
 
  P(b|m) = 0.20
  P(b|¬m) = 0.05
 
  P(c|i,b) = 0.80 
  P(c|¬i,b) = 0.80 
  P(c|i,¬b) = 0.80
  P(c|¬i,¬b) = 0.05

  P(s|b) = 0.80 
  P(s|¬b) = 0.60


How to use
----------

1.Compile the program:
> javac Main.java


2.To perform queries:
Give queries from standard input like:
> java Main
> VE  <-- this specifies which inference method to use
> 2   <-- specifies number of queries
> P(c|m,b)
> P(-c|m,-b)  <- '-' means 'not'

Then you should get ouputs like
> 0.800000
> 0.650000


3.To benchmark Variable Elimination and Markov Chain Monte Charlo:
Command line arguments should be given to enter the test mode. Following the format, _QUERY_ _METHOD_ #ITERATION #SAMPLES(if you are testing MCMC), for example:
> java Main P(c|m,b) MCMC 100 1000

Then you should get outputs like:
> MCMC computing P(c|m,b) with 100 iterations using sample size 1000.
> _SOME MILLESECONDS_


4.To extend the Bayesian Network:
Unfortunately you have to change the source code to do that :(, but i will add creating interface to my todo list.. 
Here is an example on how would you change the Main.java:

To add another node 'G' whose parent node is S and has a probability distribution like
 P(g|s) = 0.3
 P(g|-s) = 0.2

Open Main.java and add the folloing in getNetwork() method:

		net.addNode("G", new String [] {"T", "F"}, new String[] {"S"}, new String [] {
				"G = T, S = T: 0.3",
				"G = T, S = F: 0.2",
				"G = F, S = T: 0.7",
				"G = F, S = F: 0.8",
				});