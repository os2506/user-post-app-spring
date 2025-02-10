package com.oss.demo.dto;
import java.util.List;

public class UserDTO {
    private Long id;
    private String name;
    private String username;
    private String email;
    private AddressDTO address;
    private CompanyDTO company;
    private String phone;
    private String website;
    private List<String> postTitles;

    // Default constructor
    public UserDTO() {}
    
   

    // Constructor with parameters
    public UserDTO(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
    
    

    // Constructor
    public UserDTO(Long id, String name, String username, String email, AddressDTO address, 
                   CompanyDTO company, String phone, String website, List<String> postTitles) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.address = address;
        this.company = company;
        this.phone = phone;
        this.website = website;
        this.postTitles = postTitles;
        
        //this.postTitles = user.getPosts().stream()
          //      .map(Post::getTitle) // Map each Post to its title
          //       .collect(Collectors.toList()); // Collect the titles into a List<String>
    }

    // Getters & Setters
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public AddressDTO getAddress() {
		return address;
	}

	public void setAddress(AddressDTO address) {
		this.address = address;
	}

	public CompanyDTO getCompany() {
		return company;
	}

	public void setCompany(CompanyDTO company) {
		this.company = company;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public List<String> getPostTitles() {
		return postTitles;
	}

	public void setPostTitles(List<String> postTitles) {
		this.postTitles = postTitles;
	}
}
