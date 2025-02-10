package com.oss.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.oss.demo.repository.UserRepository;
import java.util.*;
import java.util.stream.Collectors;
import com.oss.demo.dto.*;
import com.oss.demo.exceptions.users.UserNotFoundException;
import com.oss.demo.exceptions.users.UserAlreadyExistsException;
import com.oss.demo.model.Post;
import com.oss.demo.model.User;
import com.oss.demo.model.addressUser;
import com.oss.demo.model.Company;

@Service
@Transactional
public class UserService {

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	// Convert DTO to User Entity
	private User convertToEntity(UserDTO userDTO) {
		User user = new User();
		user.setId(userDTO.getId());
		user.setName(userDTO.getName());
		user.setUsername(userDTO.getUsername());
		user.setEmail(userDTO.getEmail());
		user.setPhone(userDTO.getPhone());
		user.setWebsite(userDTO.getWebsite());

		// Convertir AddressDTO en Address
		addressUser address = new addressUser();
		address.setStreet(userDTO.getAddress().getStreet());
		address.setCity(userDTO.getAddress().getCity());
		address.setZipcode(userDTO.getAddress().getZipcode());
		user.setAddress(address);

		// Convertir CompanyDTO en Company
		Company company = new Company();
		company.setName(userDTO.getCompany().getName());
		company.setCatchPhrase(userDTO.getCompany().getCatchPhrase());
		company.setBs(userDTO.getCompany().getBs());
		user.setCompany(company);

		// Ajouter les posts
		List<Post> posts = new ArrayList<>();
		if (userDTO.getPostTitles() != null) {
			for (String title : userDTO.getPostTitles()) {
				Post post = new Post();
				post.setTitle(title);
				post.setUser(user);
				posts.add(post);
			}
		}
		user.setPosts(posts);

		// Persister l'entité
		user = userRepository.save(user);

		return user;
	}

	public List<UserDTO> getAllUsers() {
		List<User> users = userRepository.findAll();
		return users.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	public UserDTO getUserById(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé avec l'ID: " + userId));

		// Convert Address & Company to DTOs
		AddressDTO addressDTO = new AddressDTO(user.getAddress().getStreet(), user.getAddress().getCity(),
				user.getAddress().getZipcode());

		CompanyDTO companyDTO = new CompanyDTO(user.getCompany().getName(), user.getCompany().getCatchPhrase(),
				user.getCompany().getBs());

		// Convert Post List (Only Titles)
		List<String> postTitles = user.getPosts().stream().map(Post::getTitle).collect(Collectors.toList());

		return new UserDTO(user.getId(), user.getName(), user.getUsername(), user.getEmail(), addressDTO, companyDTO,
				user.getPhone(), user.getWebsite(), postTitles);
	}

	// Save User (Accepts DTO and Converts to Entity)
	public UserDTO saveUser(UserDTO userDTO) {
		// Check if user already exists
		if (userRepository.existsByEmail(userDTO.getEmail())) {
			// checking based on email
			throw new UserAlreadyExistsException("User with email " + userDTO.getEmail() + " already exists.");
		}

		User user = convertToEntity(userDTO);
		return convertToDTO(user);
	}

	// Delete User with Error Handling
	public void deleteUser(Long id) {
		if (!userRepository.existsById(id)) {
			throw new UserNotFoundException("User with ID " + id + " not found.");
		}
		userRepository.deleteById(id);
	}

	// Convert User Entity to UserDTO
	private UserDTO convertToDTO(User user) {
		AddressDTO addressDTO = new AddressDTO(user.getAddress().getStreet(), user.getAddress().getCity(),
				user.getAddress().getZipcode());

		CompanyDTO companyDTO = new CompanyDTO(user.getCompany().getName(), user.getCompany().getCatchPhrase(),
				user.getCompany().getBs());

		List<String> postTitles = user.getPosts().stream().map(Post::getTitle).collect(Collectors.toList());

		return new UserDTO(user.getId(), user.getName(), user.getUsername(), user.getEmail(), addressDTO, companyDTO,
				user.getPhone(), user.getWebsite(), postTitles);
	}
}
