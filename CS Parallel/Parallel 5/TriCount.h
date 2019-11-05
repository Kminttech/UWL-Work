/*
 *
 */
 #include <mpi.h>
 #include <stdio.h>

/******************************
 * Defines
 ******************************/


/******************************
 * Structures
 ******************************/
 struct Graph{
   struct Vertex *vertices;
   int numVertices;
 };typedef struct Graph Graph;

 struct Vertex{
   struct Edge *edges1;
   struct Edge *edges2;
   int numNeighbors;
   int numOrderedNeighbors;
 };typedef struct Vertex Vertex;

 struct Edge{
   int vertex1;
   int vertex2;
   struct Edge *next1;
   struct Edge *next2;
 };typedef struct Edge Edge;

/******************************
 * Global Variables
 ******************************/


/******************************
 * Function declarations
 ******************************/
Graph* createGraph(int size);
void addEdge(struct Graph *g, int v1, int v2);
int* findNeighbors(struct Graph *g,int v);
int* findOrderedNeighbors(struct Graph *g,int v);
int degree(struct Graph *g,int v);
int orderedDegree(struct Graph *g,int v);
