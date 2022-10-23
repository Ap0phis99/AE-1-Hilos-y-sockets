package users;

public class Admin extends Persona{

	public Admin(String email, String pw) {
		super(email, pw, "addbook,removebook");
	}
}