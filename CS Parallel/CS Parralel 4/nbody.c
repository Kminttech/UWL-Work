//Kevin Minter
//10/3/17
//Parralel n-boby simulation

#include <stdio.h>
#include <pthread.h>
#include <math.h>

#define G 6.67408E-11

int numBodies;
int totalTime;
int deltaTime;
int curTime;
double *mass;
double *posX;
double *posY;
double *velX;
double *velY;
double *forceX;
double *forceY;
int numWait;
pthread_mutex_t barrierLock;
pthread_cond_t barrierWait;
pthread_mutex_t *forceProtect;

void * computeBody(void *arg){
  int i;
  int myId = (int)arg;
  float x_diff, y_diff, dist, dist_cubed, forceChangeX, forceChangeY;
  while (curTime < totalTime){
		pthread_mutex_lock(&barrierLock);
		numWait +=1;
		if(numWait == numBodies){
			numWait = 0;
			pthread_cond_broadcast(&barrierWait);
		}else{
      pthread_cond_wait(&barrierWait, &barrierLock);
    }
    pthread_mutex_unlock(&barrierLock);
    for(i = myId+1; i < numBodies; i++){
      x_diff = posX[myId] - posX[i];
      y_diff = posY[myId] - posY[i];
      dist = sqrt(x_diff*x_diff + y_diff*y_diff);
      dist_cubed = dist*dist*dist;
      forceChangeX = G*mass[myId]*mass[i]/dist_cubed*x_diff;
      forceChangeY = G*mass[myId]*mass[i]/dist_cubed*y_diff;
      pthread_mutex_lock(&forceProtect[myId]);
      forceX[myId] += forceChangeX;
      forceY[myId] += forceChangeY;
      pthread_mutex_unlock(&forceProtect[myId]);
      pthread_mutex_lock(&forceProtect[i]);
      forceX[i] -= forceChangeX;
      forceY[i] -= forceChangeY;
      pthread_mutex_unlock(&forceProtect[i]);
    }
    //Wait for others
  	pthread_mutex_lock(&barrierLock);
    if(myId == 0){
      curTime+=deltaTime;
    }
  	numWait +=1;
    if(numWait == numBodies){
			numWait = 0;
			pthread_cond_broadcast(&barrierWait);
		}else{
      pthread_cond_wait(&barrierWait, &barrierLock);
    }
    pthread_mutex_unlock(&barrierLock);
    posX[myId] = posX[myId]+ deltaTime*velX[myId];
    posY[myId] = posY[myId]+ deltaTime*velY[myId];
    velX[myId] = velX[myId] + deltaTime/mass[myId]*forceX[myId];
    velY[myId] = velY[myId] + deltaTime/mass[myId]*forceY[myId];
  }
}

void nbodyCalc(){
  int i;
  pthread_t *threads = malloc(numBodies * sizeof(pthread_t));
  for(i = 0; i < numBodies; i++){
    pthread_create(&threads[i],NULL,computeBody,(void *)i);
  }
  for(i = 0; i < numBodies; i++){
    pthread_join(threads[i],NULL);
  }
}

int	main(int argc,	char **	argv)	{
	FILE *input = fopen(argv[1],"r");
  int i;
  fscanf(input, "%d\n%d\n%d\n", &numBodies, &totalTime, &deltaTime);
	curTime = 0;
	numWait = 0;
  mass = malloc(sizeof(double) * numBodies);
  posX = malloc(sizeof(double) * numBodies);
  posY = malloc(sizeof(double) * numBodies);
  velX = malloc(sizeof(double) * numBodies);
  velY = malloc(sizeof(double) * numBodies);
  forceX = malloc(sizeof(double) * numBodies);
  forceY = malloc(sizeof(double) * numBodies);
  forceProtect = malloc(sizeof(pthread_mutex_t) * numBodies);
	pthread_mutex_init(&barrierLock,NULL);
	pthread_cond_init(&barrierWait,NULL);
  for(i = 0; i < numBodies; i++){
    fscanf(input, "%lf %lf %lf %lf %lf", &mass[i], &posX[i], &posY[i], &velX[i], &velY[i]);
    pthread_mutex_init(&forceProtect[i],NULL);
  }
  nbodyCalc();
  for(i = 0; i < numBodies; i++){
    printf("%e %e %e %e %e\n", mass[i], posX[i], posY[i], velX[i], velY[i]);
  }
	return 0;
}
