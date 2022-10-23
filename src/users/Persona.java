package users;

public class Persona {
	
	private String email;
	private String pw;
	private String actions = ""; 
	
	public Persona(String email, String pw) {
		this(email, pw, "login,checkbytitle,checkbyauthor");
	}
	
	public Persona(String email, String pw, String actions) {
		this.actions = "login,checkbytitle,checkbyisbn,checkbyauthor" + actions;
		this.pw = pw;
		this.email = email;
	}
	
	public boolean login(String pw) {
		return pw == this.pw;
	}
	
	public boolean letActions(String action) {
		String []all_actions = this.actions.split(",");
		for( String let_action : all_actions) {
			if(action == let_action) {
				return true;
			}
		}
		return false;
	}
}