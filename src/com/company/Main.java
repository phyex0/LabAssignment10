//Halit Burak Yeşildal 18050111043

package com.company;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
	// write your code here
        int[][] matrix = new int[81][81];
        HashMap<String,Integer> cities = new HashMap<>();
        FileIO.cityReader(cities);
        FileIO.matrixAdder(matrix);
        HashMap<Integer,String> reverseCities = FileIO.reverseMap(cities);
        GFG.kruskalMST(matrix,reverseCities);
        MST mst = new MST();
        int[][] matrix2 = FileIO.matrixReverse(matrix);
        System.out.println("*********************************************************");
        mst.primMST(matrix2,reverseCities);


    }
}

class FileIO{

    //stores relationship of cities
    public static void matrixAdder(int[][] matrix){
        int INF = Integer.MAX_VALUE;
        try {
            File f = new File("src/com/company/weighted_graph.txt");
            if(!f.exists())
                f.createNewFile();

            Scanner scanner = new Scanner(f);

            for(int i=0;i<81;i++){
                for(int j=0;j<81;j++){
                    matrix[i][j]= scanner.nextInt();
                    if(matrix[i][j] == 0)
                        matrix[i][j] = INF;
                }
            }



        }catch(Exception e){
            e.printStackTrace();
            System.out.println("File Error");
        }

    }

    public static int[][] matrixReverse(int[][] matrix){
        int INF = Integer.MAX_VALUE;
        int[][] matrix2 = new int[81][81];
        for(int i=0;i<81; i++)
            for(int j=0;j<81;j++){
                matrix2[i][j] = matrix[i][j];
                if(matrix2[i][j] == INF)
                    matrix2[i][j] =0;
            }

        return matrix2;

    }

    //stores cities and plates.
    public static void cityReader(HashMap<String,Integer> hashMap){
        int i =1;
        try {
            File f = new File("src/com/company/cities.txt");
            if(!f.exists())
                f.createNewFile();

            Scanner scanner = new Scanner(f);
            while(scanner.hasNext())
                hashMap.put(scanner.nextLine(),i++);


        }catch(Exception e){
            e.printStackTrace();
            System.out.println("File Error");
        }

    }

    public static HashMap<Integer,String> reverseMap(HashMap<String,Integer> city){
        HashMap<Integer, String> reversedCity = new HashMap<>();
        for(String key : city.keySet())
            reversedCity.put(city.get(key),key);
        return reversedCity;
    }

}

class GFG
{

    static int V = 81;
    static int[] parent = new int[V];
    static int INF = Integer.MAX_VALUE;

    // Find set of vertex i
    static int find(int i)
    {
        while (parent[i] != i)
            i = parent[i];
        return i;
    }

    // Does union of i and j. It returns
// false if i and j are already in same
// set.
    static void union1(int i, int j)
    {
        int a = find(i);
        int b = find(j);
        parent[a] = b;
    }

    // Finds MST using Kruskal's algorithm
    static void kruskalMST(int cost[][], HashMap<Integer, String> map)
    {
        int mincost = 0; // Cost of min MST.

        // Initialize sets of disjoint sets.
        for (int i = 0; i < V; i++)
            parent[i] = i;

        // Include minimum weight edges one by one
        int edge_count = 0;
        while (edge_count < V - 1)
        {
            int min = INF, a = -1, b = -1;
            for (int i = 0; i < V; i++)
            {
                for (int j = 0; j < V; j++)
                {
                    if (find(i) != find(j) && cost[i][j] < min)
                    {
                        min = cost[i][j];
                        a = i;
                        b = j;
                    }
                }
            }

            union1(a, b);
            System.out.printf("Edge %d:(%s , %s) cost:%d \n",
                    edge_count++, map.get(a), map.get(b), min);
            mincost += min;
        }
        System.out.printf("\n Minimum cost= %d \n", mincost);
    }

}

class MST {
    // Number of vertices in the graph
    private static final int V = 81;

    // A utility function to find the vertex with minimum key
    // value, from the set of vertices not yet included in MST
    int minKey(int key[], Boolean mstSet[])
    {
        // Initialize min value
        int min = Integer.MAX_VALUE, min_index = -1;

        for (int v = 0; v < V; v++)
            if (mstSet[v] == false && key[v] < min) {
                min = key[v];
                min_index = v;
            }

        return min_index;
    }

    // A utility function to print the constructed MST stored in
    // parent[]
    void printMST(int parent[], int graph[][],HashMap<Integer,String> map)
    {   int totalCost=0;
        for (int i = 1; i < V; i++){
            System.out.printf("Edge %d:(%s , %s) cost:%d \n",
                    i-1, map.get(parent[i]), map.get(i),graph[i][parent[i]] );
            totalCost+= graph[i][parent[i]];
        }
        System.out.printf("\n Minimum cost= %d \n", totalCost);

    }

    // Function to construct and print MST for a graph represented
    // using adjacency matrix representation
    void primMST(int graph[][],HashMap<Integer,String> map)
    {
        // Array to store constructed MST
        int parent[] = new int[V];

        // Key values used to pick minimum weight edge in cut
        int key[] = new int[V];

        // To represent set of vertices included in MST
        Boolean mstSet[] = new Boolean[V];

        // Initialize all keys as INFINITE
        for (int i = 0; i < V; i++) {
            key[i] = Integer.MAX_VALUE;
            mstSet[i] = false;
        }

        // Always include first 1st vertex in MST.
        key[0] = 0; // Make key 0 so that this vertex is
        // picked as first vertex
        parent[0] = -1; // First node is always root of MST

        // The MST will have V vertices
        for (int count = 0; count < V - 1; count++) {
            // Pick thd minimum key vertex from the set of vertices
            // not yet included in MST
            int u = minKey(key, mstSet);

            // Add the picked vertex to the MST Set
            mstSet[u] = true;

            // Update key value and parent index of the adjacent
            // vertices of the picked vertex. Consider only those
            // vertices which are not yet included in MST
            for (int v = 0; v < V; v++)

                // graph[u][v] is non zero only for adjacent vertices of m
                // mstSet[v] is false for vertices not yet included in MST
                // Update the key only if graph[u][v] is smaller than key[v]
                if (graph[u][v] != 0 && mstSet[v] == false && graph[u][v] < key[v]) {
                    parent[v] = u;
                    key[v] = graph[u][v];
                }
        }

        // print the constructed MST
        printMST(parent, graph,map);
    }


}
