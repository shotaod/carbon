package org.dabuntu.sample.domain.service;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.component.annotation.Inject;
import org.dabuntu.sample.domain.entity.Role;
import org.dabuntu.sample.domain.entity.User;
import org.dabuntu.sample.domain.entity.User_;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;

/**
 * @author ubuntu 2016/11/13.
 */
@Component
public class UserRoleService {
	@Inject
	private EntityManagerFactory factory;

	public User createUser(User user, Set<Role> roles) {
		user.setRoles(roles);
		EntityManager em = factory.createEntityManager();
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		try {
			em.persist(em);
			transaction.commit();
		} catch (Throwable throwable){
			transaction.rollback();
		}
//		em.close();
		return user;
	}

	public User findByUsername(String username) {
		EntityManager em = factory.createEntityManager();

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<User> criteria = builder.createQuery(User.class);
		Root<User> root = criteria.from(User.class);
		criteria.where(builder.equal(root.get(User_.username), username));
		try {
			return em.createQuery(criteria).getSingleResult();
		} catch (NoResultException ignore) {
			return null;
		}
	}

	public List<User> findUsers() {
		EntityManager em = factory.createEntityManager();

		CriteriaQuery<User> criteria = em.getCriteriaBuilder().createQuery(User.class);
		Root<User> root = criteria.from(User.class);

		return em.createQuery(criteria).getResultList();
	}
}
