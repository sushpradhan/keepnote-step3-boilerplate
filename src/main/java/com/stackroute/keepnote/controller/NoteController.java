package com.stackroute.keepnote.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.stackroute.keepnote.exception.CategoryNotFoundException;
import com.stackroute.keepnote.exception.NoteNotFoundException;
import com.stackroute.keepnote.exception.ReminderNotFoundException;
import com.stackroute.keepnote.model.Note;
import com.stackroute.keepnote.service.NoteService;

/*
 * As in this assignment, we are working with creating RESTful web service, hence annotate
 * the class with @RestController annotation.A class annotated with @Controller annotation
 * has handler methods which returns a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized 
 * format. Starting from Spring 4 and above, we can use @RestController annotation which 
 * is equivalent to using @Controller and @ResposeBody annotation
 */
@RestController
public class NoteController {

	/*
	 * Autowiring should be implemented for the NoteService. (Use Constructor-based
	 * autowiring) Please note that we should not create any object using the new
	 * keyword
	 */
	@Autowired
	NoteService noteService;

	public NoteController(NoteService noteService) {
		this.noteService = noteService;
	}

	/*
	 * Define a handler method which will create a specific note by reading the
	 * Serialized object from request body and save the note details in a Note table
	 * in the database.Handle ReminderNotFoundException and
	 * CategoryNotFoundException as well. please note that the loggedIn userID
	 * should be taken as the createdBy for the note.This handler method should
	 * return any one of the status messages basis on different situations: 1.
	 * 201(CREATED) - If the note created successfully. 2. 409(CONFLICT) - If the
	 * noteId conflicts with any existing user3. 401(UNAUTHORIZED) - If the user
	 * trying to perform the action has not logged in.
	 * 
	 * This handler method should map to the URL "/note" using HTTP POST method
	 */
	@PostMapping("/note")
	public ResponseEntity<?> createNote(@RequestBody Note note, HttpSession session) {
		try {
			if (session.getAttribute("loggedInUserId").equals(note.getCreatedBy())) {
				try {
					if (noteService.createNote(note)) {
						return new ResponseEntity<String>("Created", HttpStatus.CREATED);
					} else {
						return new ResponseEntity<String>("Conflict", HttpStatus.CONFLICT);
					}
				} catch (ReminderNotFoundException rnfe) {
					return new ResponseEntity<String>("Reminder not found", HttpStatus.UNAUTHORIZED);
				} catch (CategoryNotFoundException cnfe) {
					return new ResponseEntity<String>("Category not found", HttpStatus.UNAUTHORIZED);
				}
			} else {
				return new ResponseEntity<String>("Unauthorised user", HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>("No match found", HttpStatus.UNAUTHORIZED);
		}
	}

	/*
	 * Define a handler method which will delete a note from a database.
	 * 
	 * This handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - If the note deleted successfully from
	 * database. 2. 404(NOT FOUND) - If the note with specified noteId is not found.
	 * 3. 401(UNAUTHORIZED) - If the user trying to perform the action has not
	 * logged in.
	 * 
	 * This handler method should map to the URL "/note/{id}" using HTTP Delete
	 * method" where "id" should be replaced by a valid noteId without {}
	 */
	@DeleteMapping("/note/{id}")
	public ResponseEntity<?> deleteNote(@PathVariable int id, HttpSession session) {
		try {
			if (session != null && session.getAttribute("loggedInUserId") != null) {
				if (noteService.deleteNote(id))
					return new ResponseEntity<String>("Deleted", HttpStatus.OK);
				else
					throw new NoteNotFoundException("NoteNotFoundException");
			} else {
				return new ResponseEntity<String>("Note not found", HttpStatus.UNAUTHORIZED);
			}
		} catch (NoteNotFoundException e) {
			return new ResponseEntity<String>("Note not found", HttpStatus.NOT_FOUND);
		}
	}

	/*
	 * Define a handler method which will update a specific note by reading the
	 * Serialized object from request body and save the updated note details in a
	 * note table in database handle ReminderNotFoundException,
	 * NoteNotFoundException, CategoryNotFoundException as well. please note that
	 * the loggedIn userID should be taken as the createdBy for the note. This
	 * handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - If the note updated successfully. 2.
	 * 404(NOT FOUND) - If the note with specified noteId is not found. 3.
	 * 401(UNAUTHORIZED) - If the user trying to perform the action has not logged
	 * in.
	 * 
	 * This handler method should map to the URL "/note/{id}" using HTTP PUT method.
	 */
	@PutMapping("/note/{id}")
	public ResponseEntity<?> updateNote(@RequestBody Note note, HttpSession session) {
		try {
			if (session != null && session.getAttribute("loggedInUserId") != null
					&& session.getAttribute("loggedInUserId").equals(note.getCreatedBy())) {
				if (noteService.updateNote(note, note.getNoteId()) != null)
					return new ResponseEntity<Note>(note, HttpStatus.OK);
				else
					throw new Exception("Not found");

			} else {
				return new ResponseEntity<String>("Note not found", HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>("Note not found", HttpStatus.NOT_FOUND);
		}
	}

	/*
	 * Define a handler method which will get us the notes by a userId.
	 * 
	 * This handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - If the note found successfully. 2.
	 * 401(UNAUTHORIZED) -If the user trying to perform the action has not logged
	 * in.
	 * 
	 * 
	 * This handler method should map to the URL "/note" using HTTP GET method
	 */
	@GetMapping("/note")
	public ResponseEntity<?> getNotesByUserId(HttpSession session) {
		if (session != null && session.getAttribute("loggedInUserId") != null) {
			List<Note> notes = noteService.getAllNotesByUserId(session.getAttribute("loggedInUserId").toString());
			return new ResponseEntity<List<Note>>(notes, HttpStatus.OK);

		} else {
			return new ResponseEntity<String>("Not found", HttpStatus.UNAUTHORIZED);
		}
	}

	@GetMapping
	public String hello() {
		return "index";
	}

}
