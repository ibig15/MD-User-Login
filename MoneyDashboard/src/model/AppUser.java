package model;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "AppUser")
public class AppUser {
	
	@Id
	private String email;
	private String password;
	private String name;
	private boolean loggedIn;
	private int incorrectPasswordCount;
	
	public AppUser(){}
	
	public AppUser(String email, String password, String name) {
		super();
		this.email = email;
		this.password = password;
		this.name = name;
		this.loggedIn = true;
		this.incorrectPasswordCount = 0;
	}
	
	public AppUser(String email, String password, String name, boolean active, int incorrectPasswordCount) {
		super();
		this.email = email;
		this.password = password;
		this.name = name;
		this.loggedIn = active;
		this.incorrectPasswordCount = incorrectPasswordCount;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean getLoggedIn() {
		return loggedIn;
	}
	
	public void setLoggedIn(boolean active) {
		this.loggedIn = active;
	}
	
	public int getIncorrectPasswordCount() {
		return incorrectPasswordCount;
	}
	
	public void setIncorrectPasswordCount(int incorrectPasswordCount) {
		this.incorrectPasswordCount = incorrectPasswordCount;
	}
	
	@Override
	public String toString() {
		return "AppUser [email=" + email + ", password=" + password + ", name=" + name + ", LoggedIn=" + loggedIn
				+ ", incorrectPasswordCount=" + incorrectPasswordCount + "]";
	}

}
