package rs.blog.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import rs.blog.jwt.JwtTokenUtils;
import rs.blog.model.Comment;
import rs.blog.model.Post;
import rs.blog.model.User;
import rs.blog.service.CommentService;
import rs.blog.service.PostService;
import rs.blog.service.UserService;

@Controller
public class CommentController {

	@Autowired
	CommentService commentService;

	@Autowired
	UserService userService;

	@Autowired
	PostService postService;

	@Autowired
	JwtTokenUtils jwtTokenUtils;

	@PostMapping(value = "/posts/{postId}/comments")
	public ResponseEntity<Void> saveComment(HttpServletRequest req, @PathVariable Long postId,
			@RequestBody Comment comment) {

		String token = jwtTokenUtils.resolveToken(req);
		String email = jwtTokenUtils.getEmail(token);
		User user = userService.findByEmail(email);

		Post foundPost = postService.getPost(postId);
		comment.setPost(foundPost);
		comment.setUser(user);

		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		String dateString = format.format(new Date());
		comment.setDateAndTime(dateString);

		commentService.save(comment);

		return new ResponseEntity<>(HttpStatus.CREATED);

	}

	@PutMapping(value = "/comments/{commentId}")
	public ResponseEntity<Void> updateComment(@PathVariable Long commentId, @RequestBody Comment comment) {
		Comment foundComment = commentService.getComment(commentId);

		if (foundComment != null) {
			foundComment.setId(commentId);
			foundComment.setCommentText(comment.getCommentText());
			SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
			String dateString = format.format(new Date());
			foundComment.setDateAndTime(dateString);

			commentService.save(foundComment);

			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping(value = "/posts/{postId}/comments/{commentId}")
	public ResponseEntity<Void> deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
		Comment foundComment = commentService.getComment(commentId);
		if (foundComment != null) {
			commentService.delete(commentId);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
