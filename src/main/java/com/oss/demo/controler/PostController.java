package com.oss.demo.controler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.oss.demo.service.PostService;
import com.oss.demo.dto.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@RestController
@RequestMapping("/api/posts")
// Pour autoriser React à consommer l'API
@CrossOrigin(origins = "http://localhost:3000")
public class PostController {

	private final PostService postService;

	public PostController(PostService postService) {
		this.postService = postService;
	}

	// http://localhost:8080/api/posts/2
	/*
	 * @GetMapping("/{userId}") public List<PostDTO>
	 * getPostsByUserId(@PathVariable("userId") Long userId) { return
	 * postService.getPostsByUserId(userId); }
	 */

	// http://localhost:8080/api/posts?userId=1
	@GetMapping
	public ResponseEntity<List<PostDTO>> getPostsByUserId(@RequestParam("userId") Long userId) {
		List<PostDTO> posts = postService.getPostsByUserId(userId);
		return ResponseEntity.ok(posts);
	}

	@PostMapping
	public ResponseEntity<?> createPost(@RequestParam("title") String title, @RequestParam("body") String body,
			@RequestParam("userId") Long userId, @RequestParam(value = "file", required = false) MultipartFile file) {

		String imagePath = null;

		if (file != null && !file.isEmpty()) {
			try {
				imagePath = saveImage(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// Create PostDTO with the other data
		PostDTO postDTO = new PostDTO();
		postDTO.setTitle(title);
		postDTO.setBody(body);
		postDTO.setUserId(userId);
		postDTO.setImagePath(imagePath);

		// Create the post using the postDTO
		PostDTO createdPostDTO = postService.createPost(postDTO);

		return ResponseEntity.status(HttpStatus.CREATED).body(createdPostDTO);
	}

	public String saveImage(MultipartFile file) throws IOException {
		Path storageDirectory = Paths.get("uploads/images");

		if (!Files.exists(storageDirectory)) {
			Files.createDirectories(storageDirectory);
		}

		String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
		Path filePath = storageDirectory.resolve(fileName);

		Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

		return filePath.toString();
	}

	// http://localhost:8080/api/posts?postId=3
	@DeleteMapping
	public ResponseEntity<Void> deletePost(@RequestParam("postId") Long postId) {
		postService.deletePost(postId);
		return ResponseEntity.noContent().build();
	}

	// Handle PUT request to update the post
	@PutMapping("/{id}")
	public ResponseEntity<?> updatePost(@PathVariable("id") Long id, @RequestParam("title") String title,
			@RequestParam("body") String body, @RequestParam(value = "imagePath", required = false) String imagePath,
			@RequestParam(value = "file", required = false) MultipartFile file) {

		try {
			String newImagePath = null;
			if (file != null && !file.isEmpty()) {
				newImagePath = saveImage(file);
			} else {
				newImagePath = imagePath;
			}

			PostDTO updatedPost = new PostDTO();
			updatedPost.setTitle(title);
			updatedPost.setBody(body);
			updatedPost.setImagePath(newImagePath);

			PostDTO updatedPostDTO = postService.updatePost(id, updatedPost);

			return ResponseEntity.status(HttpStatus.OK).body(updatedPostDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating post");
		}
	}

}
