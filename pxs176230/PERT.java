/**
 * @author Akhila Perabe (axp178830), Pooja Srinivasan (pxs176230), Shreeya Girish Degaonkar (sxd174830)
 * 
 * Implementation of PERT algorithm
 */

package pxs176230;

import rbk.Graph.Vertex;
import rbk.Graph.Edge;
import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Factory;
import rbk.Graph;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import pxs176230.DFS;

public class PERT extends GraphAlgorithm<PERT.PERTVertex> {
    public static class PERTVertex implements Factory {
    	
    	int duration;
    	int ec;
    	int lc;
    	int slack;

		public PERTVertex(Vertex u) {
        	duration = 0;
        }
        
        public PERTVertex make(Vertex u) { return new PERTVertex(u); }
        
        public int getDuration() {
			return duration;
		}

		public void setDuration(int duration) {
			this.duration = duration;
		}
		
		public int getEc() {
			return ec;
		}

		public void setEc(int ec) {
			this.ec = ec;
		}

		public int getLc() {
			return lc;
		}

		public void setLc(int lc) {
			this.lc = lc;
		}

		public int getSlack() {
			return slack;
		}

		public void setSlack(int slack) {
			this.slack = slack;
		}
    }

    int numCritical;	// Number of critical nodes
    
    /**
     * Constructor
     * @param g
     */
    public PERT(Graph g) {
        super(g, new PERTVertex(null));
    }
    
    /**
     * Implements the PERT algorithm 
     * @return true if g has cycles
     */
    public boolean pert() {
    	numCritical = 0;
    	
        //Add edges from s to all vertices
        //Add edges from all vertices to t
        Vertex s = g.getVertex(1);
        Vertex t = g.getVertex(g.size());
        int m = g.edgeSize();
        for(Vertex u : g) {
        	if(s != u)
        		g.addEdge(s, u, 1, ++m);
        	if(t != u)
        		g.addEdge(u, t, 1, ++m);        	
        }
    	
        // Get topological ordering using DFS algorithm
    	DFS dfs = new DFS(g);
    	LinkedList<Vertex> finishedList = (LinkedList<Vertex>) dfs.topologicalOrder1();
    	
    	if(finishedList == null) {
    		//If g is not DAG
    		return true;
    	}
    	
    	// Initialize EC
    	for(Vertex u : this.g) {
    		setEc(u, 0);
    	}
    	
    	// Calculate EC
    	for(Vertex u : finishedList) {
    		for (Edge e : g.outEdges(u)) {
    			Vertex v = e.otherEnd(u);
    			int dur = ec(u) + duration(v);
    			if(dur > ec(v)) {
    				setEc(v, dur);
    			}
    		}
    	}
    	
    	// Initialize LC
    	int maxTime = ec(g.getVertex(g.size()));
    	for(Vertex u : this.g) {
    		setLc(u, maxTime);
    	}
    	
    	// Calculate LC and Slack
    	Iterator<Vertex> it = finishedList.descendingIterator();
    	while(it.hasNext()) {
    		Vertex u = it.next();
    		
    		for(Edge e : g.outEdges(u)) {
    			Vertex v = e.otherEnd(u);
    			int dur = lc(v) - duration(v);
    			if(dur < lc(u)) {
    				setLc(u, dur);
    			}
    		}
    		
    		int slack = lc(u) - ec(u);
    		setSlack(u, slack);
    		if(slack == 0) {
    			numCritical++;
    		}
    	}
    	
        return false;
    }
    
    /**
     * Get duration of vertex u
     * @param u
     * @return
     */
    public int duration(Vertex u) {
    	return this.get(u).getDuration();
    }
    
    /**
     * Get EC of vertex u
     * @param u
     * @return
     */
    public int ec(Vertex u) {
        return this.get(u).getEc();
    }

    /**
     * Get LC of vertex u
     * @param u
     * @return
     */
    public int lc(Vertex u) {
        return this.get(u).getLc();
    }

    /**
     * Get Slack of vertex u
     * @param u
     * @return
     */
    public int slack(Vertex u) {
        return this.get(u).getSlack();
    }

    /**
     * Set duration of vertex u
     * @param u
     * @param d
     */
    public void setDuration(Vertex u, int d) {
    	this.get(u).setDuration(d);
    }
    
    /**
     * Set EC of vertex u
     * @param u
     * @param ec
     */
    public void setEc(Vertex u, int ec) {
    	this.get(u).setEc(ec);
    }

    /**
     * Set LC of vertex u
     * @param u
     * @param lc
     */
    public void setLc(Vertex u, int lc) {
    	this.get(u).setLc(lc);
    }
    
    /**
     * Set slack for vertex u
     * @param u
     * @param slack
     */
    public void setSlack(Vertex u, int slack) {
    	this.get(u).setSlack(slack);
    }
    
    /**
     * Returns the length of the critical path
     * @return
     */
    public int criticalPath() {
    	//EC of t
        return ec(g.getVertex(g.size()));
    }

    /**
     * Checks if vertex u is critical node or not
     * @param u
     * @return
     */
    public boolean critical(Vertex u) {
        return slack(u)==0;
    }

    /**
     * Returns the number of critical nodes in the graph
     * @return
     */
    public int numCritical() {
        return numCritical;
    }

    /**
     * Static method to call PERT algorithm
     * Creates the PERT object and calls obj.pert()
     * @param g
     * @param duration
     * @return
     */
    public static PERT pert(Graph g, int[] duration) {
    	
        PERT obj = new PERT(g);
        
        //Set duration for nodes
        for(Vertex u : g) {
        	obj.setDuration(u, duration[u.getIndex()]);
        }
    	
        if (obj.pert()) {
        	// If g has cycles
        	return null;
        } else {
        	//If g is DAG
        	return obj;
        }
    }

    /**
     * Sample driver program
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String graph = "11 12   2 4 1   2 5 1   3 5 1   3 6 1   4 7 1   5 7 1   5 8 1   6 8 1   6 9 1   7 10 1   8 10 1   9 10 1      0 3 2 3 2 1 3 2 4 1 0";
        Scanner in;
        // If there is a command line argument, use it as file from which
        // input is read, otherwise use input from string.
        in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(graph);
        Graph g = Graph.readDirectedGraph(in);
        g.printGraph(false);

        PERT p = new PERT(g);
        for(Vertex u: g) {
            p.setDuration(u, in.nextInt());
        }
        // Run PERT algorithm.  Returns null if g is not a DAG
        if(p.pert()) {
            System.out.println("Invalid graph: not a DAG");
        } else {
            System.out.println("Number of critical vertices: " + p.numCritical());
            System.out.println("u\tEC\tLC\tSlack\tCritical");
            for(Vertex u: g) {
                System.out.println(u + "\t" + p.ec(u) + "\t" + p.lc(u) + "\t" + p.slack(u) + "\t" + p.critical(u));
            }
        }
    }
}