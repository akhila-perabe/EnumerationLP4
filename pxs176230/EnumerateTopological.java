package pxs176230;

import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Timer;
import rbk.Graph.Vertex;
import rbk.Graph.Edge;
import rbk.Graph.Factory;
import rbk.Graph;

public class EnumerateTopological extends GraphAlgorithm<EnumerateTopological.EnumVertex> {
    boolean print;  // Set to true to print array in visit
    long count;      // Number of permutations or combinations visited
    Selector sel;
    public EnumerateTopological(Graph g) {
        super(g, new EnumVertex());
        print = false;
        count = 0;
        sel = new Selector();
    }

    static class EnumVertex implements Factory {
        int indegree;
        EnumVertex() {
            indegree = 0;
        }
        public EnumVertex make(Vertex u) { return new EnumVertex();	}
    }


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


    // To do: LP4; return the number of topological orders of g
    public long enumerateTopological(boolean flag) {
        print = flag;
        intializeIndegrees();
        enumerateTopological(g.getVertexArray(),g.size(),g.size());
        return count;
    }

    /**
     * method to initialize indegree of each enumvertex
     */
    private void intializeIndegrees(){
       for(Vertex v : g.getVertexArray()){
           get(v).indegree = v.inDegree();
       }
    }

    private void swap(Vertex[] vertices,int i,int j){
        Vertex temp = vertices[i];
        vertices[i] = vertices[j];
        vertices[j] = temp;
    }

    private void enumerateTopological(Vertex[] vertices,int k,int c){
        if(c==0){
            sel.visit(vertices,k);
        }
        else{
            int d = k - c;
            for(int i = d; i < k; i++){
                Vertex u = vertices[i];
                if(sel.select(u)){
                    swap(vertices,d,i);
                    enumerateTopological(vertices,k,c-1);
                    swap(vertices,d,i);
                    sel.unselect(u);
                }
            }
        }
    }

    //-------------------static methods----------------------

    public static long countTopologicalOrders(Graph g) {
        EnumerateTopological et = new EnumerateTopological(g);
        return et.enumerateTopological(false);
    }

    public static long enumerateTopologicalOrders(Graph g) {
        EnumerateTopological et = new EnumerateTopological(g);
        return et.enumerateTopological(true);
    }

    public static void main(String[] args) throws Exception {
        int VERBOSE = 0;
        if(args.length > 0) { VERBOSE = Integer.parseInt(args[0]); }
        Graph g = Graph.readDirectedGraph(new java.util.Scanner(System.in));
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
