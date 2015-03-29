package ie.dit;

//Simple weighted graph representation 
//Uses an Adjacency Linked Lists, suitable for sparse graphs

import java.io.*;
import java.util.Scanner;


class Heap
{
 private int[] h;	   // heap array
 private int[] hPos;	   // hPos[h[k]] == k
 private int[] dist;    // dist[v] = priority of v

 private int N;         // heap size

 // The heap constructor gets passed from the Graph:
 //    1. maximum heap size
 //    2. reference to the dist[] array
 //    3. reference to the hPos[] array
 public Heap(int maxSize, int[] _dist, int[] _hPos) 
 {
     N = 0;
     h = new int[maxSize + 1];
     dist = _dist;
     hPos = _hPos;
 }


 public boolean isEmpty() 
 {
     return N == 0;
 }


 public void siftUp( int k) 
 {
     int v = h[k];
     h[0] = 0;
     dist[0] = Integer.MIN_VALUE;
     
     while(dist[v] < dist[h[k / 2]]) {
    	 h[k] = h[k / 2];
    	 hPos[h[k]] = k;
    	 k = k / 2;
     }
     h[k] = v;
     hPos[v] = k;
     // code yourself
     // must use hPos[] and dist[] arrays
 }


 public void siftDown( int k) 
 {
     int v, j;
    
     v = h[k];  
     
     while( k <= N/2) {
    	 j = 2 * k;
    	 if(j < N && dist[h[j]] > dist[h[j + 1]]) j++;
    	 if(dist[v] <= dist[h[j]]) break;
    	 h[k] = h[j];
    	 hPos[h[k]] = k;
    	 k = j;
     }
     h[k] = v;
     hPos[v] = k;
 }


 public void insert( int x) 
 {
     h[++N] = x;
     siftUp( N);
 }


 public int remove() 
 {   
     int v = h[1];
     hPos[v] = 0; // v is no longer in heap
     h[N+1] = 0;  // put null node into empty spot
     
     h[1] = h[N--];
     siftDown(1);
     
     return v;
 }

}

class Graph {
 class Node {
     public int vert;
     public int wgt;
     public Node next;
 }
 
 // V = number of vertices
 // E = number of edges
 // adj[] is the adjacency lists array
 private int V, E;
 private Node[] adj;
 private Node z;
 private int[] mst;
 
 // used for traversing graph
 private int[] visited;
 private int id;
 
 
 // default constructor
 public Graph(String graphFile)  throws IOException
 {
     int u, v;
     int e, wgt;
     Node t;

     FileReader fr = new FileReader(graphFile);
		BufferedReader reader = new BufferedReader(fr);
	           
     String splits = " +";  // multiple whitespace as delimiter
		String line = reader.readLine();        
     String[] parts = line.split(splits);
     System.out.println("Parts[] = " + parts[0] + " " + parts[1]);
     
     V = Integer.parseInt(parts[0]);
     E = Integer.parseInt(parts[1]);
     
     // create sentinel node
     z = new Node(); 
     z.next = z;
     
     // create adjacency lists, initialised to sentinel node z       
     adj = new Node[V+1];        
     for(v = 1; v <= V; ++v)
         adj[v] = z;               
     
    // read the edges
     System.out.println("Reading edges from text file");
     for(e = 1; e <= E; ++e)
     {
         line = reader.readLine();
         parts = line.split(splits);
         u = Integer.parseInt(parts[0]);
         v = Integer.parseInt(parts[1]); 
         wgt = Integer.parseInt(parts[2]);
         
         System.out.println("Edge " + toChar(u) + "--(" + wgt + ")--" + toChar(v));    
         
         // write code to put edge into adjacency matrix   
         t = adj[u];
		 adj[u] = new Node();
		 adj[u].vert = v;
		 adj[u].wgt = wgt;
		 adj[u].next = t;
			
			
         t = adj[v];
		 adj[v] = new Node();
		 adj[v].vert = u;
		 adj[v].wgt = wgt;
		 adj[v].next = t;
         
     }	       
 }

 // convert vertex into char for pretty printing
 private char toChar(int u)
 {  
     return (char)(u + 64);
 }
 
 // method to display the graph representation
 public void display() {
     int v;
     Node n;
     
     for(v=1; v<=V; ++v){
         System.out.print("\nadj[" + toChar(v) + "] ->" );
         for(n = adj[v]; n != z; n = n.next) 
             System.out.print(" |" + toChar(n.vert) + " | " + n.wgt + "| ->");    
     }
     System.out.println("");
 }


 
	public void MST_Prim(int s)
	{
     int v, u;
     int wgt, wgt_sum = 0;
     int[]  dist, parent, hPos;
     Node t;

     //code here
     dist = new int[V + 1];
     parent = new int[V + 1];
     hPos = new int[V + 1];
     
     for(v = 0; v <= V; v++) {
    	 dist[v] = Integer.MAX_VALUE;
    	 parent[v] = 0;
    	 hPos[v] = 0;
    	 
     }
     Heap heap = new Heap(V + 1, dist,hPos);
     heap.insert(s);;
     dist[s] = 0;
     
     Heap pq =  new Heap(V, dist, hPos);
     pq.insert(s);
     
     while (!heap.isEmpty())
     {

         v = heap.remove();
         
         System.out.println("\n Adding edge {" + toChar(parent[v]) + "}--([" + dist[v] + "})--{" + toChar(v) + "}");
         
         //calculates the sum of the weights
         wgt_sum += dist[v];

         //prevents duplicates
         dist[v] = -dist[v];

         for (t = adj[v]; t != z; t = t.next)
         {

             if (t.wgt < dist[t.vert])
             {
                 dist[t.vert] = t.wgt;
                 parent[t.vert] = v;

                 //If the vertex is empty, insert next vertex
                 if (hPos[t.vert] == 0)
                 {
                     heap.insert(t.vert);
                 }
                 else //Else call sift up
                 {
                     heap.siftUp(hPos[t.vert]);
                 }
             }
         }
     System.out.print("\n\nWeight of MST = " + wgt_sum + "\n");
     
     mst = parent;                      		
	}
}
 
 public void showMST()
 {
         System.out.print("\n\nMinimum Spanning tree parent array is:\n");
         for(int v = 1; v <= V; ++v)
         System.out.println(toChar(v) + " -> " + toChar(mst[v]));
         System.out.println("");
 }

}

public class PrimLists {
 public static void main(String[] args) throws IOException
 {
	 Scanner in = new Scanner(System.in);
	 // The grapgh the user wants to use, the name is entered in
	 System.out.println("Enter in the text file name");
	 String fname = in.nextLine();
	 
	 // Starting Vertex entered in by the user
	 System.out.println("Enter in the starting vertex");
	 int s = in.nextInt();
	 
     Graph g = new Graph(fname);
    
     // Calls function display
     g.display();
     
     // calls function MST_Prime and the vertex number is passed
     g.MST_Prim(s);
     
     // Calls function showsMST.
     g.showMST();
            
     
 }
 
 
}
