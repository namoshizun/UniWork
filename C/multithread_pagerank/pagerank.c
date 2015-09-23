#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <stdbool.h>
#include <pthread.h>
#include <immintrin.h>
#include <semaphore.h>
#include "pagerank.h"

pthread_mutex_t locker = PTHREAD_MUTEX_INITIALIZER;
pthread_barrier_t barrier;

double* oldPr = NULL;
double* currPr = NULL;
page** pages = NULL;
double initScore;
double commonConst;
double dmp;
double norm = 0;
int isConverged = 0;
int pagesLeft;
int iteration = -1; 
/*
	Consider the list of pages as a task queue, each worker
	just fetch a page to work on from the queue once i has finished
	the calculation for the previous page.
*/

void* worker(void* arg){
	int index;
	page* pg;
	node* inlink;

	while(!isConverged){

		// Keep acquaire new tasks until finishing this iteration.
		while(pagesLeft){
			// Fetch the page index
			pthread_mutex_lock(&locker);
			index = --pagesLeft;
			pg = pages[index];
			inlink = pg->inlinks;
			pthread_mutex_unlock(&locker);

			// Do it!!!!!!	
			double tmpSum = 0;

			while(inlink){
				tmpSum += oldPr[inlink->page->index] / inlink->page->noutlinks;
				inlink = inlink->next;
			}
			currPr[index] = commonConst + tmpSum*dmp;
			// Carry out the current vector norm by the way.
			norm += (currPr[index] - oldPr[index]) * (currPr[index] - oldPr[index]);
		}

		pthread_barrier_wait(&barrier);
		// Main thread is checking if the next iteration is necessary;
		pthread_barrier_wait(&barrier);
	}
	pthread_exit(NULL);
}


void pagerank(node* lit, int npages, int nedges, int nthreads, double dampener){
	
	if(npages < nthreads){
		// Limit the number of threads to be used.
		nthreads = npages; 
	}

	/***** Initialization *****/	
	pthread_barrier_init(&barrier, NULL, nthreads + 1);
	pthread_t threads[nthreads];

	oldPr = malloc(sizeof(double) * npages);
	currPr = malloc(sizeof(double) * npages);
	pages = malloc(sizeof(page*) * npages);
	initScore = 1.00 / npages;
	commonConst = (1.00 - dampener) / npages; 
	pagesLeft = npages;
	dmp = dampener;
	double boundary = EPSILON*EPSILON;
	// Set initial pageranks for the first iteration, and prepare
	// an array to store page pointers for faster search. 
	for(int i = 0; i < npages; ++i){
		oldPr[i] = initScore;
		pages[i] = lit->page;
		lit = lit->next;
	}

	/***** Here we go!! *****/
	for(int i = 0; i < nthreads; ++i){
		if(pthread_create(&threads[i], NULL, worker, NULL) != 0){
			printf("Creating new thread failed....");
		}
	}

	/***** Main thread is also the coordinator *****/
	while(!isConverged){
		pthread_barrier_wait(&barrier);
		if(norm <= boundary){
			isConverged = 1;
		}

		// Update/Reset for the next iteration.
		double* tmp = oldPr;
		oldPr = currPr;
		currPr = tmp;
		norm = 0.00;
		pagesLeft = npages;
		pthread_barrier_wait(&barrier);
	}

	/***** Finishing! Yay! *****/
	for(int i = 0; i < nthreads; ++i)
		pthread_join(threads[i], NULL);
	
	/***** Print out results *****/
	for(int i = 0; i < npages; ++i)
		printf("%s %.4lf\n", pages[i]->name, oldPr[i]);


	free(oldPr);
	free(currPr);
	free(pages);
}

/*
######################################
### DO NOT MODIFY BELOW THIS POINT ###
######################################
*/
int main(int argc, char** argv) {

	/*
	######################################################
	### DO NOT MODIFY THE MAIN FUNCTION OR HEADER FILE ###
	######################################################
	*/

	config conf;

	init(&conf, argc, argv);

	node* list = conf.list;
	int npages = conf.npages;
	int nedges = conf.nedges;
	int nthreads = conf.nthreads;
	double dampener = conf.dampener;

	pagerank(list, npages, nedges, nthreads, dampener);

	release(list);

	return 0;
}
