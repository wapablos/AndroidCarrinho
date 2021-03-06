public class DijkstraAlgorithm {
    // A utility function to find the vertex with minimum distance value, 
    // from the set of vertices not yet included in shortest path tree 
    private int x = -1, y = 0;
    private final int INF = 100;
    private int[][] pathVect = null;
    private char[][] cmdVect = null;
    private final int[] PathValue = new int[3];
    private final int[][] adjacencyMatrix = { { 0, INF, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0}, 
                                              { 1,  0,  2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
                                              { 0,  3,  0, 2, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0}, 
                                              { 0,  0,  3, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
                                              { 0,  0,  0, 3, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
                                              { 0,  0,  0, 0, 5, 0, 3, 0, 0, 0, 0, 0, 0, 0, 5}, 
                                              { 0,  0,  0, 0, 0, 2, 0, 3, 0, 0, 0, 0, 0, 0, 0}, 
                                              { 0,  0,  5, 0, 0, 0, 3, 0, 3, 0, 0, 0, 5, 0, 0}, 
                                              { 0,  0,  0, 0, 0, 0, 0, 3, 0, 3, 0, 0, 0, 0, 0},
                                              {INF, 0,  0, 0, 0, 0, 0, 0, 3, 0, 5, 0, 0, 0, 0},
                                              { 0,  0,  0, 0, 0, 0, 0, 0, 0, 4, 0, 3, 0, 0, 0},
                                              { 0,  0,  0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 3, 0, 0},
                                              { 0,  0,  0, 0, 0, 0, 0, 5, 0, 0, 0, 2, 0, 3, 0},
                                              { 0,  0,  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 2},
                                              { 0,  0,  0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 2, 0} };
    private static final int NO_PARENT = -1;
    // Function that implements Dijkstra's 
    // single source shortest path 
    // algorithm for a graph represented  
    // using adjacency matrix 
    // representation 
    public void dijkstra(int startVertex) 
    { 
        int nVertices = adjacencyMatrix[0].length; 
  
        // shortestDistances[i] will hold the 
        // shortest distance from src to i 
        int[] shortestDistances = new int[nVertices]; 
  
        // added[i] will true if vertex i is 
        // included / in shortest path tree 
        // or shortest distance from src to  
        // i is finalized 
        boolean[] added = new boolean[nVertices]; 
        
        pathVect = new int[nVertices-1][nVertices];
        
        for(int j=0;j<(nVertices-1);j++){
            for(int i=0;i<nVertices;i++){
                pathVect[j][i]=-1;
            }
        } //Caso pathVect[j] = -1 o percurso terminou
        switch(startVertex){
            case 3:
                adjacencyMatrix[3][2] = INF;
            break;
            case 6:
                adjacencyMatrix[6][7] = INF;
            break;
            case 13:
                adjacencyMatrix[13][12] = INF;
            break;
        }
        
        // Initialize all distances as  
        // INFINITE and added[] as false 
        for (int vertexIndex = 0; vertexIndex < nVertices; vertexIndex++) 
        { 
            shortestDistances[vertexIndex] = Integer.MAX_VALUE; 
            added[vertexIndex] = false; 
        } 
          
        // Distance of source vertex from 
        // itself is always 0 
        shortestDistances[startVertex] = 0; 
  
        // Parent array to store shortest 
        // path tree 
        int[] parents = new int[nVertices]; 
  
        // The starting vertex does not  
        // have a parent 
        parents[startVertex] = NO_PARENT; 
  
        // Find shortest path for all  
        // vertices 
        for (int i = 1; i < nVertices; i++) 
        { 
  
            // Pick the minimum distance vertex 
            // from the set of vertices not yet 
            // processed. nearestVertex is  
            // always equal to startNode in  
            // first iteration. 
            int nearestVertex = -1; 
            int shortestDistance = Integer.MAX_VALUE; 
            for (int vertexIndex = 0; vertexIndex < nVertices; vertexIndex++) 
            { 
                if (!added[vertexIndex] && shortestDistances[vertexIndex] < shortestDistance)  
                { 
                    nearestVertex = vertexIndex; 
                    shortestDistance = shortestDistances[vertexIndex]; 
                } 
            } 
  
            // Mark the picked vertex as 
            // processed 
            added[nearestVertex] = true; 
  
            // Update dist value of the 
            // adjacent vertices of the 
            // picked vertex. 
            for (int vertexIndex = 0; vertexIndex < nVertices; vertexIndex++)  
            { 
                int edgeDistance = adjacencyMatrix[nearestVertex][vertexIndex]; 
                  
                if (edgeDistance > 0 && ((shortestDistance + edgeDistance) < shortestDistances[vertexIndex]))  
                { 
                    parents[vertexIndex] = nearestVertex; 
                    shortestDistances[vertexIndex] = shortestDistance + edgeDistance; 
                } 
            } 
        } 
        if (startVertex != 0)
            printPath(0,parents);
        else
            printSolution(startVertex, shortestDistances, parents);
        findCmdVect(startVertex, nVertices);
    } 
  
    // A utility function to print  
    // the constructed distances 
    // array and shortest paths 
    private void printSolution(int startVertex, int[] distances, int[] parents) 
    { 
        int nVertices = distances.length;  
          
        for (int vertexIndex = 0; vertexIndex < nVertices; vertexIndex++)  
        { 
            if (vertexIndex != startVertex)  
            { 
                printPath(vertexIndex, parents); 
            } 
        } 
    } 
  
    // Function to print shortest path 
    // from source to currentVertex 
    // using parents array 
    private void printPath(int currentVertex, int[] parents) 
    { 
        // Base case : Source node has 
        // been processed 
        if (currentVertex == NO_PARENT) 
        { 
            x++; y=0;
            return; 
        }
        printPath(parents[currentVertex], parents); 
        pathVect[x][y] = currentVertex;
        y++;
    }
    
    private void findCmdVect(int startVertex, int nVertices){
        cmdVect = new char[nVertices-1][nVertices];
        boolean pathV2, pathV1;
        int u = 0;
        for(int j=0;j<(nVertices-1);j++){
            for(int i=0;i<nVertices;i++){
                cmdVect[j][i]=' ';
            }
        }
        
        for(int i=0;i<(nVertices-1);i++){
            u = 0;
            for(int j=0; j<nVertices;j=j+2){
                PathValue[0] = pathVect[i][j];
                PathValue[1] = pathVect[i][j+1];
                PathValue[2] = pathVect[i][j+2];
                
                if(PathValue[0] == -1)
                    break;
                
                if(PathValue[1] != -1)
                    pathV1 = true;
                else
                {
                    PathValue[1] = 0;
                    pathV1 = false;
                }
                if(PathValue[2] != -1)
                    pathV2 = true;
                else
                {
                    PathValue[2] = 0;
                    pathV2 = false;
                }
                System.out.println(PathValue[0]);
                System.out.println(PathValue[1]);
                System.out.println(PathValue[2]);
                SearchConditions(pathV1,pathV2,i,u);
                SearchConditions(false,pathV2,i,u+1);
                u++;
            }
        }
    }
    
    private void SearchConditions(boolean pathV1, boolean pathV2, int i, int u){
        if((adjacencyMatrix[PathValue[0]][PathValue[1]] >= 4 && pathV1)||
          ((adjacencyMatrix[PathValue[0]][PathValue[1]] + adjacencyMatrix[PathValue[1]][PathValue[2]]) < 7 && pathV2)||
          (PathValue[0] == 0))
        {
            cmdVect[i][u] = 'f';
            System.out.println("frente");
        }
        if(((adjacencyMatrix[PathValue[0]][PathValue[1]] + adjacencyMatrix[PathValue[1]][PathValue[2]]) == 8) && pathV2)
        {
            cmdVect[i][u] = 'l';
            System.out.println("esquerda");
        }
        else
        {
            cmdVect[i][u] = 'r';
            System.out.println("direita");
        }
    }
    
    public int[][] getPathVect() {
        return pathVect;
    }
    
    public char[][] getCmdVect() {
        return cmdVect;
    }
}
