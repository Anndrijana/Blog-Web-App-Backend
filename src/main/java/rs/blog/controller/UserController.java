package rs.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import rs.blog.model.User;
import rs.blog.service.UserService;

@Controller
public class UserController {
	@Autowired
	private UserService userService;

	@PostMapping(value = "/login", consumes = "application/json")
	public ResponseEntity<String> login(@RequestBody User user) {
		String token = userService.login(user.getEmail(), user.getPassword());
		if (token == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		try {
			ObjectMapper mapper = new ObjectMapper();
			return new ResponseEntity<>(mapper.writeValueAsString(token), HttpStatus.OK);
		} catch (JsonProcessingException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(value = "/signup")
	public ResponseEntity<User> signup(@RequestBody User user) {

		User retVal = userService.signup(user);
		if (retVal == null) {
			// mora biti jedinstveni mail za korisnika
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(retVal, HttpStatus.CREATED);
	}

}
