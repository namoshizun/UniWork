#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <unistd.h>
#include <stdbool.h>
#include <pthread.h>
#include <inttypes.h>
#include <strings.h>
#include <string.h>

#include "matrix.h"

static uint32_t g_seed = 0;

static ssize_t g_width = 0;
static ssize_t g_height = 0;
static ssize_t g_elements = 0;

static ssize_t g_nthreads = 1;
static ssize_t g_id = 0;
pthread_mutex_t idLocker = PTHREAD_MUTEX_INITIALIZER;

#define IDX(x, y) ((y) * g_width + (x))

typedef struct task task;
struct task{
    uint32_t* matrix;
    uint32_t* matrix_b;// is NULL if the task doesnt use two matrixes. 
    uint32_t* result; //  is NULL if the task doesn't return a new matri.
    char job [25];   // jobs are "add", "mul", "scalar_add", "scalar_mul" etc....
    uint32_t value; // can be scalar or the value for which the frequency to be sought
};


////////////////////////////////
///     UTILITY FUNCTIONS    ///
////////////////////////////////

/**
 * Returns pseudorandom number determined by the seed
 */
uint32_t fast_rand(void) {

    g_seed = (214013 * g_seed + 2531011);
    return (g_seed >> 16) & 0x7FFF;
}

/**
 * Sets the seed used when generating pseudorandom numbers
 */
void set_seed(uint32_t seed) {

    g_seed = seed;
}

/**
 * Sets the number of threads available
 */
void set_nthreads(ssize_t count) {

    g_nthreads = count;
}

/**
 * Sets the dimensions of the matrix
 */
void set_dimensions(ssize_t order) {

    g_width = order;
    g_height = order;

    g_elements = g_width * g_height;
}

/**
 * Displays given matrix
 */
void display(const uint32_t* matrix) {

    for (ssize_t y = 0; y < g_height; y++) {
        for (ssize_t x = 0; x < g_width; x++) {
            if (x > 0) printf(" ");
            printf("%" PRIu32, matrix[y * g_width + x]);
        }

        printf("\n");
    }
}

/**
 * Displays given matrix row
 */
void display_row(const uint32_t* matrix, ssize_t row) {

    for (ssize_t x = 0; x < g_width; x++) {
        if (x > 0) printf(" ");
        printf("%" PRIu32, matrix[row * g_width + x]);
    }

    printf("\n");
}

/**
 * Displays given matrix column
 */
void display_column(const uint32_t* matrix, ssize_t column) {

    for (ssize_t y = 0; y < g_height; y++) {
        printf("%" PRIu32 "\n", matrix[y * g_width + column]);
    }
}

/**
 * Displays the value stored at the given element index
 */
void display_element(const uint32_t* matrix, ssize_t row, ssize_t column) {

    printf("%" PRIu32 "\n", matrix[row * g_width + column]);
}

////////////////////////////////
///   MATRIX INITALISATIONS  ///
////////////////////////////////

/**
 * Returns new matrix with all elements set to zero
 */
uint32_t* new_matrix(void) {

    return calloc(g_elements, sizeof(uint32_t));
}

/**
 * Returns new identity matrix
 */
uint32_t* identity_matrix(void) {

    uint32_t* matrix = new_matrix();

    for (ssize_t i = 0; i < g_width; i++) {
        matrix[i * g_width + i] = 1;
    }

    return matrix;
}

/**
 * Returns new matrix with elements generated at random using given seed
 */
uint32_t* random_matrix(uint32_t seed) {

    uint32_t* matrix = new_matrix();

    set_seed(seed);

    for (ssize_t i = 0; i < g_elements; i++) {
        matrix[i] = fast_rand();
    }

    return matrix;
}

/**
 * Returns new matrix with all elements set to given value
 */
uint32_t* uniform_matrix(uint32_t value) {

    uint32_t* matrix = new_matrix();

    for (ssize_t i = 0; i < g_elements; i++) {
        matrix[i] = value;
    }

    return matrix;
}

/**
 * Returns new matrix with elements in sequence from given start and step
 */
uint32_t* sequence_matrix(uint32_t start, uint32_t step) {

    uint32_t* matrix = new_matrix();
    uint32_t current = start;

    for (ssize_t i = 0; i < g_elements; i++) {
        matrix[i] = current;
        current += step;
    }

    return matrix;
}


////////////////////////////////
///     MATRIX OPERATIONS    ///
////////////////////////////////

/**
 * Returns new matrix with elements cloned from given matrix
 */
uint32_t* cloned(const uint32_t* matrix) {

    uint32_t* result = new_matrix();

    for (ssize_t i = 0; i < g_elements; i++) {
        result[i] = matrix[i];
    }

    return result;
}

/**
 * Returns new matrix with elements ordered in reverse
 */
uint32_t* reversed(const uint32_t* matrix) {

    uint32_t* result = new_matrix();

    for (ssize_t i = 0; i < g_elements; i++) {
        result[i] = matrix[g_elements - 1 - i];
    }

    return result;
}

/**
 * Returns new transposed matrix
 */
uint32_t* transposed(const uint32_t* matrix) {

    uint32_t* result = new_matrix();

    for (ssize_t y = 0; y < g_height; y++) {
        for (ssize_t x = 0; x < g_width; x++) {
            result[x * g_width + y] = matrix[y * g_width + x];
        }
    }

    return result;
}

/*---------Worker thread function for the following operations---- */
void *worker (void *order){
    // 'id' acutally indicates which block should this thread work on.
    ssize_t id;
    pthread_mutex_lock(&idLocker);
    id = g_id++;
    pthread_mutex_unlock(&idLocker);

    task* ptr = (task*) order;
    uint32_t ret = 0;
    // Thoese are essential for all computations
    uint32_t* matrix = ptr->matrix;
    char *job = ptr->job;

    ssize_t start = id * (g_elements/g_nthreads);
    ssize_t end = (id == g_nthreads - 1) ? g_elements : start + g_elements/g_nthreads;

    if(strcasecmp(job, "add") == 0){
        uint32_t* result = ptr->result;
        uint32_t* matrix_b = ptr->matrix_b;
        for(ssize_t i = start; i < end; ++i){
            result[i] = matrix[i] + matrix_b[i];
        }

    } else if(strcasecmp(job, "mul") == 0){
        uint32_t* result = ptr->result;
        uint32_t* matrix_b = ptr->matrix_b;
        for (ssize_t y = start; y < end; y++) {
            for (ssize_t x = start; x < end; x++) {
                for (ssize_t k = start; k < end; k++) {
                    result[IDX(x, y)] += matrix[IDX(k, y)] * matrix_b[IDX(x, k)];
                }
            }
        }

    } else if(strcasecmp(job, "scalar_add") == 0){
        uint32_t scalar = ptr->value; 
        uint32_t* result = ptr->result;
        for(ssize_t i = start; i < end; ++i){
            result[i] = matrix[i] + scalar;
        }

    } else if(strcasecmp(job, "scalar_mul") == 0){
        uint32_t scalar = ptr->value;
        uint32_t* result = ptr->result;
        for(ssize_t i = start; i < end; ++i){
            result[i] = matrix[i] * scalar;
        }

    } else if(strcasecmp(job, "min") == 0){
        uint32_t temp = matrix[start];
        for(ssize_t i = start + 1; i < end; ++i){
            if(matrix[i] <=temp){
                temp = matrix[i];
            }
        }
        ret = temp;

    } else if(strcasecmp(job, "max") == 0){
        uint32_t temp = matrix[start];
        for(ssize_t i = start + 1; i < end; ++i){
            if(matrix[i] >=temp){
                temp = matrix[i];
            }
        }
        ret = temp;

    } else if(strcasecmp(job, "sum") == 0){
        for(ssize_t i = start; i < end; ++i){
            ret += matrix[i];
        }

    } else if(strcasecmp(job, "frequency") == 0){
        uint32_t value = ptr->value; 
        for(ssize_t i = start; i < end; ++i){
            if(value == matrix[i]){
                ++ret;
            }
        }
    } 

    // as for jobs like mul and add, return is default (zero)
    // cannot cast to void* from a smaller integer type, sizeof(void*) is 64 on lab computeter
    // So use uint64_t to fix that problem... 
    uint64_t retx = (uint64_t) ret;
    pthread_exit((void*) retx); 
}

// Pass specifications to the new task
void init_task(task* new, uint32_t* matrix, uint32_t* matrix_b, 
               uint32_t* result, char job[25], uint32_t value){

    strcpy(new->job, job);
    new->matrix = matrix;
    new->value = value;

    new->matrix_b = matrix_b;
    new->result = result;
}

// Allocate worker threads with tasks and then complete the task
uint32_t do_task(task* new){
    // Limit the max numebr of threads
    if(g_nthreads > g_elements){
        g_nthreads = g_elements;
    }

    pthread_t threads[g_nthreads];
    void* ret = 0; // used to catch returns from worker functions
    uint32_t result = 0;


    for(ssize_t i = 0; i < g_nthreads; ++i){

        // Do it!!!!!!!
        pthread_create(&threads[i], NULL, worker, (void*)new);
    }

    /************** CATCH RETURN RESULTS *****************/
    // 'sum' and 'frequency' taks returns partial summariess, therefore need to add them up.
    if(strcasecmp(new->job, "sum") == 0|| strcasecmp(new->job, "frequency") == 0){

        for(ssize_t i = 0; i < g_nthreads; ++i){
            pthread_join(threads[i], &ret);
            result += (uint32_t) ret;
        }
    // 'min' amd 'max' task returns partial min or max value: need further comparision.
    } else if (strcasecmp(new->job, "min") == 0){

        uint32_t temp;
        pthread_join(threads[0], &ret);
        temp = (uint32_t) ret;

        for(ssize_t i = 1; i < g_nthreads; ++i){
            pthread_join(threads[i], &ret);
            if((uint32_t) ret < temp){
                temp = (uint32_t) ret;
            } 
        } 
        result = temp;

    } else if (strcasecmp(new->job, "max") == 0){

        uint32_t temp;
        pthread_join(threads[0], &ret);
        temp = (uint32_t) ret;

        for(ssize_t i = 1; i < g_nthreads; ++i){
            pthread_join(threads[i], &ret);
            if((uint32_t) ret > temp){
                temp = (uint32_t) ret;
            } 
        } 
        result = temp;

    } else {
        // add and mul cases: no return from worker threads.
        for(ssize_t i = 0; i < g_nthreads; ++i){
            
            pthread_join(threads[i], NULL);
        } 
    }

    g_id = 0;
    return result;
}

/**
 * Returns new matrix with scalar added to each element
 *
 *      example: 
 *      1 2        5 6
 *      3 4 + 4 => 7 8
 */
uint32_t* scalar_add(uint32_t* matrix, uint32_t scalar) {

    uint32_t* result = new_matrix();
    task* new = malloc(sizeof(task));

    init_task(new, matrix, NULL, result, "scalar_add", scalar);
    //                     ^^^ means no second matrix
    do_task(new);

    free(new);
    return result;
}

/**
 * Returns new matrix with scalar multiplied to each element
        example:

        1 2        2 4
        3 4 x 2 => 6 8
 */
uint32_t* scalar_mul(uint32_t* matrix, uint32_t scalar) {

    uint32_t* result = new_matrix();
    task* new = malloc(sizeof(task));

    init_task(new, matrix, NULL, result, "scalar_mul", scalar);
    do_task(new);

    free(new);
    return result;
}

/**
 * Returns new matrix with elements added at the same index
        example:

        1 2   4 4    5 6
        3 4 + 4 4 => 7 8
 */
uint32_t* matrix_add(uint32_t* matrix_a, uint32_t* matrix_b) {

    uint32_t* result = new_matrix();
    task* new = malloc(sizeof(task));

    init_task(new, matrix_a, matrix_b, result, "add", 0);
    do_task(new);

    free(new);
    return result;
}

/**
 * Returns new matrix, multiplying the two matrices together
         example:

        1 2   5 6    19 22
        3 4 x 7 8 => 43 50
 */
uint32_t* matrix_mul(uint32_t* matrix_a, uint32_t* matrix_b) {

    uint32_t* result = new_matrix();
    for (ssize_t y = 0; y < g_width; y++) {
        for (ssize_t k = 0; k < g_width; k++) {
            for (ssize_t x = 0; x < g_width; x++) {
                result[IDX(x, y)] += matrix_a[IDX(k, y)] * matrix_b[IDX(x, k)];
            }
        }
    }

    return result;
}

/**
 * Returns new matrix, powering the matrix to the exponent
         example: 

        1 2        199 290
        3 4 ^ 4 => 435 634
 */
uint32_t* matrix_pow(uint32_t* matrix, uint32_t exponent) {
    if(exponent == 0) return identity_matrix();
    if(exponent == 1) return cloned(matrix);
    uint32_t* result = new_matrix();
    
    if(exponent % 2 == 0) {
        result = matrix_pow(matrix, exponent/2);
        result = matrix_mul(result, result);
        return result;
    } else {
        return matrix_mul(matrix, matrix_pow(matrix, exponent - 1));
    }

    /*
    uint32_t* result =  cloned(matrix);
    if(exponent == 0) return identity_matrix();
    if(exponent == 1) return result;
    
    if(exponent % 2 == 1) {
        result = matrix_pow(matrix, exponent - 1);
        result = matrix_mul(matrix, result);
        return result;
    } else {
        result = matrix_pow(matrix, exponent/2);
        result = matrix_mul(matrix, result);
        return result;
    } */
}

////////////////////////////////
///       COMPUTATIONS       ///
////////////////////////////////

/**
 * Returns the sum of all elements
        example:

        1 2
        2 1 => 6
 */
uint32_t get_sum(uint32_t* matrix) {

    uint32_t result;
    task* new = malloc(sizeof(task));

    init_task(new, matrix, NULL, NULL, "sum", 0);
    //                           ^^^^ meand dont have to return a new matrix
    result = do_task(new);

    free(new);
    return result;
}

/**
 * Returns the trace of the matrix
         example:

        2 1
        1 2 => 4
 */
uint32_t get_trace(uint32_t* matrix) {

    uint32_t trace = 0;

    for(ssize_t i = 0; i < g_height; ++i){
        trace += matrix[g_height * i + i];
    }

    return trace;
}

/**
 * Returns the smallest value in the matrix
         example:

        4 3
        2 1 => 1
 */
uint32_t get_minimum(uint32_t* matrix) {

    uint32_t result;
    task* new = malloc(sizeof(task));

    init_task(new, matrix, NULL, NULL, "min", 0);
    result = do_task(new);

    free(new);
    return result;
}

/**
 * Returns the largest value in the matrix
        example:

        4 3
        2 1 => 4
 */
uint32_t get_maximum(uint32_t* matrix) {

    uint32_t result;
    task* new = malloc(sizeof(task));

    init_task(new, matrix, NULL, NULL, "max", 0);
    result = do_task(new);

    free(new);
    return result;
}

/**
 * Returns the frequency of the value in the matrix
         example

        1 1
        1 1 :: 1 => 4
 */
uint32_t get_frequency(uint32_t* matrix, uint32_t value) {

    uint32_t result = 0;
    task* new = malloc(sizeof(task));

    init_task(new, matrix, NULL, NULL, "frequency", value);
    result = do_task(new);

    free(new);
    
    return result;
}
