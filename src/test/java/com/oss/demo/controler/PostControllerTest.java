package com.oss.demo.controler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.oss.demo.dto.PostDTO;
import com.oss.demo.service.PostService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PostControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private PostService postService;

	@InjectMocks
	private PostController postController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
	}

	// Test for createPost
	@Test
	void testCreatePost_Success() throws Exception {
		// Given
		PostDTO postDTO = new PostDTO();
		postDTO.setTitle("Test Title");
		postDTO.setBody("Test Body");
		postDTO.setUserId(1L);
		postDTO.setImagePath("uploads/images/filename.png");

		when(postService.createPost(Mockito.any(PostDTO.class))).thenReturn(postDTO);

		// When & Then
		mockMvc.perform(multipart("/api/posts").file("file", "sample file content".getBytes())
				.param("title", "Test Title").param("body", "Test Body").param("userId", "1"))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.title", is("Test Title")))
				.andExpect(jsonPath("$.body", is("Test Body"))).andExpect(jsonPath("$.userId", is(1)))
				.andExpect(jsonPath("$.imagePath", is("uploads/images/filename.png")));
	}

	// Test for deletePost
	@Test
	void testDeletePost_Success() throws Exception {
		// Given
		Long postId = 1L;

		doNothing().when(postService).deletePost(postId);

		// When & Then
		mockMvc.perform(delete("/api/posts").param("postId", postId.toString())).andExpect(status().isNoContent());
	}

	// Test for getPostsByUserId
	@Test
	void testGetPostsByUserId_Success() throws Exception {
		// Given
		Long userId = 1L;
		List<PostDTO> postList = new ArrayList<>();
		PostDTO postDTO = new PostDTO();
		postDTO.setTitle("Post Title");
		postDTO.setBody("Post Body");
		postDTO.setUserId(userId);
		postList.add(postDTO);

		when(postService.getPostsByUserId(userId)).thenReturn(postList);

		// When & Then
		mockMvc.perform(get("/api/posts").param("userId", userId.toString())).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].title", is("Post Title")))
				.andExpect(jsonPath("$[0].body", is("Post Body")));
	}

	// Test for updatePost
	@Test
	void testUpdatePost_Success() throws Exception {
		// Given
		Long postId = 1L;
		PostDTO updatedPostDTO = new PostDTO();
		updatedPostDTO.setTitle("Updated Title");
		updatedPostDTO.setBody("Updated Body");
		updatedPostDTO.setImagePath("uploads/images/updatedFile.png");

		when(postService.updatePost(Mockito.anyLong(), Mockito.any(PostDTO.class))).thenReturn(updatedPostDTO);

		// When & Then
		mockMvc.perform(put("/api/posts/{id}", postId).param("title", "Updated Title").param("body", "Updated Body")
				.param("imagePath", "uploads/images/updatedFile.png")).andExpect(status().isOk())
				.andExpect(jsonPath("$.title", is("Updated Title"))).andExpect(jsonPath("$.body", is("Updated Body")))
				.andExpect(jsonPath("$.imagePath", is("uploads/images/updatedFile.png")));
	}
}
