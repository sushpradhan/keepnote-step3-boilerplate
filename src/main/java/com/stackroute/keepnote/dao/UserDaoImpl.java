package com.stackroute.keepnote.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.model.User;

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
public class UserDaoImpl implements UserDAO {

	/*
	 * Autowiring should be implemented for the SessionFactory.(Use
	 * constructor-based autowiring.
	 */
	@Autowired
	SessionFactory sessionFactory;

	public UserDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/*
	 * Create a new user
	 */

	public boolean registerUser(User user) {
		sessionFactory.getCurrentSession().save(user);
		return true;
	}

	/*
	 * Update an existing user
	 */

	public boolean updateUser(User user) {
		sessionFactory.getCurrentSession().update(user);
		return true;

	}

	/*
	 * Retrieve details of a specific user
	 */
	public User getUserById(String UserId) {
		return sessionFactory.getCurrentSession().find(User.class, UserId);
	}

	/*
	 * validate an user
	 */

	public boolean validateUser(String userId, String password) throws UserNotFoundException {
		User user = sessionFactory.getCurrentSession().find(User.class, userId);
		if (user == null) {
			throw new UserNotFoundException("UserNotFoundException");
		} else {
			return (user.getUserPassword().equals(password) && user.getUserId().equals(userId));
		}

	}

	/*
	 * Remove an existing user
	 */
	public boolean deleteUser(String userId) {
		if (getUserById(userId) == null || userId == null) {
			return false;
		} else {
			sessionFactory.getCurrentSession().delete(getUserById(userId));
			sessionFactory.getCurrentSession().flush();
			return true;
		}

	}

}
