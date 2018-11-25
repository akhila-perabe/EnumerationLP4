/**
 * @author Akhila Perabe (axp178830), Pooja Srinivasan (pxs176230), Shreeya Girish Degaonkar (sxd174830)
 * 
 * Enumerate topological order for a graph
 */

package pxs176230;

import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Vertex;
import rbk.Graph.Edge;
import rbk.Graph.Factory;

import java.util.Scanner;

import rbk.Graph;

public class EnumerateTopological extends GraphAlgorithm<EnumerateTopological.EnumVertex> {
    boolean print;  	// Set to true to print array in visit
    long count;      	// Number of permutations or combinations visited
    Selector sel;
    
    /**
     * Constructor
     * @param g
     */
    public EnumerateTopological(Graph g) {
        super(g, new EnumVertex());
        print = false;
        count = 0;
        sel = new Selector();
    }

    /**
     * Factory class for EnumerateTopological 
     */
    static class EnumVertex implements Factory {
        int indegree;
        EnumVertex() {
            indegree = 0;
        }
        public EnumVertex make(Vertex u) { return new EnumVertex();	}
    }

    /**
     * Approver class for EnumerateToplogical
     */
    class Selector extends Enumerate.Approver<Vertex> {
        @Override
        public boolean select(Vertex u) {
            if(get(u).indegree == 0) {
                for(Edge e : g.incident(u)){
                    Vertex v = e.otherEnd(u);
                    get(v).indegree--;
                }
                return true;
            }
            return false;
        }

        @Override
        public void unselect(Vertex u) {
            for(Edge e : g.incident(u)){
                Vertex v = e.otherEnd(u);
                get(v).indegree++;
            }
        }

        @Override
        public void visit(Vertex[] arr, int k) {
            count++;
            if(print) {
                for(Vertex u: arr) {
                    System.out.print(u + " ");
                }
                System.out.println();
            }
        }
    }


    /**
     * Returns the number of topological orders of g
     * @param flag	true if enumeration required
     * @return
     */
    public long enumerateTopological(boolean flag) {
        print = flag;
        intializeIndegrees();
        enumerateTopological(g.getVertexArray(), g.size());
        return count;
    }

    /**
     * Initializes indegree of each vertex
     */
    private void intializeIndegrees(){
       for(Vertex v : g.getVertexArray()){
           get(v).indegree = v.inDegree();
       }
    }

    /**
     * Swap vertex i and j in the given vertices array
     * @param vertices
     * @param i
     * @param j
     */
    private void swap(Vertex[] vertices,int i,int j){
        Vertex temp = vertices[i];
        vertices[i] = vertices[j];
        vertices[j] = temp;
    }

    /**
     * Enumerate topological order for the given vertices array
     * @param vertices
     * @param k
     * @param c
     */
    private void enumerateTopological(Vertex[] vertices, int c){
    	int k = vertices.length;
        if(c==0){
            sel.visit(vertices, k);
        }
        else{
            int d = k - c;
            for(int i = d; i < k; i++){
                Vertex u = vertices[i];
                if(sel.select(u)){
                    swap(vertices,d,i);
                    enumerateTopological(vertices, c-1);
                    swap(vertices,d,i);
                    sel.unselect(u);
                }
            }
        }
    }

    //-------------------Static methods----------------------

    /**
     * Returns the count of topological orders
     * @param g
     * @return
     */
    public static long countTopologicalOrders(Graph g) {
        EnumerateTopological et = new EnumerateTopological(g);
        return et.enumerateTopological(false);
    }

    /**
     * Prints all topological orders and returns count
     * @param g
     * @return
     */
    public static long enumerateTopologicalOrders(Graph g) {
        EnumerateTopological et = new EnumerateTopological(g);
        return et.enumerateTopological(true);
    }

    /**
     * Sample driver program
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        int VERBOSE = 0;
        String string = "7 7   1 2 2   1 3 3   2 4 5   3 4 4   4 5 1   1 5 7   6 7 1";
        if(args.length > 0) { VERBOSE = Integer.parseInt(args[0]); }
        Scanner in = args.length > 1 ? new Scanner(new java.io.File(args[1])) : new Scanner(string);
        Graph g = Graph.readDirectedGraph(in);
        Graph.Timer t = new Graph.Timer();
        long result;
        if(VERBOSE > 0) {
            result = enumerateTopologicalOrders(g);
        } else {
            result = countTopologicalOrders(g);
        }
        System.out.println("\n" + result + "\n" + t.end());
    }

}
