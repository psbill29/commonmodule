package com.xworks.commonmodule.dao;

import java.io.Serializable;
import java.util.Objects;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.xworks.commonmodule.dto.RegisterDTO;
import com.xworks.commonmodule.entity.RegisterEntity;

@Component
public class RegisterDAOImpl implements RegisterDAO {

	private static final Logger log = Logger.getLogger(RegisterDAOImpl.class);

	@Autowired
	private SessionFactory factory;

	RegisterEntity registerEntity = null;

	public boolean saveUser(RegisterEntity registerEntity) {
		log.info("invoked saveUser inside the RegisterDAOImpl ....");
		Session session = null;
		try {
			session = factory.openSession();
			session.beginTransaction();
			Serializable serializable = session.save(registerEntity);
			if (Objects.nonNull(serializable)) {
				log.info("registration  data good to be saved into DB...");
			} else {
				log.info("registration data not save to DB..." + "\n");
			}
			session.getTransaction().commit();
			log.info("User Data saved into the Database..");
		} catch (HibernateException h) {
			session.getTransaction().rollback();
			log.error(h.getMessage(), h);
		} finally {
			if (Objects.nonNull(session)) {
				session.close();
			}
		}
		return false;

	}

	public RegisterEntity validateUserID(RegisterDTO registerDTO, Model model) {

		log.info("invoked validateUserID....");
		Session session = null;
		RegisterEntity registerEntity = null;
		try {
			session = factory.openSession();
			session.beginTransaction();

			String hqlFetchByUserId = "from RegisterEntity where user_id = '" + registerDTO.getUser_id() + "'";
			log.info("HQL Query to check the user ID exist or no: " + hqlFetchByUserId);

			Query<RegisterEntity> userQuery = session.createQuery(hqlFetchByUserId);
			log.info("Query created by Userid :" + userQuery);

			registerEntity = userQuery.uniqueResult();
			log.info("Query created and got results from the DB under validate by userid :" + registerEntity);
			return registerEntity;

		} catch (HibernateException h) {
			session.getTransaction().rollback();
			log.error(h.getMessage(), h);
		} finally {
			if (Objects.nonNull(session)) {
				session.close();
			}
		}
		return null;

	}

	public RegisterEntity validateEmail(RegisterDTO registerDTO, Model model) {
		log.info("invoked validateEmail....");

		Session session = null;
		RegisterEntity registerEntity = null;

		try {
			session = factory.openSession();

			String hqlFetchByEmail = "from RegisterEntity where email = '" + registerDTO.getEmail() + "'";
			log.info("HQl query created to fetch details by email which returns registerEntity Object: ");

			Query<RegisterEntity> emailQuery = session.createQuery(hqlFetchByEmail, RegisterEntity.class);
			log.info("Query created to get details : " + emailQuery);

			registerEntity = (RegisterEntity) emailQuery.uniqueResult();
			log.info("Query created and got results from the DB under validate by email:" + registerEntity);

			if (Objects.isNull(registerEntity)) {
				log.info("No data found :" + registerEntity);
				return null;
			}
			return registerEntity;
		} catch (HibernateException h) {
			session.getTransaction().rollback();
			log.error(h.getMessage(), h);
		} finally {
			if (Objects.nonNull(session)) {
				log.info("session closed in the finally block");
				session.close();
			}
		}
		return null;
	}

	public Integer checkAttempts(String loginEmail) {
		log.info("inside the checkAttempts: ");
		Session session = null;
		Integer attemptCount = 0;
		try {
			session = factory.openSession();
			session.beginTransaction();

			// to check attempts more than 3 times

			String attemptsHql = "from RegisterEntity where email='" + loginEmail + "'";
			log.info("HQL query to provide further to QUERY :" + attemptsHql);

			Query attemptQuery = session.createQuery(attemptsHql);
			log.info("Query created :" + attemptQuery);

			registerEntity = (RegisterEntity) attemptQuery.uniqueResult();
			log.info("value of register entity before assigning to attemptCount :" + registerEntity);

			if (Objects.isNull(registerEntity)) {
				log.info("attempts value inside ISNULL method under DAO :" + attemptCount);
				return 0;
			}

			attemptCount = registerEntity.getNoOfAttempts();
			log.info("value of attempts under DAO before ISNULL :" + attemptCount);

			return attemptCount;

		} catch (Exception e) {
			session.getTransaction().rollback();
			log.error(e.getMessage(), e);

		} finally {
			if (Objects.nonNull(session)) {
				session.close();
				log.info("session closed in finnaly fro add attempts");
			}
		}
		return attemptCount;

	}

	public Integer addAttempts(String loginEmail, int noOfAttempts) {

		log.info("inside the addAttempts: ");
		Session session = null;

		try {
			session = factory.openSession();
			session.beginTransaction();

			String hqlUpdateAttempt = "update RegisterEntity set noOfAttempts='" + noOfAttempts + "' where email='"
					+ loginEmail + "'";
			log.info("HQL query created to update count:" + hqlUpdateAttempt);

			Query query = session.createQuery(hqlUpdateAttempt);
			log.info("Query is created :" + query);

			Integer totalAttempts = query.executeUpdate();
			log.info("return query true or false " + query);

			log.info("loginemail: " + loginEmail + "\n" +"\t"+"\t"+ "nnoOfAttempts :" + totalAttempts);
			session.getTransaction().commit();
			return totalAttempts;

		} catch (Exception e) {
			session.getTransaction().rollback();
			log.error(e.getMessage(), e);
		} finally {
			if (Objects.nonNull(session)) {
				session.close();
				log.info("session closed in finnaly fro add attempts");
			}
		}
		return null;
	}

}
