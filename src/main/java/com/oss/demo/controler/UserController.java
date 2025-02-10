package com.oss.demo.controler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.oss.demo.dto.UserDTO;
import com.oss.demo.exceptions.users.UserNotFoundException;
import com.oss.demo.service.UserService;
import java.util.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	// Get All user
	@GetMapping
	public ResponseEntity<List<UserDTO>> getAllUsers() {

		try {
			List<UserDTO> users = userService.getAllUsers();

			if (users.isEmpty()) {
				throw new UserNotFoundException("Aucun utilisateur trouvé.");
			}
			return ResponseEntity.ok(users);

		} catch (UserNotFoundException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new RuntimeException("Une erreur s'est produite lors de la récupération des utilisateurs.", ex);
		}

	}

	// Get User by ID (Returns UserDTO)
	@GetMapping("/{id}")
	public ResponseEntity<UserDTO> getUserById(@PathVariable("id") Long id) {
		try {
			UserDTO userDTO = userService.getUserById(id);
			return ResponseEntity.ok(userDTO);
		} catch (UserNotFoundException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new RuntimeException("Une erreur s'est produite lors de la récupération de l'utilisateur.", ex);
		}
	}

	// Create User (Returns UserDTO) // Exception gerer coté service	
	@PostMapping
	public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
	    UserDTO savedUser = userService.saveUser(userDTO);
	    return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
	}

	// Delete User //  Exception gerer coté service
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
	    userService.deleteUser(id);
	    return ResponseEntity.ok("User deleted successfully.");
	}

}