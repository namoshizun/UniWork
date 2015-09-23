#ifndef SNAPSHOT_H
#define SNAPSHOT_H
#define MAX_KEY_LENGTH 16
#define MAX_LINE_LENGTH 1024
#include <stdbool.h>

typedef struct value value;
typedef struct entry entry;
typedef struct snapshot snapshot;

struct value {
	value* prev;
	value* next;
	int value;
};

struct entry {
	entry* prev;
	entry* next;
	value* values;
	char key[MAX_KEY_LENGTH];
	int hasValues;
};

struct snapshot {
	snapshot* prev;
	snapshot* next;
	entry* entries;
	int id;
};

// -----List Methods for Entry  -----------------------
void entry_init(entry *head) {
	head->next = head;
	head->prev = head;
}

void entry_addFront(entry *new, entry *head) {
	new->prev = head;
	new->next = head->next;

	new->next->prev = new;
	new->prev->next = new;
}

void entry_addTail(entry *new, entry *head) {
	new->prev = head->prev;
	new->next = head;

	new->prev->next = new;
	new->next->prev = new;
}

void entry_del(entry *ent) {
	ent->prev->next = ent->next;
	ent->next->prev = ent->prev;

	entry_init(ent);
}


// -----List Methods for Value  -----------------------
// Value list beign and terminates with NULLs, so its structure is different
void value_start(entry *ent) {
	value *head = malloc(sizeof(value));
	head->next = head;
	head->prev = head;
	ent->values = head;
	ent->hasValues = 1;
}

void value_addFront(value *new, entry *head) {
	if(!head->hasValues){
		value_start(head);
	}

	new->prev = head->values;
	new->next = head->values->next;

	new->next->prev = new;
	new->prev->next = new;
}

void value_addTail(value *new, entry *head) {
	if(!head->hasValues){
		value_start(head);
	}
	new->prev = head->values-> prev;
	new->next = head->values;

	new->prev->next = new;
	new->next->prev = new;
}

void value_del(value *val) {
	val->prev->next = val->next;
	val->next->prev = val->prev;
}

void value_reverseOrder(entry *ent){

	value *current = ent->values;
	do {
		value *next = current->next;
		current->next = current->prev;
		current->prev = next;
		current = next;
	} while (current != ent->values);
}

// -----List Methods for Snapshot  -----------------------
void snapshot_init(snapshot *head) {
	head->next = head;
	head->prev = head;
}
void snapshot_addFront(snapshot *new, snapshot *head) {
	new->prev = head;
	new->next = head->next;

	new->next->prev = new;
	new->prev->next = new;
}

void snapshot_addTail(snapshot *new, snapshot *head) {
	new->prev = head->prev;
	new->next = head;

	new->prev->next = new;
	new->next->prev = new;
}

void snapshot_del(snapshot *sshot) {
	sshot->prev->next = sshot->next;
	sshot->next->prev = sshot->prev;
}

const char* instruction =
		"BYE   clear database and exit\n"
				"HELP  display this help message\n"
				"\n"
				"LIST KEYS       displays all keys in current state\n"
				"LIST ENTRIES    displays all entries in current state\n"
				"LIST SNAPSHOTS  displays all snapshots in the database\n"
				"\n"
				"GET <key>    displays entry values\n"
				"DEL <key>    deletes entry from current state\n"
				"PURGE <key>  deletes entry from current state and snapshots\n"
				"\n"
				"SET <key> <value ...>     sets entry values\n"
				"PUSH <key> <value ...>    pushes each value to the front one at a time\n"
				"APPEND <key> <value ...>  append each value to the back one at a time\n"
				"\n"
				"PICK <key> <index>   displays entry value at index\n"
				"PLUCK <key> <index>  displays and removes entry value at index\n"
				"POP <key>            displays and removes the front entry value\n"
				"\n"
				"DROP <id>      deletes snapshot\n"
				"ROLLBACK <id>  restores to snapshot and deletes newer snapshots\n"
				"CHECKOUT <id>  replaces current state with a copy of snapshot\n"
				"SNAPSHOT       saves the current state as a snapshot\n"
				"\n"
				"MIN <key>  displays minimum entry value\n"
				"MAX <key>  displays maximum entry value\n"
				"SUM <key>  displays sum of entry values\n"
				"LEN <key>  displays number of entry values\n"
				"\n"
				"REV <key>   reverses order of entry values\n"
				"UNIQ <key>  removes repeated adjacent entry values\n"
				"SORT <key>  sorts entry values in ascending order\n";

#endif
