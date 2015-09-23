#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "snapshot.h"
// ------- COMMANDS DECLARATIONS AND INITIALIZATION --------
enum COMMAND {
	HELP,BYE,LIST_KEYS,LIST_ENTRIES,LIST_SNAPSHOTS,GET,
	DEL,PURGE,SET,PUSH,APPEND,PICK,PLUCK,POP,DROP,ROLLBACK,CHECKOUT,
	SNAPSHOT,MIN,MAX,SUM,LEN,REV,UNIQ,SORT,END
};

const char *COMMAND_STR[] = { "HELP\n", "BYE\n", "LIST KEYS\n", "LIST ENTRIES\n",
		"LIST SNAPSHOTS\n", "GET", "DEL", "PURGE", "SET", "PUSH", "APPEND",
		"PICK", "PLUCK", "POP", "DROP", "ROLLBACK", "CHECKOUT", "SNAPSHOT\n",
		"MIN", "MAX", "SUM", "LEN", "REV", "UNIQ", "SORT" };

/** A tagged union that represents a command */
struct command {
	enum COMMAND tag;
	char key[MAX_KEY_LENGTH];

	union {

		// SET, PUSH, APPEND.
		struct {
			int length;
			int values[MAX_LINE_LENGTH];
		};

		// PICK, PLUCK
		struct {
			int index;
		};

		// DROP, ROLLBACK, CHECKOUT
		struct {
			int snapshotID;
		};
	};
};

/** Finds the command enum matching the string */
enum COMMAND find_command(char *str) {
	enum COMMAND i;
	for (i = HELP; i < END; ++i) {
		if (strcmp(str, COMMAND_STR[i]) == 0) {
			return i;
		}
	}
	return END;
}

/* Prints error messgae if argyments passed to commmand is invalid
 * or no destinated object found.
 */
void printErrorMsg(enum COMMAND cmdTag) {

	switch (cmdTag) {

	case LIST_KEYS:
		puts("no keys\n\n");
		break;
	case LIST_ENTRIES:
		puts("no entries\n\n");
		break;
	case LIST_SNAPSHOTS:
		puts("no snapshots\n\n");
		break;
	case GET:
	case DEL:
		puts("no such key\n\n");
		break;
	case PICK:
		puts("index out of range\n\n");
		break;
	case DROP:
	case ROLLBACK:
	case CHECKOUT:
		puts("no such snapshot\n\n");
		break;
	default :
		break;
	}
}

static entry entry_head;
static snapshot snapshot_head;
// ---Two dynamicly changing pointers that allows to keep the track of list ---
static entry* current_entry = &entry_head;
//static snapshot* current_snapshot = &snapshot_head;
//static int topID = 0;

/** Checks the return value from *alloc */
void *check(void *ptr) {
	if (!ptr) {
		perror("*alloc failed");
		abort(); // no point cleaning up, just let the program die
	}
	return ptr;
}

/*------- Print functions  -------*/
static void printValues(value* current) {
	value *end = current;
	value *temp;

	for (current = current->next; current!= end; current = temp) {
		temp = current->next;
		if(temp == end){
			printf("%d", current->value); // So that final out put is not like [xxxxx ]
		} else {
			printf("%d ", current->value);
		}
	}
}

// LIST_KEYS
void printKeys() {
	entry *iterator = current_entry;
	if(current_entry == &entry_head){
		printf("no keys\n\n");
		return;
	}
	for (; iterator != &entry_head; iterator = iterator->prev)
		printf("%s\n", iterator->key);
	printf("\n");
}

// LIST_ENTRIES
void printEntries() {
	entry *iterator = current_entry;
	if(current_entry == &entry_head){
		printf("no entries\n\n");
		return;
	}
	while (iterator != &entry_head) {
		printf("%s ", iterator->key);
		printf(" [");
		printValues(iterator->values);
		printf("]\n");

		iterator = iterator->prev;
	}
	printf("\n");
}

// LIST_SNAPSHOTS
void printSnapshots(){
	printf("no snapshots\n\n");
	return;
}


// Returns the address according to the key
entry* findEntry(char *key) {
	char *ptr = key;
	while(*ptr != '\0'){
		if(*ptr == '\n'){
			*ptr = '\0';
		}
		++ptr;
	} // REMOVE THE ANNOYING '\N' !!!!!! POINTER MELTS BRAIN!

	entry* iterator = current_entry;
	for (; iterator != &entry_head; iterator = iterator->prev) {
		if (strcmp(key, iterator->key) == 0) {
			return iterator;
		}
	}
	return &entry_head;
}

// Delete all the values after 'start'
static void clearEntry(value *current) {
	value *temp;
	value *end = current;
	for (current = current->next; current!= end; current = temp) {
		temp = current->next;
		value_del(current);
		free(current);
	}
}

/* --- HELPER METHODS ---- */
void addValuesTail(struct command *cmd){
	entry *destEntry = findEntry(cmd->key);
	if (destEntry == &entry_head) {
		printErrorMsg(GET);
		return;
	} else {
		for (int i = 0; i < cmd->length; ++i) {
			value *new = check(malloc(sizeof(value)));
			new->value = cmd->values[i];
			value_addTail(new, destEntry);
		}
		printf("ok\n\n");
	}
}

value* locateValue(entry* ent, int index){
	value *valueIteraotr;
	int i;

	// Error handling:index out of range or no such key found.
	if (index <= 0) {
		printErrorMsg(PICK);
		return NULL;
	} else if(!ent->values){
		printf("nil\n\n");
		return NULL;
	} else {
		valueIteraotr = ent->values->next;
		for (i = 0; i < index && valueIteraotr != ent->values; ++i, valueIteraotr =
				valueIteraotr->next)
			;
		if (i != index) {
			// Meaning the index is beyond the length of list;
			printErrorMsg(PICK);
			return NULL;
		} else {
			return valueIteraotr->prev;
		}
	}
}

/**************** RETRIEVE COMMANDS ******************/
// GET
void getValues(struct command *cmd) {
	entry* destEntry = findEntry(cmd->key);
	if (destEntry == &entry_head) {
		printErrorMsg(cmd->tag);
		return;
	}
	printf("[");
	printValues(destEntry->values);
	printf("]\n\n");
}

// PICK
void showThisValue(struct command *cmd) {
	entry *destEntry = findEntry(cmd->key);
	if(destEntry == &entry_head){
		printErrorMsg(GET);
		return;
	} else {
		value *val = locateValue(destEntry, cmd->index);
		if(val){
			printf("%d\n\n", val->value);
		}
	}
}

/****************** SET/ADD COMMANDS ******************/
//APPEND
void appendValues(struct command *cmd) {
	addValuesTail(cmd);
}

// PUSH
void addValuesFront(struct command *cmd) {
	entry *destEntry = findEntry(cmd->key);
	if (destEntry == &entry_head) {
		printErrorMsg(GET);
		return;
	} else {
		for (int i = 0; i < cmd->length; ++i) {
			value *new = check(malloc(sizeof(value)));
			new->value = cmd->values[i];
			value_addFront(new, destEntry);
		}
		printf("ok\n\n");
	}
}


// SET
void setEntry(struct command *cmd) {
	entry *destEntry = findEntry(cmd->key);
	if (destEntry == &entry_head) {
		// If the entry does not exist (ie, not a reset opreation),
		// then add the new entry to the list.
		entry *new = check(malloc(sizeof(entry)));
		new->hasValues = 0;
		strcpy(new->key, cmd->key);
		entry_addTail(new, &entry_head);

		current_entry = new;
		addValuesTail(cmd);
	} else{
		// Resetting a currently existing entry
		clearEntry(destEntry->values);
		addValuesTail(cmd);
	}
}


/*******************  Deletion Functions ***********************/
// DEL
void deleteEntry(struct command *cmd) {
	entry *destEntry = findEntry(cmd->key);
	if (destEntry == &entry_head) {
		printErrorMsg(GET);
	} else {

		if(destEntry == current_entry){
			current_entry = destEntry->prev;
		}
		clearEntry(destEntry->values);
		free(destEntry->values);
		entry_del(destEntry);
		free(destEntry);
		printf("ok\n\n");
	}
}

// POP
void removeFrontValue(struct command *cmd) {
	entry *destEntry = findEntry(cmd->key);
	if (destEntry == &entry_head) {
		printErrorMsg(GET);
		return;
	} else if(destEntry->values->next != destEntry->values){
		value *val = destEntry->values->next;
		printf("%d\n\n", val->value);
		value_del(val);
		free(val);
	} else {
		// The entry is already empty, no need to pop
		printf("nil\n\n");
	}
}

// PLUCK
void removeEntryValue(struct command *cmd) {

	entry *destEntry = findEntry(cmd->key);
	if(destEntry == &entry_head){
		printErrorMsg(GET);
		return;
	} else {
		value *val = locateValue(destEntry, cmd->index);
		if(val){
			printf("%d\n\n", val->value);
			value_del(val);
			free(val);
		}
	}
}


/********************* Values Manipulation  *********************/
// MIN
void minValue(struct command *cmd) { // <----- What if the entry has not yet stored any values...
	entry* destEntry = findEntry(cmd->key);
	int temp;

	if (destEntry == &entry_head) {
		printErrorMsg(GET);
		return;
	} else if(!destEntry->hasValues){
		printf("0\n\n");
		return;
	}
	value* iterator = destEntry->values->next;
	temp = iterator->value;
	while(iterator->next != destEntry->values){
		iterator = iterator->next;
		if(iterator->value < temp){
			temp = iterator->value;
		}
	}
	printf("%d\n\n", temp);
}
// MAX
void maxValue(struct command *cmd) {
	entry* destEntry = findEntry(cmd->key);
	int temp;
	if (destEntry == &entry_head) {
		printErrorMsg(GET);
		return;
	} else if(!destEntry->hasValues){
		printf("0\n\n");
		return;
	}
	value* iterator = destEntry->values->next;
	temp = iterator->value;
	while(iterator->next != destEntry->values){

		iterator = iterator->next;
		if(iterator->value > temp){ // '>' The only difference..
			temp = iterator->value;
		}
	}
	printf("%d\n\n", temp);
}
// SUM
void sumValues(struct command *cmd) {
	entry* destEntry = findEntry(cmd->key);
	value* iterator;
	int sum = 0;

	if (destEntry == &entry_head) {
		printErrorMsg(GET);
		return;
	} else if(!destEntry->hasValues){
		printf("0\n\n");
		return;
	}

	for (iterator = destEntry->values->next; iterator != destEntry->values;
			iterator = iterator->next){
		sum += iterator->value;
	}
	printf("%d\n\n", sum);
}

int numValues(struct command *cmd) {
	entry* destEntry = findEntry(cmd->key);
	value* iterator;
	if (destEntry == &entry_head) {
		printErrorMsg(GET);
		return 0;
	}
	int num = 0;

	for (iterator = destEntry->values->next; iterator != destEntry->values;
			iterator = iterator->next){
		++num;
	}
	return num;
}

// LEN
void showLength(struct command *cmd){
	int num = 0;
	num = numValues(cmd);
	if(num != 0){
		printf("%d\n\n", num);
	}
}

// Compare function for QSORT
int compare(const void *x, const void *y) {
    return (*(int *)x) - (*(int *)y);
}

// SORT
void sortValues(struct command *cmd) {
	entry* destEntry = findEntry(cmd->key);
	value *iterator = destEntry->values;

	if (destEntry == &entry_head) {
		printErrorMsg(GET);
		return;
	}
	// Copy all the values into an arry
	int valNum = numValues(cmd);
	int values[valNum];
	for(int i = 0; i < valNum; ++i){
		iterator = iterator->next;
		values[i] = iterator->value;
	}
	// sort it!
	qsort(values, valNum, sizeof(int), compare);

	// Return the sorted array back into command 'values';
	// Reset the entry using command;
	for(int i = 0; i < valNum; ++i) cmd->values[i] = values[i];
	setEntry(cmd);
}
// UNIQ
void removeRepeats(struct command *cmd) {
	entry *destEntry = findEntry(cmd->key);
	if (destEntry == &entry_head) {
		printErrorMsg(GET);
		return;
	} else {
		value *val = destEntry->values->next;
		value *temp;
		for (; val != destEntry->values; val = temp) {
			temp = val->next;
			if (val->value == temp->value) {
				value_del(val);
				free(val);
			}
		}
		printf("ok\n\n");
	}
}

// REV
void reverseValues(struct command *cmd){
	entry *destEntry = findEntry(cmd->key);
	if (destEntry == &entry_head) {
		printErrorMsg(GET);
		return;
	} else {
		value_reverseOrder(destEntry);
		printf("ok\n\n");
	}
}
/***********************SNAPSHOTS!!!!***************************/
void save(struct command *cmd){
/*	entry *copy_entHead = malloc(sizeof(entry));

	if(current_snapshot == &snapshot_head){
		snapshot *new = malloc(sizeof(snapshot));
		new->id = ++topID;
		snapshot_addTail(new, &snapshot_head);

		memcpy(copy_entHead, entry_head, sizeof(entry));
	}
*/
	printf("ok\n\n");
}

void clearData(struct command *cmd){
	printf("ok\n\n");
}

/****************TAKE INPUTS AND PARSE TO CMD***************************/
int getInputs(char* buffer, char **argv) {
	int argc = 0;
	char *token;

	fgets(buffer, MAX_LINE_LENGTH, stdin);

	// special case: the list functions
	if(strstr(buffer,"LIST")){
	    argv[0] = buffer;
	    ++argc;
	    return argc;
	}

	token = strtok(buffer, " ");
	while (token != NULL) {
		// Passing each input components into argv
		argv[argc] = token;
		++argc;
		// otherwise keep spliting the string
		token = strtok(NULL, " ");
	}
	return argc;
}


void parseArugment(struct command *cmd, char **argv, int argc) {

	switch (cmd->tag) {
	case GET:  case DEL:  case PURGE:
	case POP:  case MIN:  case MAX:
	case SUM:  case LEN:  case REV:
	case UNIQ: case SORT:
		strncpy(cmd->key, argv[1], MAX_KEY_LENGTH);
		break;

	case SET:  case PUSH:  case APPEND:
		strncpy(cmd->key, argv[1],MAX_KEY_LENGTH);
		cmd->length = argc - 2;
		for (int i = 0; i < cmd->length; ++i) {
			cmd->values[i] = atoi(argv[i + 2]); //Passing from char to int;
		}
		break;

	case PICK:  case PLUCK:
		strncpy(cmd->key, argv[1],MAX_KEY_LENGTH);
		cmd->index = atoi(argv[2]);
		break;

	case DROP:  case ROLLBACK:  case CHECKOUT:
		cmd->snapshotID = atoi(argv[1]);
		break;
	default :
		break;
	}
}
/*************INPUT PROCEDURE ENDS********************/

void execute(struct command *cmd) {

	switch(cmd -> tag){
		case LIST_KEYS: printKeys(); break;
		case LIST_ENTRIES: printEntries(); break;
		case LIST_SNAPSHOTS: printSnapshots();  break;
		case GET: getValues(cmd); break;
		case DEL: deleteEntry(cmd); break;
		case PURGE: clearData(cmd); break;
		case SET: setEntry(cmd); break;
		case PUSH: addValuesFront(cmd); break;
		case APPEND: appendValues(cmd);  break;
		case PICK: showThisValue(cmd); break;
		case PLUCK: removeEntryValue(cmd); break;
		case POP: removeFrontValue(cmd); break;
//		case DROP: deleteSnapshot(cmd); break;
//		case ROLLBACK: undoSnapshot(cmd); break; -- My confession....
//		case CHECKOUT: restoreData(cmd); break;
//		case SNAPSHOT: save(cmd); break;
		case MIN: minValue(cmd); break;
		case MAX: maxValue(cmd); break;
		case SUM: sumValues(cmd); break;
		case LEN: showLength(cmd); break;
		case REV: reverseValues(cmd); break;
		case UNIQ: removeRepeats(cmd); break;
		case SORT: sortValues(cmd); break;

		default:
			break;
	}
}

void free_entries() {
	entry *ent = current_entry;
	entry* temp;
	for (; ent != &entry_head; ent = temp){
		temp = ent->prev;
		clearEntry(ent->values); // clearEntry method doesn't free the HEAD value;
		free(ent->values);
		entry_del(ent);
		free(ent);
	}
}

void free_snapshots(){
	return;
}

int main(void) {

	entry_init(&entry_head);
	snapshot_init(&snapshot_head);

	while (1) {
		printf("> ");

		int argc;
		char **argv = check(malloc(sizeof(char *) * MAX_LINE_LENGTH));
		char *buffer = check(malloc(sizeof(char *) * MAX_LINE_LENGTH));
		argc = getInputs(buffer, argv); // Let argv store inputs, and return the number of
										// inputs to argc

		if (argc == 0) {
			puts("invalid input");
			continue;
		} else {

			struct command cmd;
			cmd.tag = find_command(argv[0]);
			parseArugment(&cmd, argv, argc);
			/*
			 * By this point, cmd can indicate which command is called
			 * and what are the aruguments
			 */

			switch (cmd.tag) {
			case HELP:
				printf("%s\n\n", instruction);
				break;
			case BYE:
				printf("bye\n");
				goto stop;
			default:
				execute(&cmd);
				break;
			}
		}
		free(argv);
		free(buffer);
		continue;

		stop:
			free(argv);
			free(buffer);
			free_entries();
			free_snapshots();
			break;
	}
	return 0;
}
