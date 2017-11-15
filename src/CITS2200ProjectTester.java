import java.io.*;
import java.util.Iterator;
import java.util.Stack;
import java.util.ArrayList;


public class CITS2200ProjectTester {
	public static void loadGraph(CITS2200Project project, String path) {
		// The graph is in the following format:
		// Every pair of consecutive lines represent a directed edge.
		// The edge goes from the URL in the first line to the URL in the second line.
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			while (reader.ready()) {
				String from = reader.readLine();
				String to = reader.readLine();
				//System.out.println("Adding edge from " + from + " to " + to);
				project.addEdge(from, to);
			}
			reader.close();
		} catch (Exception e) {
			System.out.println("There was a problem:");
			System.out.println(e.toString());
		}
	}

	public static void main(String[] args) {
		// Change this to be the path to the graph file.
		String pathToGraphFile = "D:\\Documents\\stuff\\UWA\\2nd Year\\Sem 1\\CITS2200 - Data Structures and Algorithms\\2200Project\\lib\\test_graphs\\1024n16384e"
				+ ".txt";
		// Create an instance of your implementation.
		WikiGraphAnalyzer proj = new WikiGraphAnalyzer();
		// Load the graph into the project.
		loadGraph(proj, pathToGraphFile);
		for(int i = 0; i < proj.getGraph().size(); i++) {
			for(int j = 0; j < proj.getGraph().get(i).size(); j++) {
				System.out.print(proj.getGraph().get(i).get(j) + " ");
			}
			System.out.print("\n");
		}
		System.out.print("\n");

		System.out.print("\n");
		//System.out.print(proj.getShortestPath("/wiki/Out-of-kilter_algorithm", "/wiki/Push%E2%80%93relabel_maximum_flow_algorithm"));
		printArray(proj.getCenters());
		System.out.println(proj.getGraph().size());
		System.out.println(proj.getCenters().length);
	}
	
	private static void printArrayList(ArrayList<ArrayList<String>> a) {
		for(int i = 0; i < a.size(); i++) {
			for(int j = 0; j < a.get(i).size(); j++) {
				System.out.print(a.get(i).get(j) + " ");
			}
			System.out.print("\n");
		}
	}
	
	
	private static void printArray(String[] a) {
		for(int i = 0; i < a.length; i++) {
			System.out.print(a[i] + " ");
		}
		System.out.print("\n");
	}
	
	private static void print2DArray(String[][] a) {
		for(int i = 0; i < a.length; i++) {
			for(int j = 0; j < a.length; j++) {
				System.out.print(a[i][j] + " ");
			}
			System.out.print("\n");
		}
		System.out.print("\n");
	}
	
	private static void printStack(Stack s) {
		while(!s.isEmpty()) {
			System.out.print(s.pop() + " ");
		}
		System.out.print("\n");
	}
	
}