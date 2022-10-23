import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import users.UserManager;

public class LibraryServer {
	public Library library = new Library();
	private UserManager user_manager = new UserManager();
	
	public static final int PORT = 2017;
	
	public LibraryServer() {	
	}
	
	public boolean login(String user, String pw) {
		return this.user_manager.login(user, pw);
	}
	
	public boolean letAction(String action) {
		return this.user_manager.validAction(action);
	}
	
	public String checkBooks(String type, String value) {
		String response = "";
		if(type == "checkbytitle")
			response = this.library.getBookByTitle(value).toString();
		else if(type == "checkbyisbn")
			response = this.library.getBookByISBN(value).toString();
		else if(type == "checkbyauthor") {
			 for(Book book : this.library.getBooksByAuthor(value)) {
				 response += book.toString() + "\n";
			 };
		}
		return response;
	}
	
	public static void main(String[] args) throws InterruptedException {
		System.out.println("      Library      ");
		LibraryServer lib_sev = new LibraryServer();	
		InputStreamReader input = null;
		PrintStream output = null;		
		Socket socketToClient = null;
		
		InetSocketAddress adress = new InetSocketAddress(PORT);

		try (ServerSocket serverSocket = new ServerSocket()){			
			
			serverSocket.bind(adress);
			
			int requests = 0;
			
			while(true){		
				
				System.out.println("SERVIDOR: Esperando peticion por el puerto " + PORT);
				
				socketToClient = serverSocket.accept();
				System.out.println("SERVIDOR: peticion numero " + ++requests + " recibida");
				
				input = new InputStreamReader(socketToClient.getInputStream());
				BufferedReader bf = new BufferedReader(input);
											
				String request_body = bf.readLine();//3-4
				
				System.out.println("SERVIDOR: Me ha llegado del cliente: " + request_body);
				
				output = new PrintStream(socketToClient.getOutputStream());
				
				if(request_body == "exit") {
					output.println("adios!!!");	
					socketToClient.close();
				}else {
					String[] request_body_split = request_body.split(":");
					String action = request_body_split[0];
					String value = request_body_split[1];
					String response = "not valid action";
					
					if(action == "login") {
						if(lib_sev.login(request_body_split[1], request_body_split[2])) {
							//loged correctly
						}else {
							
						}
					}else if(lib_sev.letAction(action)) {
						if(action == "addbook") {
							
						}else if(action == "removebook") {
												
						}else if("checkbyisbn,checkbytitle,checkbyauthor".indexOf(action) != -1 ) {
							lib_sev.checkBooks(action, value);	
						}
					}else {
						//accion no valida para el usuario actual
					}
					
					
					output.println(response);					
				}		
			}
		} catch (IOException e) {
			System.err.println("SERVIDOR: Error de entrada/salida");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("SERVIDOR: Error -> " + e);
			e.printStackTrace();
		}
	}//FIN DEL PROGRAMA

	
}