package com.oss.demo.controler;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oss.demo.dto.UserDTO;
import com.oss.demo.exceptions.users.UserNotFoundException;
import com.oss.demo.service.UserService;

class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private UserService userService;

	@InjectMocks
	private UserController userController;

	
	private ObjectMapper objectMapper;

	@BeforeEach
	public void setup() {
		objectMapper = new ObjectMapper();
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
	}

	// Test GET AllUser /api/users
	@Test
	public void testGetAllUsers_Success() throws Exception {
		List<UserDTO> users = Arrays.asList(new UserDTO(3L, "John Doe", "john.doe@example.com"));
		when(userService.getAllUsers()).thenReturn(users);

		mockMvc.perform(get("/api/users")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(3))
				.andExpect(jsonPath("$[0].name").value("John Doe"))
				.andExpect(jsonPath("$[0].email").value("john.doe@example.com"));
	}

	// Test GET AllUser not found
	@Test
    public void testGetAllUsers_NoUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Aucun utilisateur trouvé."));
    }

	// Test GET UserByID success /api/users/{id}
	@Test
	public void testGetUserById_Success() throws Exception {
		UserDTO user = new UserDTO(3L, "John Doe", "john.doe@example.com");
		when(userService.getUserById(3L)).thenReturn(user);

		mockMvc.perform(get("/api/users/{id}", 3L)).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(3))
				.andExpect(jsonPath("$.name").value("John Doe"))
				.andExpect(jsonPath("$.email").value("john.doe@example.com"));
	}

	// Test GET UserByID Not found /api/users/{id}
	@Test
    public void testGetUserById_NotFound() throws Exception {
        when(userService.getUserById(999L)).thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(get("/api/users/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));
    }

	// Test POST User /api/users
	@Test
	public void testCreateUser_Success() throws Exception {
		UserDTO user = new UserDTO(4L, "Toto", "toto@gmail.com");
		when(userService.saveUser(any(UserDTO.class))).thenReturn(user);

		mockMvc.perform(
				post("/api/users").contentType("application/json").content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(4))
				.andExpect(jsonPath("$.name").value("Toto"))
				.andExpect(jsonPath("$.email").value("toto@gmail.com"));
	}

	// Test DELETE User /api/users/{id}
	@Test
	public void testDeleteUser_Success() throws Exception {
		doNothing().when(userService).deleteUser(4L);

		mockMvc.perform(delete("/api/users/{id}", 4L)).andExpect(status().isOk())
				.andExpect(content().string("User deleted successfully."));
	}

	// Test DELETE User Not found
	@Test
	public void testDeleteUser_NotFound() throws Exception {
		doThrow(new UserNotFoundException("User not found")).when(userService).deleteUser(999L);

		mockMvc.perform(delete("/api/users/{id}", 999L)).andExpect(status().isNotFound())
				.andExpect(content().string("User not found"));
	}

}
