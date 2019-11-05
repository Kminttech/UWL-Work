#include <stdlib.h>
#include <string.h>
#include "persons.h"

struct Person *personCreate(char *f, char *l, int age, char *email){
  struct Person *p = malloc(sizeof(struct Person));
  if(p == NULL){
    return NULL;
  }
  p->first = strdup(f);
  p->last = strdup(l);
  p->age = age;
  p->email = strdup(email);
  return p;
}

char *personFirstName(struct Person *p){
  return p->first;
}

char *personLastName(struct Person *p){
  return p->last;
}

char *personEmail(struct Person *p){
  return p->email;
}

int personAge(struct Person *p){
  return p->age;
}

void personDelete(struct Person *p){
  free(p->first);
  free(p->last);
  free(p->email);
  free(p);
}


struct PersonList *plCreate(){
  struct PersonList *l = malloc(sizeof(struct PersonList));
  l->size = 0;
  l->first = NULL;
  return l;
}

void plDelete(struct PersonList *db, enum deleteType dtype){
  struct Node *curNode = db->first;
  struct Node *nextNode = NULL;
  while (curNode!=NULL) {
    if(dtype == PD_DEEP){
      personDelete(curNode->value);
    }
    nextNode = curNode->next;
    free(curNode);
    curNode = nextNode;
  }
}

void plAdd(struct PersonList *db, struct Person *p){
  struct Node *n = malloc(sizeof(struct Node));
  n->value = p;
  n->next = db->first;
  db->first = n;
  db->size++;
}

void plRemovePerson(struct PersonList*db, struct Person *p){
  struct Node *prevNode = NULL;
  struct Node *curNode = db->first;
  while (curNode->value != p) {
    prevNode = curNode;
    curNode = curNode->next;
  }
  if(prevNode == NULL){
    db->first = curNode->next;
  }else{
    prevNode->next = curNode->next;
  }
  free(curNode);
  db->size--;
}

struct PersonList *plFindByFirstName(struct PersonList *db, char *first){
  struct PersonList *matches = plCreate();
  struct Node *curNode = db->first;
  while (curNode != NULL) {
    if(strcmp(curNode->value->first,first) == 0){
      plAdd(matches,curNode->value);
    }
    curNode = curNode->next;
  }
  return matches;
}

struct PersonList *plFindByLastName(struct PersonList *db, char *last){
  struct PersonList *matches = plCreate();
  struct Node *curNode = db->first;
  while (curNode != NULL) {
    if(strcmp(curNode->value->last,last) == 0){
      plAdd(matches,curNode->value);
    }
    curNode = curNode->next;
  }
  return matches;
}

struct PersonList *plFindAtLeastAsOldAs(struct PersonList *db, int age){
  struct PersonList *matches = plCreate();
  struct Node *curNode = db->first;
  while (curNode != NULL) {
    if(curNode->value->age >= age){
      plAdd(matches,curNode->value);
    }
    curNode = curNode->next;
  }
  return matches;
}

struct Person *plGet(struct PersonList *db ,int n){
  if(n >= db->size){
    return NULL;
  }
  struct Node *curNode = db->first;
  int i;
  for(i = 0; i < n; i++){
    curNode = curNode->next;
  }
  return curNode->value;
}

int plSize(struct PersonList *db){
  return db->size;
}
