package com.xworks.commonmodule.dao;

import java.util.Objects;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.xworks.commonmodule.entity.RegisterEntity;

@Component
public class ForgotPasswordDAOImpl implements ForgotPasswordDAO {

	private static final Logger log = Logger.getLogger(ForgotPasswordDAOImpl.class);

	@Autowired
	private SessionFactory factory = null;

	Session session = null;

	RegisterEntity registerEntity = null;

	public ForgotPasswordDAOImpl() {
		log.info("invoked ForgotDAO :" + this.getClass().getSimpleName());
	}

	public RegisterEntity checkUserDetails(String email, Model model) {

		log.info("inside the DAO under check user details: " + this.getClass().getSimpleName());

		try {
			session = factory.openSession();
			session.beginTransaction();

			String checkEmailExist = "from RegisterEntity where email='" + email + "'";
			log.info("HQL query for the QUERY :" + checkEmailExist);

			Query isEmailValid = session.createQuery(checkEmailExist);
			log.info("Query created :" + isEmailValid);

			registerEntity = (RegisterEntity) isEmailValid.uniqueResult();

			log.info("value from the DB :" + registerEntity);

			return registerEntity;

		} catch (Exception e) {
			session.getTransaction().rollback();
			log.error(e.getMessage(), e);
		} finally {
			log.info("Session closed insode the resetPass finnaly block");
			if (Objects.nonNull(session))
				session.close();
		}

		return null;
	}

	public boolean resetPasswrod(RegisterEntity registerEntity) {
		log.info("inside the DAO under Resetting the password: " + this.getClass().getSimpleName());

		try {

			session = factory.openSession();
			session.beginTransaction();

			String updatePassHQL = "update from RegisterEntity set password='" + registerEntity.getPassword()
					+ "' where email='" + registerEntity.getEmail() + "'";
			log.info("HQL query created to resent the password :" + updatePassHQL);

			String updateDecodedPassHQL = "update from RegisterEntity set decoded='" + registerEntity.getDecodedPass()
					+ "' where email='" + registerEntity.getEmail() + "'";
			log.info("HQL query created to reset the Decodedpassword :" + updateDecodedPassHQL);

			Query passUpdateQuery = session.createQuery(updatePassHQL);
			log.info("Query created under reset password in DAO:" + passUpdateQuery);

			Query decodedPassUpdateQuery = session.createQuery(updateDecodedPassHQL);
			log.info("Query created under reset password in DAO:" + decodedPassUpdateQuery);

			log.info("about to update password and decoded pass..");
			passUpdateQuery.executeUpdate();
			decodedPassUpdateQuery.executeUpdate();
			session.getTransaction().commit();
			log.info("committed unsder reset password with values :" + registerEntity.getPassword());

		} catch (Exception e) {
			session.getTransaction().rollback();
			log.error(e.getMessage(), e);
		} finally {
			log.info("Session closed insode the resetPass finnaly block");
			if (Objects.nonNull(session))
				session.close();
		}
		return false;

	}

	public boolean resetNoOfAttempts(String email, Model model) {

		log.info("invoked the reset attempts method");

		try {
			session = factory.openSession();
			session.beginTransaction();
			Integer noOfAttempts = 0;

			String hql = "update RegisterEntity set noOfAttempts='" + noOfAttempts + "'where email='" + email + "'";
			log.info("HQL prepared to reset the attempts :" + hql);

			Query attemptQuery = session.createQuery(hql);
			log.info("Query created to reset the attempts :" + attemptQuery);

			Integer count = attemptQuery.executeUpdate();
			log.info("this is check about attempt flush : " + count);
			session.getTransaction().commit();

		} catch (Exception e) {
			session.getTransaction().rollback();
			log.error(e.getMessage(), e);
		} finally {
			if (Objects.nonNull(session))
				log.info("Session closed insode the resetPass finnaly block");
			session.close();
		}

		return false;
	}

}
