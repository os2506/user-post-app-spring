package com.oss.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.oss.demo.exceptions.users.UserNotFoundException;
import com.oss.demo.exceptions.users.UserAlreadyExistsException;
import com.oss.demo.exceptions.posts.PostNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

	// Utilisateur introuvable
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
		// Renvoie une erreur 404 avec le msg recuperer par l'exception
		// UserNotFoundException
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}

	// Utilisateur déja existant
	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	// Post introuvable
	@ExceptionHandler(PostNotFoundException.class)
	public ResponseEntity<String> handlePostNotFoundException(PostNotFoundException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleGlobalException(Exception e) {
		// Pour toutes les autres exceptions, renvoie une erreur 500 (serveur)
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("Une erreur s'est produite : " + e.getMessage());
	}

}
