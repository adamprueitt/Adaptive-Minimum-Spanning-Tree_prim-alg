/*************************************************************************
  *  Compilation:  javac AdaptiveMST.java 
  *           or   javac-algs4  AdaptiveMST.java
  *  Execution:    java AdaptiveMST <filename>
  *          or    java-algs4  AdaptiveMST <filename>
  *  Dependencies: StdIn from the Sedgewick and Wayne stdlib library
  *                EdgeWeightedGraph 
  *
  *  uses code by  Robert Sedgewick and Kevin Wayne
  *
  * @author:  Dan Dougherty, for CS 2223 WPI,  November 2014 
  *
  * For project 3 your job is to write the method treeChanges, along with whatever 
  * auxilliary methods you'd like to write.
  *
  * The main procedure should be ready to use, you needn't worry about
  * changing that code.
  *
  *************************************************************************/

import java.io.*;
import java.util.*;

public class AdaptiveMST {
    
    
    /*
     REQUIRES: T is a minimum spaning tree for G, e is an edge over the vertices of G,
     interpreted as a modification of G.
     other assumption are in force (see program spec)
     EFFECTS: writes to standard output the changes in T due to the modifiation e
     */
    
    // STUB VERSION:  right now this doesn't do anything but echo the input.
    //  Get rid of these printlns !
    public static void treeChanges(EdgeWeightedGraph G, EdgeWeightedGraph T, Edge e) {
        
        PrimMST tPrime = new PrimMST(G);
        
//        for (i=0; i<G.v; i++){
//            while (G.adj[i]<adj.N){
//                testnode = G.adj[i]
//                    if (e.v == testnode.v && e.w == testnode.w){
//                    inG = true;
//                    break;
//                }
        
        int v = e.either();
        int w = e.other(v);
        Bag<Edge> b = new Bag();
        Edge old;
        
        try {
            b = (Bag<Edge>)G.adj(v);
        } catch (IndexOutOfBoundsException exc){
            System.out.println("Vertex not in given graph");
            return;
        } finally {
            old = b.findInBag(e);
        }
        
        if(old != null){ //case 5
            T.addEdge(e);
            Bag<Edge> bagToTest = (Bag<Edge>)T.edges();
            Edge result = bagToTest.findLargest();
            if(result.compareTo(e) == 0){
                System.out.println("No change in tree.");
            }
            else{
                v = result.either();
                w = e.either();
                System.out.println("The result is to remove edge {" + v + "," + result.other(v) + "} from the MST and add edge {" + w + "," + e.other(w) + "}.");
            }
            return;
        }
        
        Bag<Edge> t;
        try {
            t = (Bag<Edge>)T.adj(v);
        } catch (IndexOutOfBoundsException exc){
            System.out.println("Vertex not in given graph");
            return;
        } finally {}
        
        Edge inT = t.findInBag(e);
        boolean eInT;
        
        if (inT == null){
            eInT = false;
        }
        else {
            eInT = true;
        }
        
        if(e.compareTo(old) == -1){ // decreasing weight
            if(eInT){
                System.out.println("weight of edge {" + v + "," + w + "} decreased to " + e.weight() + ": no change in tree.");
            }
            else{
                T.addEdge(e);
                Bag<Edge> bagToTest = (Bag<Edge>)T.edges();
                Edge result = bagToTest.findLargest();
                if(result.compareTo(e) == 0){
                    System.out.println("No change in tree.");
                }
                else{
                    v = result.either();
                    w = e.either();
                    System.out.println("The result is to remove edge {" + v + "," + result.other(v) + "} from the MST and add edge {" + w + "," + e.other(w) + "}.");
                }
            }
        }
        else if (e.compareTo(old) ==1){ //increasing weight
            if(eInT){
                //case 4
                System.out.println("This is case 4, which we still need to write.");
            }
            else{
                System.out.println("No change in the tree.");
            }
        }
        else{
            System.out.println("Edge weight not changed");
        }
    }   
    
    /*
     REQUIRES: a string consisting of 3 tokens representing two ints and a double.
     RETURNS:  a weighted edge built from this data.
     EFFECTS: in addition to number format exceptions, can throw an
     IllegalArgumentException if input is not 3 tokens,
     */
    public static Edge makeEdge(String inline) 
        throws IllegalArgumentException, NumberFormatException
    {
        // whitespace will separate strings; have to trim leading/trailing whitespace first
        String delims = "[ ]+";
        String[] input_strings = inline.trim().split(delims);
        
        if (input_strings.length != 3) 
            throw new IllegalArgumentException("Expect 3 numbers per input line");
        int x = Integer.parseInt(input_strings[0]);
        int y = Integer.parseInt(input_strings[1]);
        double weight = Double.parseDouble(input_strings[2]);
        return new Edge(x,y, weight);
    }
    
    
    public static void main(String[] args) throws IOException, IllegalArgumentException {
        
        // Read original graph from a file
        // Here is where we use Sedgewick and Wayne stdlib library
        In graph_in = new In(args[0]);
        EdgeWeightedGraph G = new EdgeWeightedGraph(graph_in);
        
        // Now compute an MST for G
        // Here we just make T something dumb
        EdgeWeightedGraph T = new EdgeWeightedGraph(17);
        
        // Read the user input, line by line
        // This is all stock Java
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String prompt = "Please enter a weighted edge, or a blank line to exit";
        String input_line;
        
        System.out.println(prompt);
        while ( (input_line = in.readLine()) !=null && input_line.length() != 0 ) {
            
            try{
                Edge current_edge = makeEdge(input_line);
                treeChanges(G, T, current_edge);
            }
            catch (IllegalArgumentException e) {
                System.out.println(e);
                System.out.println("Try again...");
            }
            System.out.println(prompt);
        }
        System.out.println("bye");
    }
}