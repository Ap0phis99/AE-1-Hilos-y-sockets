package users;

public class User extends Persona{

	public User (String email, String pw) {
		super(email, pw, "borrowbytitle,borrowbyisbn,backbook");
	}
}