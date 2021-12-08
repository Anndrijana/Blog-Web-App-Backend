package rs.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.blog.jwt.JwtTokenUtils;
import rs.blog.model.Role;
import rs.blog.model.User;
import rs.blog.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtTokenUtils jwtTokenUtils;

	public String login(String email, String password) {
		User user = userRepository.findByEmail(email);
		if (user != null && 
			user.getEmail().equals(email) && user.getPassword().equals(password)) {
				return jwtTokenUtils.createToken(user.getEmail(), user.getRole());
		}
		return null;

	}

	public User signup(User user) {
		if (!userRepository.existsByEmail(user.getEmail())) {
			user.setRole(Role.USER);
			return userRepository.save(user);
		}

		return null;
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

}
