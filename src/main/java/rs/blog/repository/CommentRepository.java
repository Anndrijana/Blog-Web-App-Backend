package rs.blog.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import rs.blog.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
	@Query("SELECT c FROM Comment c WHERE c.post.id = ?1 ORDER BY c.dateAndTime")
	public List<Comment> findAllCommentByPostId(Long id);

	@Modifying
	@Transactional
	@Query(value = "delete from Comment c where c.id = ?1")
	void delete(Long id);

}
