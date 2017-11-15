import java.util.*;
/**
 *A class that analyzes subsets of the Wikipedia graph and finds features of it.
 * It implements the CITS2200Project interface.
 * @author Robin Markwitz & Frederick Subere Albawy
 *
 */

public class WikiGraphAnalyzer implements CITS2200Project {

	/**
	 * Instance variables of the class.
	 * graph is an adjacency list that stores the structure of the graph
	 * getIndex is a HashMap that, given a particular page, returns the index at which it is present in graph
	 */
	private ArrayList<ArrayList<String>> graph;
	private HashMap <String, Integer> getIndex;
	
	public WikiGraphAnalyzer() {
		graph = new ArrayList<ArrayList<String>>();
		getIndex = new HashMap <String, Integer>();
	}
	
	public ArrayList<ArrayList<String>> getGraph() {
		return graph;
	}
	
	/**
	 * This class holds a String and an int, and signifies the following:
	 * -name: is the name of the vertex.
	 * -weight: is the 'weight' that it has from a given vertex.
	 * Needed for the correct implementation of a Priority Queue with a Comparator.
	 * @author Robin Markwitz & Frederick Subere Albawy
	 *
	 */
	private class Node {
		
		private String name;
		private int weight;
		
		public Node(String n, int w) {
			name = n;
			weight = w;
		}
		
		/**
		 * Getter method.
		 * @return name
		 */
		public String getName() {
			return name;
		}
		/**
		 * Getter method.
		 * @return weight
		 */
		public int getWeight() {
			return weight;
		}
	}
	/**
	 * Comparator for Node objects.
	 * @author Robin Markwitz & Frederick Subere Albawy
	 *
	 */
	private class NodeComparator implements Comparator<Node> {

		/**
		 * Determines which Node has a lower value compared to another. Useful for ordering.
		 */
		public int compare(Node x, Node y) {
			if(x.getWeight() > y.getWeight()) return 1;
			else if (x.getWeight() == y.getWeight()) return 0;
			else return -1;
		}
		
	}
	
	/**
	 * This method adds edges to the graph field, which is implemented as an adjacency list.
	 * It also uses a HashMap to store the indices of each node.
	 * @param urlFrom - the URL of a Wikipedia page
	 * @param urlTo - the URL of another Wikipedia page
	 * @return void
	 */
	public void addEdge(String urlFrom, String urlTo) {
		if(getIndex.containsKey(urlFrom)){
			int index = getIndex.get(urlFrom);
			graph.get(index).add(urlTo);
		}
		else{
			ArrayList<String> temp = new ArrayList<String>();
			temp.add(urlFrom);
			temp.add(urlTo);
			getIndex.put(urlFrom, graph.size());
			graph.add(temp);
		}
		if(!getIndex.containsKey(urlTo)){
			ArrayList<String> temp = new ArrayList<String>();
			temp.add(urlTo);
			getIndex.put(urlTo, graph.size());
			graph.add(temp);
		}
	}

	/**
	 * This method finds the shortest path from any one page to any other page.
	 * The algorithm used is Dijkstra's algorithm.
	 * Uses a priority queue of Node objects.
	 * Taken from: https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
	 * Time complexity is O(|E|lg|V|).
	 * @param urlFrom - a Wikipedia page
	 * @param urlTo - another Wikipedia page
	 * @return the shortest path from urlFrom to urlTo
	 */
	public int getShortestPath(String urlFrom, String urlTo) {
		long startTime = System.nanoTime();
		int [] currentPaths = getShortestPathArray(urlFrom);
		long endTime = System.nanoTime();
		double time = (endTime-startTime)/1000000000.0;
		System.out.println("Took "+(time) + " secs"); 
		return currentPaths[getIndex.get(urlTo)];
	}
	
	/**
	 * This algorithm finds the shortest path from a given page to all other pages in the subset.
	 * Private helper method for getCenters() and getShortestPath(String urlFrom, String urlTo)
	 * The algorithm used is Dijkstra's algorithm.
	 * Taken from: https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
	 * Time complexity is O(|E|lg|V|).
	 * @param urlFrom - a Wikipedia page
	 * @return an array with the shortest paths from urlFrom to all other pages (and 0 to itself)
	 */
	public int[] getShortestPathArray(String urlFrom) {
		int vertices = graph.size();
		int[] currentPaths = new int[vertices];
		Comparator<Node> comparator = new NodeComparator();
		PriorityQueue<Node> q = new PriorityQueue<Node>(10, comparator);
		for(int i = 0; i < vertices; i++) {
			if(getIndex.get(urlFrom) != i)
			currentPaths[i] = Integer.MAX_VALUE;
		}
		int alt = 0;
		q.add(new Node(urlFrom,0));
		while(!q.isEmpty()) {
			String w = q.poll().getName();
			for(String s: graph.get(getIndex.get(w))) {
				alt = currentPaths[getIndex.get(w)] + 1;
				if (alt < currentPaths[getIndex.get(s)]) {
					currentPaths[getIndex.get(s)] = alt;
					q.add(new Node(s,alt));
				}
			}
		}
		return currentPaths;
	}
	/**
	 * A private helper method that returns the index in the list of any given page.
	 * @param s - a string
	 * @return the index
	 */
	private int getIndex(String s) {
		int a = -1;
		for(int i = 0; i < graph.size(); i++) {
			if(graph.get(i).get(0).equals(s)) {
				a = i;
				break;
			}
		}
		return a;
	}
	
	
	/**
	 * A method that returns a list of centers of a graph.
	 * Uses eccentricities and radii in conjunction with Dijkstra's algorithm.
	 * Inspired by codeforces.com/blog/entry/17974
	 * The time complexity is O(|E||V|lg|V|) //PLEASE CHECK THIS
	 * @return an array of strings denoting the center(s) of the graph.
	 * NOTE that if all nodes are centers, the code will return ALL of the nodes, not none of them.
	 */
	public String[] getCenters() {
		long startTime = System.nanoTime();
		int[][] floyd = floyd();
		int[] e = new int[graph.size()];
		int radius = Integer.MAX_VALUE;
		ArrayList<String> centerList = new ArrayList<String>();
		for(int i = 0; i < graph.size(); i++) {
			for(int j = 0; j < graph.size(); j++) {
				e[i] = Integer.max(e[i],floyd[i][j]);
			}
		}
		for(int i = 0; i < graph.size(); i++) {
			radius = Integer.min(radius, e[i]);
			
		}
		for(int i = 0; i < graph.size(); i++) {
			if(e[i] == radius) centerList.add(graph.get(i).get(0));
		}
		String[] centers = new String[centerList.size()];
		centerList.toArray(centers);
		long endTime = System.nanoTime();
		double time = (endTime-startTime)/1000000000.0;
		System.out.println("Took "+(time) + " secs"); 
		return centers;
	}
	/**
	 * Simulates the Floyd-Warshall algorithm by using Dijkstra's algorithm.
	 * Private helper method for getCenters().
	 * Builds up a 2D array of all-pair shortest paths.
	 * @return a 2D array of all-pair shortest paths
	 */
	
	private int[][] floyd() {
		int[][] shortestPaths = new int[graph.size()][graph.size()];
		for(int i = 0; i < graph.size(); i++) {
			shortestPaths[i] = getShortestPathArray(graph.get(i).get(0));
		}
		return shortestPaths;
	}

	/**
	 * A method that finds a list of all strongly connected components of a graph.
	 * The algorithm used is Kosaraju's algorithm.
	 * The time complexity is O(|E| + |V|).
	 * Taken from https://sites.google.com/site/indy256/algo/strongly_connected_components
	 * Taken from https://www.hackerearth.com/practice/algorithms/graphs/strongly-connected-components/tutorial
	 * Inspired by https://web.stanford.edu/class/archive/cs/cs161/cs161.1138/lectures/04/Small04.pdf
	 * @return Double string array of one strongly connected component per row 
	 */
	public String[][] getStronglyConnectedComponents() {
		long startTime = System.nanoTime();
		String[][] components = new String[graph.size()][graph.size()];
		boolean visited[] = new boolean[graph.size()];
		Stack<String> s = new Stack<String>();
		boolean added[] = new boolean[graph.size()];
		DFS(visited,s,graph.get(0).get(0), graph);
		for(int j = 0; j < visited.length; j++) {
			if(visited[j] == false) {
				DFS(visited,s,graph.get(j).get(0),graph);
			}
		}
		Arrays.fill(visited,false);
		ArrayList<ArrayList<String>> rev = makeReverse();
		int i = 0;
		while(!s.isEmpty()) {
			String temp = s.pop();
			String[] compRow = new String[visited.length];
			if(visited[getIndex.get(temp)]) continue;
			else {
				DFS(visited,s,temp,rev);
				for(int j = 0; j < visited.length; j++) {
					if(visited[j] && !added[j]) {
						compRow[j] = graph.get(j).get(0);
						added[j] = true;
					}
				}
			}
			String[] toAdd = compRow;
			components[i] = toAdd;
			i++;
		}
		long endTime = System.nanoTime();
		double time = (endTime-startTime)/1000000000.0;
		System.out.println("Took "+(time) + " secs"); 
		return components;
	}
	/**
	 * A private helper method that finds the list transpose of the instance variable graph.
	 * Assists in getStronglyConnectedComponents().
	 * @return the transposed list
	 */
	private ArrayList<ArrayList<String>> makeReverse() {
		ArrayList<ArrayList<String>> reverse = new ArrayList<ArrayList<String>>();
		ArrayList<String> holder = new ArrayList<String>();
		int check = 0;
		for(int i = 0; i < graph.size(); i++) {
			String temp = graph.get(i).get(0);
			for(ArrayList<String> a: graph)  {
				for(int j = 1; j < a.size(); j++) {
					if(a.get(j).equals(temp)) {
						if (check == 0) holder.add(temp);
						holder.add(a.get(0));
						check++;
					}
				}
			}
			ArrayList<String> toAdd = new ArrayList<String>(holder); //you need this because otherwise it clears the whole arraylist when you add
			reverse.add(i,toAdd);
			holder.clear();
			check = 0;
		}
		return reverse;
	}
	/**
	 * An implementation of the recursive depth-first search graph algorithm.
	 * @param visited - a boolean array which designates the state of a given page
	 * @param stack - a stack
	 * @param s - a string
	 * @param graphs - a graph
	 */
	
	private void DFS(boolean[] visited, Stack<String> stack, String s, ArrayList<ArrayList<String>> graphs) {
		visited[getIndex(s)] = true;
		for(String l: graphs.get(getIndex(s))) {
			if(!visited[getIndex(l)]) {
				DFS(visited,stack,l, graphs);
			}
		}
		stack.push(s);
	}
	
	
	/**
	 * Finds the Hamiltonian path of a graph, if one exists.
	 * The algorithm used is the Held-Karp algorithm. Complexity is O(2^n n^2).
	 * Taken from https://www.hackerearth.com/practice/algorithms/graphs/hamiltonian-path/tutorial/.
	 * @return a String array which details the exact path taken.
	 */
	
	public String[] getHamiltonianPath() {
		long startTime = System.nanoTime();
		int n = graph.size();
		boolean[][] dp = new boolean[n][1<<n];
		boolean[][] adj = makeAdjacencyMatrix();
		String[] path = new String[n];
		ArrayList<String> currentPath = new ArrayList<String>();
		for(int i = 0; i < n; i++) {
			dp[i][1<<i] = true;
		}
		for(int i = 0; i <(1<<n); i++) {
			for(int j = 0; j < n; j++) {
				if((i & (1<<j)) != 0) {
					for(int k = 0; k < n; k++) {
						if((j != k) && ((i & (1<<k)) != 0) && (adj[k][j]) && dp[k][i^(1<<j)]) {
							dp[j][i] = true;
							break;
						}
					}
				}
			}
		}
		boolean flag = false;
		for(int i = 0; i < n; i++) {
			if(dp[i][(1<<n)-1]) {
				flag = true;
				break;
			}
		}
		if(flag) {
			int i = (1<<n)-1;
			while(i > 0) {
				for(int k = 0; k < n; k++) {
					if(dp[k][i] == true && (i & (1<<k)) != 0) {
						i = (i)^(1<<k);
						currentPath.add(graph.get(k).get(0));
						break;
					}
				}
					
			}
			for(int l = 0; l < currentPath.size(); l++) {
				path[currentPath.size()-(l+1)] = currentPath.get(l);
			}
			long endTime = System.nanoTime();
			double time = (endTime-startTime)/1000000000.0;
			System.out.println("Took "+(time) + " secs"); 
			return path;
		}
		long endTime = System.nanoTime();
		double time = (endTime-startTime)/1000000000.0;
		System.out.println("Took "+(time) + " secs"); 
		return path;
	}
	/**
	 * Creates an adjacency matrix for the adjacency list graph.
	 * Private helper method for getHamiltonianPath().
	 * @return a boolean array which represents if there is an edge between two particular nodes or not
	 */
	private boolean[][] makeAdjacencyMatrix() {
		boolean[][] adj = new boolean[graph.size()][graph.size()];
		for(int i = 0; i < graph.size(); i++) {
			for(String s: graph.get(i)) {
				if(getIndex.get(s) != i)
				adj[i][getIndex.get(s)] = true;
			}
		}
		return adj;
	}
	
}