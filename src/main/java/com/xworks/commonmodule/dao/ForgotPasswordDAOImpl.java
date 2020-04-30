package com.xworks.commonmodule.dao;

import java.util.Objects;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.xworks.commonmodule.entity.RegisterEntity;

@Component
public class ForgotPasswordDAOImpl implements ForgotPasswordDAO {

	@Autowired
	private SessionFactory factory = null;

	Session session = null;

	RegisterEntity registerEntity = null;

	public ForgotPasswordDAOImpl() {
		System.out.println("invoked ForgotDAO :" + this.getClass().getSimpleName());
	}

	public RegisterEntity checkUserDetails(String email, Model model) {

		System.out.println("inside the DAO under check user details: " + this.getClass().getSimpleName());

		try {
			session = factory.openSession();
			session.beginTransaction();

			String checkEmailExist = "from RegisterEntity where email='" + email + "'";
			Query isEmailValid = session.createQuery(checkEmailExist);
			System.out.println("Query created :" + isEmailValid);
			registerEntity = (RegisterEntity) isEmailValid.uniqueResult();

			System.out.println("value got from db NULL or NO: " + registerEntity);

			System.out.println("details from DB :" + registerEntity);

			return registerEntity;

		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();

		} finally {
			System.out.println("Session closed insode the resetPass finnaly block");
			if (Objects.nonNull(session))
				session.close();
		}

		return null;
	}

	public boolean resetPasswrod(RegisterEntity registerEntity) {
		System.out.println("inside the DAO under Resetting the password: " + this.getClass().getSimpleName());

		try {

			session = factory.openSession();
			session.beginTransaction();

			String updatePassHQL = "update from RegisterEntity set password='" + registerEntity.getPassword()
					+ "' where email='" + registerEntity.getEmail() + "'";
			Query passUpdateQuery = session.createQuery(updatePassHQL);
			System.out.println("Query created under reset password in DAO :" + passUpdateQuery);
			System.out.println("about to update password");
			passUpdateQuery.executeUpdate();
			session.getTransaction().commit();
			System.out.println("committed uns=der reset password with values :" + registerEntity.getPassword());

		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();

		} finally {
			System.out.println("Session closed insode the resetPass finnaly block");
			if (Objects.nonNull(session))
				session.close();
		}
		return false;

	}

	public boolean resetNoOfAttempts(String email, Model model) {

		try {
			session = factory.openSession();
			session.beginTransaction();
			Integer noOfAttempts = 0;
			String hql = "update RegisterEntity set noOfAttempts='" + noOfAttempts + "'where email='" + email + "'";
			Query attemptQuery = session.createQuery(hql);
			Integer count = attemptQuery.executeUpdate();
			System.out.println("this is check about attempt flush : " + count);
			session.getTransaction().commit();

		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			if (Objects.nonNull(session))
				session.close();
		}

		return false;
	}

}
