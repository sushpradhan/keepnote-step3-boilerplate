package com.stackroute.keepnote.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stackroute.keepnote.dao.CategoryDAO;
import com.stackroute.keepnote.dao.NoteDAO;
import com.stackroute.keepnote.dao.ReminderDAO;
import com.stackroute.keepnote.exception.CategoryNotFoundException;
import com.stackroute.keepnote.exception.NoteNotFoundException;
import com.stackroute.keepnote.exception.ReminderNotFoundException;
import com.stackroute.keepnote.model.Category;
import com.stackroute.keepnote.model.Note;
import com.stackroute.keepnote.model.Reminder;

/*
* Service classes are used here to implement additional business logic/validation 
* This class has to be annotated with @Service annotation.
* @Service - It is a specialization of the component annotation. It doesn�t currently 
* provide any additional behavior over the @Component annotation, but it�s a good idea 
* to use @Service over @Component in service-layer classes because it specifies intent 
* better. Additionally, tool support and additional behavior might rely on it in the 
* future.
* */
@Service
public class NoteServiceImpl implements NoteService {

	/*
	 * Autowiring should be implemented for the NoteDAO,CategoryDAO,ReminderDAO.
	 * (Use Constructor-based autowiring) Please note that we should not create any
	 * object using the new keyword.
	 */
	@Autowired
	NoteDAO notedao;
	@Autowired
	CategoryDAO categoryDao;
	@Autowired
	ReminderDAO reminderDao;

	public NoteServiceImpl(NoteDAO notedao, CategoryDAO categoryDao, ReminderDAO reminderDao) {
		super();
		this.notedao = notedao;
		this.categoryDao = categoryDao;
		this.reminderDao = reminderDao;
	}

	/*
	 * This method should be used to save a new note.
	 */

	public boolean createNote(Note note) throws ReminderNotFoundException, CategoryNotFoundException {
		/*
		 * Category category = note.getCategory(); Reminder reminder =
		 * note.getReminder(); if (category == null) { throw new
		 * CategoryNotFoundException("CategoryNotFoundException"); } else {
		 * categoryDao.getCategoryById(category.getCategoryId()); }
		 * 
		 * if (reminder == null) { throw new
		 * ReminderNotFoundException("ReminderNotFoundException"); } else {
		 * reminderDao.getReminderById(reminder.getReminderId()); } return
		 * notedao.createNote(note);
		 */
		Reminder reminder = note.getReminder();
		Category category = note.getCategory();
		try {
			if (reminder != null)
				reminderDao.getReminderById(reminder.getReminderId());
		} catch (ReminderNotFoundException rnf) {
			throw new ReminderNotFoundException("ReminderNotFoundException");
		}
		try {
			if (category != null)
				categoryDao.getCategoryById(category.getCategoryId());
		} catch (CategoryNotFoundException cnf) {
			throw new CategoryNotFoundException("CategoryNotFoundException");
		}

		return notedao.createNote(note);
	}

	/* This method should be used to delete an existing note. */

	public boolean deleteNote(int noteId) {
		return notedao.deleteNote(noteId);

	}
	/*
	 * This method should be used to get a note by userId.
	 */

	public List<Note> getAllNotesByUserId(String userId) {
		return notedao.getAllNotesByUserId(userId);

	}

	/*
	 * This method should be used to get a note by noteId.
	 */
	public Note getNoteById(int noteId) throws NoteNotFoundException {
		Note note = notedao.getNoteById(noteId);
		if (note == null) {
			throw new NoteNotFoundException("NoteNotFoundException");
		} else {
			return note;
		}

	}

	/*
	 * This method should be used to update a existing note.
	 */

	public Note updateNote(Note note, int id)
			throws ReminderNotFoundException, NoteNotFoundException, CategoryNotFoundException {
		/*
		 * Note noteFound = notedao.getNoteById(id); Category catFound =
		 * note.getCategory(); //Reminder remFound = note.getReminder(); if (noteFound
		 * == null) { throw new NoteNotFoundException("NoteNotFoundException"); } else {
		 * notedao.UpdateNote(note); } if (catFound == null) { throw new
		 * CategoryNotFoundException("CategoryNotFoundException"); } else {
		 * categoryDao.getCategoryById(catFound.getCategoryId()); } /*if (remFound ==
		 * null) { throw new ReminderNotFoundException("ReminderNotFoundException"); }
		 * else { reminderDao.getReminderById(remFound.getReminderId()); } return note;
		 */
		Note noteFound = notedao.getNoteById(id);
		Reminder reminder = note.getReminder();
		Category category = note.getCategory();
		if (noteFound == null) {
			throw new NoteNotFoundException("NoteNotFoundException");
		} else {
			notedao.UpdateNote(noteFound);
		}
		try {
			if (reminder != null)
				reminderDao.getReminderById(reminder.getReminderId());
		} catch (ReminderNotFoundException rnf) {
			rnf.printStackTrace();
		}
		try {
			if (category != null)
				categoryDao.getCategoryById(category.getCategoryId());
		} catch (CategoryNotFoundException cnf) {
			cnf.printStackTrace();
		}

		return note;

	}

}
