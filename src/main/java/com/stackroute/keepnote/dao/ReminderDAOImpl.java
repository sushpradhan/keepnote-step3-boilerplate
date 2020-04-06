package com.stackroute.keepnote.dao;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.stackroute.keepnote.exception.ReminderNotFoundException;
import com.stackroute.keepnote.model.Reminder;

/*
 * This class is implementing the UserDAO interface. This class has to be annotated with 
 * @Repository annotation.
 * @Repository - is an annotation that marks the specific class as a Data Access Object, 
 * thus clarifying it's role.
 * @Transactional - The transactional annotation itself defines the scope of a single database 
 * 					transaction. The database transaction happens inside the scope of a persistence 
 * 					context.  
 * */
@Repository
@Transactional
public class ReminderDAOImpl implements ReminderDAO {

	/*
	 * Autowiring should be implemented for the SessionFactory.(Use
	 * constructor-based autowiring.
	 */
	@Autowired
	SessionFactory sessionfactory;

	public ReminderDAOImpl(SessionFactory sessionFactory) {
		this.sessionfactory = sessionFactory;
	}

	/*
	 * Create a new reminder
	 */

	public boolean createReminder(Reminder reminder) {
		sessionfactory.getCurrentSession().save(reminder);
		return true;

	}

	/*
	 * Update an existing reminder
	 */

	public boolean updateReminder(Reminder reminder) {
		sessionfactory.getCurrentSession().update(reminder);
		return true;

	}

	/*
	 * Remove an existing reminder
	 */

	public boolean deleteReminder(int reminderId) {
		boolean flag = false;
		try {
			if (getReminderById(reminderId) == null) {

				flag = false;
			} else {
				sessionfactory.getCurrentSession().delete(getReminderById(reminderId));
				sessionfactory.getCurrentSession().flush();
				flag=true;
			}
		} catch (HibernateException | ReminderNotFoundException e) {
			e.printStackTrace();
		}
		return flag;
	}

	/*
	 * Retrieve details of a specific reminder
	 */

	public Reminder getReminderById(int reminderId) throws ReminderNotFoundException {

		Reminder reminder = sessionfactory.getCurrentSession().find(Reminder.class, reminderId);
		if (reminder == null) {
			throw new ReminderNotFoundException("ReminderNotFoundException");
		} else {
			return reminder;
		}

	}

	/*
	 * Retrieve details of all reminders by userId
	 */

	public List<Reminder> getAllReminderByUserId(String userId) {
		Query query = sessionfactory.getCurrentSession().createQuery("from Reminder");
		List<Reminder> res = query.list();
		return res.stream().filter(r -> r.getReminderCreatedBy().equals(userId)).collect(Collectors.toList());

	}

}
