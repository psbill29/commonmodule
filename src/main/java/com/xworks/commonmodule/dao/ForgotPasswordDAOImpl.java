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

	public boolean resetPasswordAndCounts(RegisterEntity registerEntity) {
		log.info("inside the DAO under Resetting the password: " + this.getClass().getSimpleName());

		try {

			session = factory.openSession();
			session.beginTransaction();

			String updatePassHQL = "update from RegisterEntity set password='" + registerEntity.getPassword()
					+ "',decoded='" + registerEntity.getDecodedPass() + "',noOfAttempts='"
					+ registerEntity.getNoOfAttempts() + "' where email='" + registerEntity.getEmail() + "'";
			log.info("HQL query created to resent the password :" + updatePassHQL);

			Query passUpdateQuery = session.createQuery(updatePassHQL);
			log.info("Query created under reset password and number of attempts in DAO:" + passUpdateQuery);

			log.info("about to update password, decoded pass and no.of attempts..");
			passUpdateQuery.executeUpdate();
			session.getTransaction().commit();
			log.info("committed unsder resetPasswordAndCounts with values :" + registerEntity.getPassword());

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

}
