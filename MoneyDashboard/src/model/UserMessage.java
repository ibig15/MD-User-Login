package model;
import java.util.EnumMap;

public class UserMessage {
	public enum message {
		InvalidEmail, ExistingEmail, SuccesfulRegistration, DbError, UserNotRegistered, IncorrectPassword, SuccessfulLogin, SuccessfulLogout, BlockedUser, UserDeleted
		}

	private EnumMap<message, String> userMessage;
	
	public UserMessage(){
		this.userMessage = new EnumMap<>(message.class);
		userMessage.put(message.InvalidEmail, "The email you introduced is invalid. Please introduce a valid email.");
		userMessage.put(message.ExistingEmail, "The email you introduced is already registered.");
		userMessage.put(message.SuccesfulRegistration, "The new user was successfully added to the database.");
		userMessage.put(message.DbError, "Could not add user in the database.");
		userMessage.put(message.UserNotRegistered, "This user is not registered.");
		userMessage.put(message.IncorrectPassword, "The introduced password is incorrect.");
		userMessage.put(message.SuccessfulLogin, "The user is logged in.");
		userMessage.put(message.SuccessfulLogout, "The user is logged out.");
		userMessage.put(message.BlockedUser, "The user was blocked after introducing an incorrect password 5 times. Password reset is needed.");
		userMessage.put(message.UserDeleted, "The user was deleted.");
	}
	
	public String getUserMsg(message m) {
		return userMessage.get(m);
	}
	
}
