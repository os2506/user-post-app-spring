package com.oss.demo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.oss.demo.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

	List<Post> findByUserId(Long userId);

	@Query("SELECT p FROM Post p WHERE p.user.id = :userId")
	List<Post> findPostsByUserId(@Param("userId") Long userId);
}
