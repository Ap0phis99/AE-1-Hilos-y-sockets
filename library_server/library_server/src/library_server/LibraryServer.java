package library_server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class LibraryServer {
	
	public static final int PORT = 2018;

	public static void main(String[] args) {
		System.out.println("SERVIDOR: biblioteca inicializada");
		
		try (ServerSocket server = new ServerSocket()){			
			InetSocketAddress adress = new InetSocketAddress(PORT);
			server.bind(adress);

			System.out.println("SERVIDOR: Esperando peticion por el puerto " + PORT);
			
			while (true) {
				//creamos el socket y se lo pasamos al objeto tipo librarythread para que ya se encarge ella de gestionarlo
				Socket socketToCliente = server.accept();
				System.out.println("SERVIDOR: petición recibida");
				new LibraryThread(socketToCliente);
			}			
		} catch (IOException e) {
			System.err.println("SERVIDOR: Error de entrada/salida");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("SERVIDOR: Error");
			e.printStackTrace();
		}
	}

}
