package com.oss.demo.dto;

import com.oss.demo.model.Post;

public class PostDTO {
    private Long id;
    private String title;
    private String body;
    private Long userId;
    private String imagePath; // Path to the image
    
    // Default Constructor (Required for Jackson)
    public PostDTO() {}

    // Constructor for manual instantiation
    public PostDTO(Long id, String title, String body, String imagePath, Long userId) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.imagePath = imagePath;
        this.userId = userId;
    }

    // Constructor to convert Post to PostDTO
    public PostDTO(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.body = post.getBody();
        this.imagePath = post.getImagePath();
        this.userId = post.getUser().getId();
    }
    
    // Getters and setters for all fields
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
