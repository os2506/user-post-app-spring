package com.oss.demo.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.oss.demo.repository.PostRepository;
import com.oss.demo.dto.PostDTO;
import com.oss.demo.model.Post;
import com.oss.demo.model.User;
import com.oss.demo.exceptions.posts.PostNotFoundException;

@Service
public class PostService {

	private final PostRepository postRepository;

	public PostService(PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	// get All posts
	public List<PostDTO> getAllPosts() {
		List<Post> posts = postRepository.findAll();
		return posts.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	// Convert User Entity to UserDTO
	public PostDTO convertToDTO(Post post) {
		return new PostDTO(post.getId(), post.getTitle(), post.getBody(), post.getImagePath(),
				post.getUser() != null ? post.getUser().getId() : null // Avoid NullPointerException
		);
	}

	// get posts by userId
	public List<PostDTO> getPostsByUserId(Long userId) {
		List<Post> posts = postRepository.findPostsByUserId(userId);

		if (posts.isEmpty()) {
			throw new PostNotFoundException("No posts found for user ID: " + userId);
		}

		return posts.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	// create Post
	public PostDTO createPost(PostDTO postDTO) {
		Post post = convertToEntity(postDTO);
		Post savedPost = postRepository.save(post);
		return convertToDTO(savedPost);
	}

	// delete Post
	public void deletePost(Long id) {
		boolean exists = checkIfPostExists(id);
		if (!exists) {
			throw new PostNotFoundException("Post with ID " + id + " not found.");
		}
		postRepository.deleteById(id);
	}

	private boolean checkIfPostExists(Long id) {
		return postRepository.existsById(id);
	}

	private Post convertToEntity(PostDTO postDTO) {
		Post post = new Post();
		post.setTitle(postDTO.getTitle());
		post.setBody(postDTO.getBody());
		post.setImagePath(postDTO.getImagePath());

		if (postDTO.getUserId() != null) {
			User user = new User();
			user.setId(postDTO.getUserId());
			post.setUser(user);
		}

		return post;
	}

	public PostDTO updatePost(Long id, PostDTO postDTO) {
		// Retrieve the post from the database
		Post existingPost = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));

		// Update the post fields
		existingPost.setTitle(postDTO.getTitle());
		existingPost.setBody(postDTO.getBody());
		existingPost.setImagePath(postDTO.getImagePath()); // If an image is updated, this will be set

		// Save the updated post
		postRepository.save(existingPost);

		// Convert to DTO and return
		return new PostDTO(existingPost);
	}
}
