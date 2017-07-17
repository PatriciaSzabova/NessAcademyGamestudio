package sk.tuke.gamestudio.server.service.JPA;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import sk.tuke.gamestudio.server.entity.User;
import sk.tuke.gamestudio.server.service.UserService;

@Transactional
public class UserServiceJPA implements UserService {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public User login(String username, String passwd) {
		User user = null;
		try {
			user = (User) entityManager.createNamedQuery("User.login").setParameter("username", username)
					.setParameter("passwd", passwd).getSingleResult();
		} catch (NoResultException e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public User register(String username, String passwd) {
		User user = new User(username, passwd);
		entityManager.persist(user);
		return user;
	}

	public boolean existsUser(String username) {
		try {
			User user = (User) entityManager.createQuery("SELECT u FROM User u WHERE u.username=:username")
					.setParameter("username", username).getSingleResult();
			return true;
		} catch (NoResultException e) {
			e.printStackTrace();
			return false;
		}
	}
}
