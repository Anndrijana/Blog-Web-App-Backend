package rs.blog.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.blog.model.Comment;
import rs.blog.repository.CommentRepository;
import rs.blog.repository.PostRepository;

@Service
public class CommentService {
	@Autowired
	PostRepository postRepository;

	@Autowired
	CommentRepository commentRepository;

	public List<Comment> getAllCommentsForPostId(Long postId) {
		return commentRepository.findAllCommentByPostId(postId);
	}

	public Comment getComment(Long id) {
		Optional<Comment> comment = commentRepository.findById(id);
		if (comment.isPresent()) {
			return comment.get();
		}
		return null;
	}

	public Comment save(Comment comment) {
		return commentRepository.save(comment);
	}

	public void delete(Long commentId) {
		commentRepository.delete(commentId);
	}

}
