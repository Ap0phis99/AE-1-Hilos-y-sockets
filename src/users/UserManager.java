package users;

import java.util.HashMap;
import java.util.Map;

public class UserManager {

	Map<String, Persona> personas;
	Persona current_user;

	public UserManager() {			
		this.personas = new HashMap<String, Persona>();
		this.loadDefUsers();
		this.current_user = null;
	}
	
	public boolean login(String email, String pw) {
//		while(true) {
//			System.out.println("Introduzca el email del usuario:");
//			Persona user = this.getUser(sc.nextLine());
//			if(user != null) {
//				System.out.println("Introduzca la pass del usuario:");
//				String user_pw = sc.nextLine();
//				if(user.login(user_pw)) {
//					this.current_user = user;
//					break;
//				}
//			}
//			System.out.println("Usuario o contraseÃ±a incorrectos, pulse cualquier tecla para volver a intentarlo o cero para salir:");
//			String op = sc.nextLine();
//			if(op == "0") {
//				break;
//			}			
//		}
		Persona user = this.getUser(email);
		if(user != null) {
			return user.login(pw);
		}else {
			return false;
		}		
		
	}
	
	private Persona getUser(String email) {
		if(this.personas.containsKey(email)) {
			return this.personas.get(email);
		}else {
			return null;
		}		
	}
	
	private void loadDefUsers() {
		this.personas.put("lib_admin@gmail.com", new Admin("lib_admin@gmail.com", "1234"));
		this.personas.put("lib_user@gmail.com", new User("lib_admin@gmail.com", "1234"));
	}

	public boolean validAction(String action) {
		return this.current_user.letActions(action);
	}

}