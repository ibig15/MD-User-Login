package test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import controller.AppUserController;
import model.AppUser;
import model.UserMessage;
import model.UserMessage.message;

public class UserAppTests {
	public AppUserController tester = new AppUserController();
	public UserMessage um = new UserMessage();
	
	@Test
	public void registerUserWithInvalidEmailShouldReturnInvalidEmailError() {
		System.out.println("Starting Test 1");
		assertTrue((message.InvalidEmail).equals(tester.registerUser("johndoylegmail.com", "pass123", "john doyle")));
		System.out.println("Finished Test 1");
	}
	
	@Test
	public void registerUserThatExistsShouldReturnExistingEmailError() {
		System.out.println("Starting Test 2");
		assertTrue((message.ExistingEmail).equals(tester.registerUser("abcd@gmail.com", "Tester99", "ab cd")));
		System.out.println("Finished Test 2");
	}
	
	@Test
	public void registerUserSuccessfullyShouldReturnSuccesfulRegistrationMessage() {
		System.out.println("Starting Test 3");
		String email = "mary@gmail.com";
		String pass = "Tester99";
		String name = "Mary N";
		tester.deleteUser("mary@gmail.com");	// delete the user first if it already exists
		assertTrue((message.SuccesfulRegistration).equals(tester.registerUser(email, pass, name)));
		List<AppUser> existingUser = tester.findUser(email);
		
		assertTrue(existingUser.size() == 1);
		AppUser user = (AppUser) existingUser.get(0);
	    assertTrue(user.getEmail().equals(email));
	    assertTrue(user.getPassword().equals(tester.encryptPassword(pass)));
	    assertTrue(user.getName().equals(name));
	    assertTrue(user.getLoggedIn());
	    assertTrue(user.getIncorrectPasswordCount() == 0);
	    
		System.out.println("Finished Test 3");
	}
	
	@Test
	public void logInUserThatExistsShouldReturnUserNotRegisteredError() {
		System.out.println("Starting Test 4");
		assertTrue((message.UserNotRegistered).equals(tester.logInUser("dummny_nonexistent_email@gmail.com", "Tester99")));
		System.out.println("Finished Test 4");
	}
	
	@Test
	public void logInUserThatExistsShouldReturnIncorrectPasswordError() {
		System.out.println("Starting Test 5");
		String email = "email_no5@gmail.com";
		String pass = "Pass123";
		String wrongPass = "Pass12";
		tester.registerUser(email, pass, "");
		assertTrue((message.IncorrectPassword).equals(tester.logInUser(email, wrongPass)));
		tester.deleteUser(email);
		System.out.println("Finished Test 5");
	}
	
	@Test
	public void logInUserThatExistsShouldReturnBlockedUserError() {
		System.out.println("Starting Test 6");
		String email = "email_no6@yahoo.com";
		String pass = "Pass123";
		String wrongPass = "Pass12";
		tester.registerUser(email, pass, "");
		for (int i = 0; i<5; i++){
			tester.logInUser(email, wrongPass);
		}
		assertTrue((message.BlockedUser).equals(tester.logInUser(email, pass)));
		tester.deleteUser(email);
		System.out.println("Finished Test 6");
	}
	
	@Test
	public void logInUserSuccessfullyShouldReturnSuccessfulLoginMessage() {
		System.out.println("Starting Test 7");
		assertTrue((message.SuccessfulLogin).equals(tester.logInUser("abcd@gmail.com", "Tester99")));
		System.out.println("Finished Test 7");
	}
	
	@Test
	public void logOutUserNotRegisteredShouldReturnUserNotRegisteredError() {
		System.out.println("Starting Test 8");
		assertTrue((message.UserNotRegistered).equals(tester.logOutUser("not.registered_user@email.com")));
		System.out.println("Finished Test 8");
	}
	
	@Test
	public void logOutUserSuccessfullyShouldReturnSuccessfulLoginMessage() {
		System.out.println("Starting Test 9");
		assertTrue((message.SuccessfulLogout).equals(tester.logOutUser("abcd@gmail.com")));
		System.out.println("Finished Test 9");
	}
	
	@Test
	public void deleteUserNotRegisteredShouldReturnUserNotRegisteredError() {
		System.out.println("Starting Test 10");
		String email = "email_no10@outlook.com";
		assertTrue((message.UserNotRegistered).equals(tester.deleteUser(email)));
		System.out.println("Finished Test 10");
	}

	@Test
	public void deleteUserSuccessfullyShouldReturnUserDeletedMessage() {
		System.out.println("Starting Test 11");
		String email = "email_no11@outlook.com";
		tester.registerUser(email, "123abcd", "george");
		assertTrue((message.UserDeleted).equals(tester.deleteUser(email)));
		System.out.println("Finished Test 11");
	}
	
}
