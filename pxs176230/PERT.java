package pxs176230;

import rbk.Graph.Vertex;
import rbk.Graph.Edge;
import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Factory;
import  rbk.Graph;
import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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

    int numCritical = 0;
    
    public PERT(Graph g) {
        super(g, new PERTVertex(null));
    }
    
    public boolean pert() {
    	numCritical = 0;
    	
    	DFS dfs = new DFS(g);
    	LinkedList<Vertex> finishedList = (LinkedList<Vertex>) dfs.topologicalOrder1();
    	
    	if(finishedList == null) {
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
    
    public int duration(Vertex u) {
    	return this.get(u).getDuration();
    }
    
    public int ec(Vertex u) {
        return this.get(u).getEc();
    }

    public int lc(Vertex u) {
        return this.get(u).getLc();
    }

    public int slack(Vertex u) {
        return this.get(u).getSlack();
    }

    public void setDuration(Vertex u, int d) {
    	this.get(u).setDuration(d);
    }
    
    public void setEc(Vertex u, int ec) {
    	this.get(u).setEc(ec);
    }

    public void setLc(Vertex u, int lc) {
    	this.get(u).setLc(lc);
    }
    
    public void setSlack(Vertex u, int slack) {
    	this.get(u).setSlack(slack);
    }
    
    public int criticalPath() {
    	//EC of t
        return ec(g.getVertex(g.size()));
    }

    public boolean critical(Vertex u) {
        return slack(u)==0;
    }

    public int numCritical() {
        return numCritical;
    }

    // setDuration(u, duration[u.getIndex()]);
    public static PERT pert(Graph g, int[] duration) {
    	
        PERT obj = new PERT(g);
        System.out.println(Arrays.toString(duration));

        //Add edges from s (1) to all vertices
        //Add edges from all vertices to t
        Vertex s = g.getVertex(1);
        Vertex t = g.getVertex(g.size());
        for(Vertex u : g) {
        	if(s != u)
        		g.addEdge(s.getIndex(), u.getIndex(), 1);
        	if(t != u)
        		g.addEdge(u.getIndex(), t.getIndex(), 1);
        	
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

    /*public static void main(String[] args) throws Exception {
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
    }*/
}