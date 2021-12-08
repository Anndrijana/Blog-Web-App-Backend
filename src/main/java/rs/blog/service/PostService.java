package rs.blog.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.blog.model.Post;
import rs.blog.model.Rating;
import rs.blog.model.User;
import rs.blog.repository.PostRepository;
import rs.blog.repository.RatingRepository;

@Service
public class PostService {
	@Autowired
	PostRepository postRepository;

	@Autowired
	RatingRepository ratingRepository;

	public List<Post> getAllPosts() {
		return postRepository.findAll();
	}

	public List<Post> getAllApprovedPosts() {
		return postRepository.findAllApprovedPosts();
	}

	public Post getPost(Long id) {
		Optional<Post> post = postRepository.findById(id);
		if (post.isPresent()) {
			return post.get();
		}
		return null;
	}

	public Post save(Post post) {
		return postRepository.save(post);
	}

	public void delete(Long id) {
		postRepository.deleteById(id);
	}

	public void ratePost(Post post, User user, int rate) {
		Rating rating = new Rating(user, post, rate);
		ratingRepository.save(rating);
	}

	public double getRateForPost(Long id) {
		List<Rating> ratings = ratingRepository.findByPostId(id);
		if (!ratings.isEmpty()){
			double sum = 0;
			for (Rating rating : ratings) {
				sum += rating.getRate();
			}
			return Math.round(sum/ratings.size() * 100.0) / 100.0;
		}
		return 0.0;
	}

}
