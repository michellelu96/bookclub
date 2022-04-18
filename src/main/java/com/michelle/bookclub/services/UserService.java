package com.michelle.bookclub.services;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.michelle.bookclub.models.LoginUser;
import com.michelle.bookclub.models.User;
import com.michelle.bookclub.repositories.UserRespository;

@Service
public class UserService {

	@Autowired
    private UserRespository userRepo;
    
    // TO-DO: Write register and login methods!
    public User register(User newUser, BindingResult result) {
        Optional<User> potentialUser = userRepo.findByEmail(newUser.getEmail());
        
        //check email
        if(potentialUser.isPresent()) {
        	result.rejectValue("email", "unique", "The email already exists");
        }
        // check if passwords match
        if(!newUser.getPassword().equals(newUser.getConfirm())) {
        	result.rejectValue("password", "matches", "The passwords don't match");
        }
        if(result.hasErrors()) {
        	  return null;
        }
       String hashed=BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt());
       newUser.setPassword(hashed);
       return userRepo.save(newUser);
    }
    
    
    public User login(LoginUser newLogin, BindingResult result) {
    	// find user
    	Optional<User> potentialUser = userRepo.findByEmail(newLogin.getEmail());
    	//reject if it doesn't exist
    	if(!potentialUser.isPresent()) {
    		result.rejectValue("email","unique","Email doesn't exist" );
    		return null;
    	}
    	
    	//get from DB
    	User user = potentialUser.get();
    	//check PW
		if(!BCrypt.checkpw(newLogin.getPassword(), user.getPassword())) {
		    result.rejectValue("password", "matches", "Invalid Password!");
		}
        if(result.hasErrors()) {
        	return null;
        }
        return user;
    }
    
    public User getUser(Long id) {
    	Optional<User> optionalUser = userRepo.findById(id);
    	if(optionalUser.isPresent()) {
    		return optionalUser.get();
    	}else {
    		return null;
    	}
    }
}
