package controller;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import model.*;
import model.UserMessage.message;


public class AppUserController {
	private final String PERSISTENCE_UNIT_NAME = "AppUsers";
	
	private EntityManagerFactory factory;
	private EntityManager em;
	
	public AppUserController()
	{
		this.factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	    this.em = factory.createEntityManager();
	}
	
	public void close()
	{
		this.em.close();
	}
	
	public List<AppUser> getRegisteredUsers()
	{
		Query q = em.createQuery("select u from AppUser u");
        List<AppUser> registeredUsers = q.getResultList();
        return registeredUsers;
	}
	
	public List<AppUser> getLoggedInUsers()
	{
		Query q = em.createQuery("select u from AppUser u where u.loggedIn = true");
        List<AppUser> loggedInUsers = q.getResultList();
        return loggedInUsers;
	}
	
	public message registerUser(String email, String password, String name)
	{
		// check email is correct
		boolean isValidEmail = validateEmail(email);
		if (!isValidEmail){
			return message.InvalidEmail;
		}
		
		// check email doesn't already exist in the db
		boolean emailAlreadyExists = checkUserEmailExists(email);
		if (emailAlreadyExists){
	    	return message.ExistingEmail;
	    }
		
		// encrypt password
		String encPass = encryptPassword(password);
		
		try {
			// add/register user in the db
			AppUser user = new AppUser(email, encPass, name, true, 0);
			em.getTransaction().begin();
			em.persist(user);
			em.getTransaction().commit();
		    return message.SuccesfulRegistration;
		}
		catch (Exception exc) {
			return message.DbError;
		}
	}
	
	private boolean validateEmail(String email) {
		Pattern EMAIL_ADDRESS_REGEX = 
				Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
	}
	
	private boolean checkUserEmailExists(String email){
		Query q1 = em.createQuery("select u from AppUser u where u.email = :email");
		q1.setParameter("email", email);
		List<AppUser> existingUser = q1.getResultList();
		return (existingUser.size() == 1) ? true:false;
	}
	
	public List<AppUser> findUser(String email){
		Query q = em.createQuery("select u from AppUser u where u.email = :email");
		q.setParameter("email", email);
		List<AppUser> existingUser = q.getResultList();
		return existingUser;
	}
	
	public String encryptPassword(String pass){
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(pass.getBytes("UTF-8")); // or "UTF-16"
			byte[] hashedBytes = md.digest();
			return bytesToHexString(hashedBytes);
		} catch (NoSuchAlgorithmException|UnsupportedEncodingException exc) {
			throw new RuntimeException("Could not encrypt password. ", exc);
		}
	} 
	
	private String bytesToHexString(byte[] arrayBytes) {
	    StringBuffer hexString = new StringBuffer();
	    for (int i = 0; i < arrayBytes.length; i++) {
	    	hexString.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16).substring(1));
	    }
	    return hexString.toString();
	}
	
	public message logInUser(String email, String password){
		List<AppUser> existingUser = findUser(email);
		String encryptedPassword = encryptPassword(password);
		
		if (existingUser.size() != 1){	// check if email exists in the db
	    	return message.UserNotRegistered;
	    }
	    else if (!encryptedPassword.equals(existingUser.get(0).getPassword())){ // check if the password matches
			// increase Incorrect Password Count
	    	AppUser user = existingUser.get(0);
			int incorrectPassCount = user.getIncorrectPasswordCount() + 1;
			user.setIncorrectPasswordCount(incorrectPassCount);
			em.getTransaction().begin();
			em.persist(user);
			em.getTransaction().commit();
		    
			return message.IncorrectPassword;
		}
		else if (existingUser.get(0).getIncorrectPasswordCount() >= 5){	// check if account is blocked because the password was introduced incorrectly 5 times
			return message.BlockedUser;
		}
		else{	// else log in correctly
			AppUser user = existingUser.get(0);
			user.setLoggedIn(true);
			em.getTransaction().begin();
			em.persist(user);
			em.getTransaction().commit();
		    return message.SuccessfulLogin;
		}
	}
	
	public message logOutUser(String email){
		List<AppUser> existingUser = findUser(email);
		
		if (existingUser.size() != 1){	// check if email exists in the db
	    	return message.UserNotRegistered;
	    }
	    else{	// else log out user
			AppUser user = existingUser.get(0);
			user.setLoggedIn(false);
			em.getTransaction().begin();
			em.persist(user);
			em.getTransaction().commit();
		    return message.SuccessfulLogout;
		}
	}

	public message deleteUser(String email){
		List<AppUser> existingUser = findUser(email);
		
		if (existingUser.size() != 1){	// check if email exists in the db
	    	return message.UserNotRegistered;
	    }
		else{
			em.getTransaction().begin();
			em.remove(existingUser.get(0));
		    em.getTransaction().commit();
			return message.UserDeleted;
		}
	}
}
