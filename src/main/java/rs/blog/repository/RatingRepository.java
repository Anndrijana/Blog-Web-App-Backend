package rs.blog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.blog.model.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

	public List<Rating> findByPostId(Long id);

}
