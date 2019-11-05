#include <TriCount.h>

int main(int argc, char** argv) {
  File input = fopen(argv[1], "r");
  int numVert;
  fscanf(input, "%d\n",&numVert);
  Graph* triGraph = createGraph(numVert);
  int vert1;
  int vert2;
  while(fscanf(input, "%d %d\n",vert1,vert2) == 2){
    addEdge(triGraph,vert1,vert2);
  }
  // Initialize the MPI environment
  MPI_Init(NULL, NULL);
  // Get the number of processes
  int size;
  MPI_Comm_size(MPI_COMM_WORLD, &size);
  // Get the rank of the process
  int rank;
  MPI_Comm_rank(MPI_COMM_WORLD, &rank);

  if(rank == 0){

  }

}

Graph* createGraph(int size){
  int i;
  Graph *g = (Graph*) malloc(sizeof(Graph));
  g->vertices = (Vertex*) malloc(sizeof(Vertex)*size);
  for(i = 0; i < size; i++){
    g->vertices[i].edges1 = NULL;
    g->vertices[i].edges2 = NULL;
    g->vertices[i].numNeighbors = 0;
  }
  g->numVertices = size;
  return g;
}

void addEdge(struct Graph *g, int v1, int v2){
  Edge *newEdge = (Edge*) malloc(sizeof(Edge));
  if(v1 > v2){
    int temp = v1;
    v1 = v2;
    v2 = temp;
  }
  newEdge->vertex1 = v1;
  newEdge->vertex2 = v2;
  newEdge->next1 = g->vertices[v1].edges1;
  newEdge->next2 = g->vertices[v2].edges2;
  g->vertices[v1].numNeighbors++;
  g->vertices[v2].numNeighbors++;
}

int* findNeighbors(struct Graph *g,int v){
  int* neighbors = (int*) malloc(sizeof(int)*g->vertices[v].numNeighbors);
  struct Edge* e1 = g->vertices[v].edges1;
  struct Edge* e2 = g->vertices[v].edges2;
  int i = 0;
  while(e1 != NULL){
    neighbors[i] = e1->vertex2;
    e1 = e1->next1;
    i++;
  }
  while(e1 != NULL){
    neighbors[i] = e2->vertex1;
    e2 = e2->next2;
    i++;
  }
  return neighbors;
}

int* findOrderedNeighbors(struct Graph *g,int v){
  int* orderedNeighbors = (int*) malloc(sizeof(int)*g->vertices[v].numNeighbors);
  struct Edge* e1 = g->vertices[v].edges1;
  struct Edge* e2 = g->vertices[v].edges2;
  int degreeV = degree(g,v);
  int i = 0;
  while(e1 != NULL){
    if(degreeV <= degree(g,e1->vertex2)){
      orderedNeighbors[i] = e1->vertex2;
      i++;
    }
    e1 = e1->next1;
  }
  while(e2 != NULL){
    if(degreeV <= degree(g,e2->vertex1)){
      orderedNeighbors[i] = e2->vertex1;
      i++;
    }
    e2 = e2->next2;
  }
  g->vertices[v].numOrderedNeighbors = i;
  return orderedNeighbors;
}

int degree(struct Graph *g,int v){
  return g->vertices[v].numNeighbors;
}

int orderedDegree(struct Graph *g,int v){
  return g->vertices[v].numOrderedNeighbors;
}
