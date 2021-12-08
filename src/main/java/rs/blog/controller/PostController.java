package rs.blog.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import rs.blog.jwt.JwtTokenUtils;
import rs.blog.model.Post;
import rs.blog.model.User;
import rs.blog.service.PostService;
import rs.blog.service.UserService;

@Controller
@RequestMapping(value = "/posts")
public class PostController {

	@Autowired
	private PostService postService;

	@Autowired
	private UserService userService;

	@Autowired
	private JwtTokenUtils jwtTokenUtils;

	@GetMapping
	public ResponseEntity<List<Post>> getAllPosts() {
		List<Post> posts = postService.getAllPosts();
		for (Post post : posts) {
			post.setRating(postService.getRateForPost(post.getId()));
		}
		return new ResponseEntity<>(posts, HttpStatus.OK);
	}

	@GetMapping("/approved")
	public ResponseEntity<List<Post>> getAllApprovedPosts() {
		List<Post> posts = postService.getAllApprovedPosts();
		for (Post post : posts) {
			post.setRating(postService.getRateForPost(post.getId()));
		}
		return new ResponseEntity<>(posts, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Post> getPost(@PathVariable Long id) {
		Post post = postService.getPost(id);
		if (post != null) {
			return new ResponseEntity<>(post, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping()
	public ResponseEntity<Post> savePost(@RequestBody Post post, HttpServletRequest req) {
		String token = jwtTokenUtils.resolveToken(req);
		String email = jwtTokenUtils.getEmail(token);

		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		String dateString = format.format(new Date());

		post.setDateAndTime(dateString);
		post.setUser(userService.findByEmail(email));
		post.setApproved(false);
		Post retVal = postService.save(post);
		if (retVal == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(retVal, HttpStatus.OK);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<Void> updatePost(@PathVariable Long id, @RequestBody Post post) {
		Post foundPost = postService.getPost(id);
		if (foundPost != null) {
			foundPost.setId(id);
			foundPost.setText(post.getText());
			SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
			String dateString = format.format(new Date());
			foundPost.setDateAndTime(dateString);
			postService.save(foundPost);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deletePost(@PathVariable Long id) {
		Post foundPost = postService.getPost(id);
		if (foundPost != null) {
			postService.delete(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping(value = "/{id}/approve")
	public ResponseEntity<Post> approvePost(@PathVariable Long id, @RequestBody Post post) {
		Post foundPost = postService.getPost(id);
		if (foundPost != null) {
			foundPost.setApproved(true);
			Post retVal = postService.save(foundPost);
			return new ResponseEntity<>(retVal, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping(value = "/{id}/rate")
	public ResponseEntity<Void> ratePost(@RequestBody Post post, @PathVariable Long id, HttpServletRequest req) {

		String token = jwtTokenUtils.resolveToken(req);
		String email = jwtTokenUtils.getEmail(token);

		User user = userService.findByEmail(email);

		Post foundPost = postService.getPost(id);
		if (foundPost != null) {
			postService.ratePost(foundPost, user, (int) post.getRating());
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

}
