/**
 * @author Akhila Perabe (axp178830), Pooja Srinivasan (pxs176230), Shreeya Girish Degaonkar (sxd174830)
 * 
 * DFS algorithm
 */

package pxs176230;

import rbk.Graph.Vertex;
import rbk.Graph.Edge;
import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Factory;
import rbk.Graph;
import java.io.File;
import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;

public class DFS extends GraphAlgorithm<DFS.DFSVertex> {
    // Class to store information about vertices during DFS
    public static int time = 0;
    private static List<Vertex> topoOrderVertices = new LinkedList<>();
    public static class DFSVertex implements Factory {
        int cno;
        int color; // 0 - white, 1 - grey, 2 - black
        Vertex parent;
        int startTime;
        int endTime;
        public DFSVertex(Vertex u) {
            color = 0;
            parent = null;
            startTime = 0;
            endTime = 0;
        }
        public DFSVertex make(Vertex u) { return new DFSVertex(u); }
    }
    // code to initialize storage for vertex properties is in GraphAlgorithm class
    public DFS(Graph g) {
        super(g, new DFSVertex(null));
    }

    public static DFS depthFirstSearch(Graph g) {
       DFS d = new DFS(g);
       int componentNumber = 1;
       for(Vertex v:g){
           if(d.get(v).color==0){
               try{
                   d.depthFirstSearch(g,v,componentNumber++);
               }
               catch (Exception e) {
                   System.out.println(e.getMessage());
                   return null;
               }
           }
       }
       return d;
    }

    private void depthFirstSearch(Graph g,Vertex u,int componentNumber) throws Exception{
        get(u).color = 1;
        get(u).startTime = ++time;
        for(Edge e: g.incident(u)) {
           Vertex v = e.otherEnd(u);
           if(get(v).color==0){
               get(v).parent = u;
               depthFirstSearch(g,v,componentNumber);
           }
           else if(get(v).color==1){ // indicates loop
               throw new Exception("Cycle Detected. Cannot perform topological sorting");
           }
           else if(get(v).color == 2){  // back edge
              continue;
           }
        }
        topoOrderVertices.add(0,u);
        get(u).endTime = ++time;
        get(u).cno=componentNumber;
        get(u).color = 2;
    }

    // Member function to find topological order
    public List<Vertex> topologicalOrder1() {
       depthFirstSearch(this.g);
       return topoOrderVertices;
    }

    // Find the number of connected components of the graph g by running dfs.
    // Enter the component number of each vertex u in u.cno.
    // Note that the graph g is available as a class field via GraphAlgorithm.
    public int connectedComponents() {
        int max = 0;
        for(Vertex u:this.g){
            max = Math.max(max,get(u).cno);
        }
        return max;
    }

    // After running the onnected components algorithm, the component no of each vertex can be queried.
    public int cno(Vertex u) {
        return get(u).cno;
    }

    // Find topological oder of a DAG using DFS. Returns null if g is not a DAG.
    public static List<Vertex> topologicalOrder1(Graph g) {
        DFS d = new DFS(g);
        return d.topologicalOrder1();
    }

    // Find topological oder of a DAG using the second algorithm. Returns null if g is not a DAG.
    public static List<Vertex> topologicalOrder2(Graph g) {
        return null;
    }

    public static void main(String[] args) throws Exception {
        String string = "7 7   1 2 2   1 3 3   2 4 5   3 4 4   4 5 1   1 5 7   6 7 1";
        Scanner in;
        // If there is a command line argument, use it as file from which
        // input is read, otherwise use input from string.
        in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(string);

        // Read graph from input
        Graph g = Graph.readDirectedGraph(in);
        g.printGraph(false);

        DFS d = new DFS(g);
        List<Vertex>  orderedVertex = d.topologicalOrder1();
        System.out.println("Topological Ordering Using DFS:");
        for(Vertex u : orderedVertex){
            System.out.print(u.getName() + " ");
        }
        System.out.println();
    }
}