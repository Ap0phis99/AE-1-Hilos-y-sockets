package library_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import library.Book;
import library.Library;
import personas.Persona;
import personas.UserManager;

public class LibraryThread implements Runnable {
	private Thread thread;
	private static int queryNum = 0;
	private Socket socketToClient;
	public Library library = new Library();
	private UserManager user_manager = new UserManager();
	
	public LibraryThread(Socket socketToClient) {
		queryNum++;
		thread = new Thread(this, "query_"+queryNum);
		this.socketToClient = socketToClient;
		thread.start();
	}
	
	
	
	private Persona login(String user, String pw) {
		return this.user_manager.login(user, pw);
	}
	
	private String checkBooks(String type, String value) {
		Book checked = null;
		String response = "";
		if(type.equals("title"))
			checked = this.library.getBookByTitle(value);
		else if(type.equals("isbn"))
			checked = this.library.getBookByISBN(value);
		else if(type.equals("author")) {			
			for(Book book : this.library.getBooksByAuthor(value)) {
				 response += book.toString() + "\n";
			};
			 
		}
		if(checked  != null && response.equals("")) {
			return checked.toString();
		}else if(!response.equals("")) {
			return response;
		}else {
			return "libro no encontrado";
		}
	}
	
	private String addBook(String isbn, String title, String author,  int copies) {
		this.library.addBook(isbn, title, author, copies);
		return "libro añadido a la librería";
	}
	
	private String borrowBook(String type, String value) {
		Book borrowed = null;
		if(type.equals("borrowbyisbn")) {
			borrowed = this.library.borrowBookByISBN(value);
		}else if(type.equals("borrowbytitle")){
			borrowed = this.library.borrowBookByTitle(value);
		}
		if(borrowed != null) {
			return borrowed.getTitle()+" fue prestado";
		}else {
			return "libro no disponible";
		}
	}
	
	private String returnBook(String type, String value) {
		Book returned = null;
		if(type.equals("isbn")) {
			returned = this.library.backBookByISBN(value);;
		}else if(type.equals("title")){
			returned = this.library.backBookByTitle(value);
		}
		if(returned != null) {
			return returned.getTitle()+" fue devuelto correctamente";
		}else {
			return "error al devolver el libro";
		}		
	}
	
	private String removeBook(String type, String value) {
		return "";
	}
	
	private String processAction(String[] request_args) {
		String response = "";
		String action = request_args[0];
		String user = request_args[1];
		String pwd = request_args[2];
		Persona loged = this.login(user, pwd);
		
		if(loged == null) {
			return "";
		}
		
		if(action.equals("login")) {
			//solo los admins pueden añadir libros, como la clase Persona es genérica usamos esta diferencia para ver qué tipo de usuario es la instancia que estamos haciendo
			if(loged.letActions("add")) {
				response = "admin";
			}else {
				response = "user";
			}
		}else {
			String body = request_args[3];
			if(!loged.letActions(action)) {
				response = "acción no permitida para este usuario";
			}else if(action.equals("add")) {
				String[] book_attrs = body.split("|");
				String isbn = "";
				String title = "";
				String auth = "anom";
				int copies = 0;
				for(String attr : book_attrs) {
					if(attr.indexOf("isbn") != -1) {
						isbn = attr.replace("isbn:", "");
					}else if(attr.indexOf("title") != -1) {
						title = attr.replace("title:", "");
					}else if(attr.indexOf("auth") != -1) {
						auth = attr.replace("auth:", "");
					}else if(attr.indexOf("copies") != -1) {
						copies = Integer.parseInt(attr.replace("copies:", ""));
					}
				}
				if(!isbn.equals("") && !title.equals("")) {
					response = this.addBook(isbn, title, auth, copies);
				}else {
					response = "no se ha podido añadir el libro al catalogo";
				}
			}else {
				String[] query = body.split(":");
				if(action.equals("remove")) {				
					response = this.removeBook(query[0], query[1]);							
				}else if(action.equals("check")) {
					response = this.checkBooks(query[0], query[1]);
				}else if(action.equals("borrow")) {
					response = this.borrowBook(query[0], query[1]);							
				}else if(action.equals("returnbook")){
					response = this.returnBook(query[0], query[1]);								
				}			
			} 
		}		
		return response;
	}
	@Override
	public void run() {
		System.out.println("SERVIDOR: Estableciendo comunicacion con " + thread.getName());
		PrintStream output = null;
		InputStreamReader input = null;
		BufferedReader buffer_input = null;
		
		try {
			output = new PrintStream(this.socketToClient.getOutputStream());
		
			input  = new InputStreamReader(this.socketToClient.getInputStream());
			buffer_input = new BufferedReader(input);
			
			boolean is_the_end = false; // este flag se pondrá a true cuando la petición sea "exit" para acabar la ejecución
			String response = "";
			
			do {
				String request_body = buffer_input.readLine();
				System.out.println("SERVIDOR: resolviendo " + request_body);
				if(request_body == "exit") {
					is_the_end = true;
					response = "SERVIDOR BIBLIOTECA: SESIÓN CERRADA";
				}else {
					response = this.processAction(request_body.split("#"));
					output.println(response);					
				}
				System.out.println("SERVIDOR: peticion resuelta... ");
				System.out.println(response);
				output.println(response);	
				
			}while(is_the_end);
			
			this.socketToClient.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}