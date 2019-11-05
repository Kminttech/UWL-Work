//Kevin Minter
//10/3/17
//Parralel Shell Sort

#include <stdio.h>

void countingsort(int* nums,int size,int range){
  int i;
  int numOfThreads = 4;
  int *countedVals = malloc(sizeof(int)*numOfThreads*range);

  #pragma omp parallel for private(i) num_threads(numOfThreads)
  for(i = 0; i < size; i++){
    countedVals[omp_get_thread_num()*range+nums[i]]++;
  }
  #pragma omp parallel for private(i) num_threads(numOfThreads)
  for(i = 0; i < range; i++){
    int j;
    for(j = 1; j < numOfThreads; j++){
      countedVals[i] += countedVals[j*range+i];
    }
  }

  int temp = countedVals[0];
  for(i = 1; i < range; i++){
    countedVals[i] += temp;
    temp = countedVals[i];
  }

  #pragma omp parallel for private(i)
  for(i = 0; i < range; i++){
    int j;
    int start;
    if(i == 0){
      start = 0;
    }else{
      start = countedVals[i-1];
    }
    for(j = start; j < countedVals[i]; j++){
      nums[j] = i;
    }
  }
}

int	main(int argc,	char	**	argv)	{
  int count	=	atoi(argv[1]);
  int range = atoi(argv[2]);
  int* nums	=	(int*)	malloc(sizeof(int)*count);
  int i;
  for(i = 0;i < count; i++)	{
	 	nums[i] = random() % range;
  }
  countingsort(nums,count,range);
  if(argc > 3){
    FILE *f;
    f = fopen(argv[3],"w");
    for	(i = 0; i < count; i++)	{
  	 	 fprintf(f,	"%d\n",nums[i]);
    }
    fclose(f);
  }else{
    for	(i = 0; i < count; i++)	{
  	 	 printf("%d\n",nums[i]);
    }
  }
}
