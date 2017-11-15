import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class WriteFile {
	private String path;
	
	public static void main(String[] args) {
		String file_name = "";
		WriteFile w = new WriteFile(file_name);
		try {
			w.addGraph(600);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public WriteFile(String file_path) {
		path = file_path;
	}
	
	public void addGraph(int size) throws IOException {
		FileWriter write = new FileWriter(path);
		PrintWriter print_line = new PrintWriter(write);
		for(int i = 0; i < size; i++) {
			char a = (char)((size/3)*Math.random() + 48);
			char b = (char)((size/3)*Math.random() + 48);
			if ((int) a == (int) b) b = (char)((int) a + 1);
			print_line.printf("%s" + "%n" , "" + a);
			print_line.printf("%s" + "%n" , "" + b);
		}
		print_line.close();
	}
}
