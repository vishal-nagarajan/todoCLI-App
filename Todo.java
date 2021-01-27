import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;  
public class Todo {
	static void createFile(String FileName) {
		try {
		      File myObj = new File(FileName+".txt");
		      myObj.createNewFile();
			}
		catch (IOException e) {
		      e.printStackTrace();
		    }
	}
	static void helpCmd() {
		String s = "Usage :-\n"
				+ "$ ./todo add \"todo item\"  # Add a new todo\n"
				+ "$ ./todo ls               # Show remaining todos\n"
				+ "$ ./todo del NUMBER       # Delete a todo\n"
				+ "$ ./todo done NUMBER      # Complete a todo\n"
				+ "$ ./todo help             # Show usage\n"
				+ "$ ./todo report           # Statistics";
		System.out.print(s);
	}
	static void addTodo(String todo) {
		try {
			PrintStream out  = new PrintStream(new FileOutputStream("todo.txt",true));
			byte[] buf = todo.getBytes();
			out.write(buf);
			out.write("\n".getBytes());
			out.close();
			System.out.format("Added todo: \"%s\"",todo);
		}catch (IOException e) {
	        System.out.println("An error occurred.");
	        e.printStackTrace();
	      }
	}
	static ArrayList<String> getTodoasList(){
		ArrayList<String> todos= new ArrayList<String>();
		try {
		      File myObj = new File("todo.txt");
		      Scanner myReader = new Scanner(myObj);
		       
		      while (myReader.hasNextLine()) {
		        String data = myReader.nextLine();
		        todos.add(data);
		      }
		      myReader.close(); 
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		return todos;
		
	}
	static void listTodo() {
		 	  ArrayList<String> todos = getTodoasList();
		      if(todos.isEmpty()) {
		    	  System.out.print("There are no pending todos!");
		      }else {
		      for(int i=todos.size()-1;i>=0;i--) {
		    	  System.out.format("[%d] %s\n",i+1,todos.get(i));
		      }
		      }
	}
	static void removeLine(ArrayList<String> todos,String Index) {
		
		try{
			//delete specified index from list
			todos.remove(Integer.parseInt(Index)-1);
	      //load the list into the file.
	      FileWriter Writer = new FileWriter("todo.txt");
	      for(String str : todos) {
			Writer.write(str+System.lineSeparator());
	      }
			Writer.close();
			}
			catch(IOException e) {
				System.out.println("An error occurred.");
			    e.printStackTrace();
			}
	}
	static void deleteTodo(String delIndex) {
		try {
			//read all lines into a list
			ArrayList<String> todos=  getTodoasList();
			removeLine(todos,delIndex);
			System.out.format("Deleted todo #%s",delIndex);
		}
		catch(IndexOutOfBoundsException e) {
			//given index is not available
			System.out.format("Error: todo #%s does not exist. Nothing deleted.",delIndex);
		}
	}
	static String Date() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
		LocalDateTime now = LocalDateTime.now();
		return dtf.format(now).toString();
	}
	static void done(String doneIndex) {
		try {
			//read all lines into a list
			ArrayList<String> todos= getTodoasList();
		      //add the specified index to done.txt
			  createFile("done");
		      PrintStream out  = new PrintStream(new FileOutputStream("done.txt",true));
		      byte[] buf = ("x "+Date()+" "+todos.get(Integer.parseInt(doneIndex)-1)).getBytes();
		      out.write(buf);
		      out.write("\n".getBytes());
		      out.close();
		      removeLine(todos,doneIndex);
			System.out.format("Marked todo #%s as done.",doneIndex);
		}
		catch(IndexOutOfBoundsException e) {
			//given index is not available
			System.out.format("Error: todo #%s does not exist.",doneIndex);
		}
		catch(IOException e) {
			System.out.println("An error occurred.");
		    e.printStackTrace();
		}
	}
	static void reportTodoDone(){
		try { 
			File myObj = new File("todo.txt");
		    Scanner myReader = new Scanner(myObj);
		    int todoSize =0;
		    int doneSize = 0;
		    while (myReader.hasNextLine()) {
		    	myReader.nextLine();
		    	todoSize++;			    	  
		      }
		      myReader.close();
		      myObj = new File("done.txt");
		      myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) {
			    	myReader.nextLine();
			    	doneSize++;			    	  
			      }
		      
		      System.out.format("%s Pending : %s Completed : %s",Date(),todoSize,doneSize);
		}catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}
	public static void main(String args[]){		
		createFile("todo");
		if(args.length==0 || args[0].equals("help")) {
			helpCmd();
		}
		else if(args[0].equals("add")) {
			if(args.length==1) {
				System.out.print("Error: Missing todo string. Nothing added!");
			}else {
				addTodo(args[1]);
			}
		}
		else if(args[0].equals("ls")) {
			listTodo();
		}
		else if(args[0].equals("del")) {
			if(args.length==1) {
				System.out.print("Error: Missing NUMBER for deleting todo.");
			}else {
				deleteTodo(args[1]);	
			}
		}
		else if(args[0].equals("done")) {
			if(args.length==1) {
				System.out.print("Error: Missing NUMBER for marking todo as done.");
			}else {
				done(args[1]);	
			}			
		}
		else if(args[0].equals("report")) {
			reportTodoDone();
		}	
	}
}
