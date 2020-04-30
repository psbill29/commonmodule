package com.xworks.commonmodule.dao;

import java.io.Serializable;
import java.util.Objects;

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
	@Autowired
	private SessionFactory factory;

	RegisterEntity registerEntity = null;

	public boolean saveUser(RegisterEntity registerEntity) {
		System.out.println("invoked saveUser inside the RegisterDAOImpl ....");
		Session session = null;
		try {
			session = factory.openSession();
			session.beginTransaction();
			Serializable serializable = session.save(registerEntity);
			if (Objects.nonNull(serializable)) {
				System.out.println("registration  data saved to DB...");
			} else {
				System.out.println("registration data not save to DB..." + "\n");
			}
			session.getTransaction().commit();
		} catch (HibernateException h) {
			session.getTransaction().rollback();
			h.printStackTrace();
		} finally {
			if (Objects.nonNull(session)) {
				session.close();
			}
		}
		return false;

	}

	public RegisterEntity validateUserID(RegisterDTO registerDTO, Model model) {

		System.out.println("invoked validateUserID....");
		Session session = null;
		RegisterEntity registerEntity = null;
		try {
			session = factory.openSession();
			session.beginTransaction();

			String hqlFetchByUserId = "from RegisterEntity where user_id = '" + registerDTO.getUser_id() + "'";
			Query<RegisterEntity> emailQuery = session.createQuery(hqlFetchByUserId);
			registerEntity = emailQuery.uniqueResult();
			System.out.println("Query created and got results from the DB under validate by userid :" + registerEntity);
			return registerEntity;

		} catch (HibernateException h) {
			session.getTransaction().rollback();
			h.printStackTrace();
		} finally {
			if (Objects.nonNull(session)) {
				session.close();
			}
		}
		return null;

	}

	public RegisterEntity validateEmail(RegisterDTO registerDTO, Model model) {
		System.out.println("invoked validateEmail....");
		Session session = null;
		RegisterEntity registerEntity = null;
		try {
			session = factory.openSession();

			String hqlFetchByEmail = "from RegisterEntity where email = '" + registerDTO.getEmail() + "'";
			System.out.println("HQl query created : ");
			Query<RegisterEntity> emailQuery = session.createQuery(hqlFetchByEmail, RegisterEntity.class);
			registerEntity = (RegisterEntity) emailQuery.uniqueResult();
			System.out.println("Query created and got results from the DB under validate by email:" + registerEntity);
			if (Objects.isNull(registerEntity)) {
				System.out.println("No data found :" + registerEntity);
				return null;
			}
			return registerEntity;
		} catch (HibernateException h) {
			session.getTransaction().rollback();
			h.printStackTrace();
		} finally {
			if (Objects.nonNull(session)) {
				session.close();
			}
		}
		return null;
	}

	public Integer checkAttempts(String loginEmail) {
		System.out.println("inside the checkAttempts: ");
		Session session = null;
		Integer attemptCount = 0;
		try {
			session = factory.openSession();
			session.beginTransaction();

			// to check attempts more than 3 times

			String attemptsHql = "from RegisterEntity where email='" + loginEmail + "'";

			Query attemptQuery = session.createQuery(attemptsHql);
			registerEntity = (RegisterEntity) attemptQuery.uniqueResult();
			System.out.println("value of register entity before assigning to attemptCount :" + registerEntity);
			if (Objects.isNull(registerEntity)) {
				System.out.println("attempts value inside ISNULL method under DAO :" + attemptCount);
				return 0;
			}

			attemptCount = registerEntity.getNoOfAttempts();
			System.out.println("value of attempts under DAO before ISNULL :" + attemptCount);

			System.out.println("value of count for checkAattempts :" + attemptCount);
			return attemptCount;

		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();

		} finally {
			if (Objects.nonNull(session)) {
				session.close();
				System.out.println("session closed in finnaly fro add attempts");
			}
		}
		return attemptCount;

	}

	public Integer addAttempts(String loginEmail, int noOfAttempts) {

		System.out.println("inside the addAttempts: ");
		Session session = null;

		try {
			session = factory.openSession();
			session.beginTransaction();

			String hqlUpdateAttempt = "update RegisterEntity set noOfAttempts='" + noOfAttempts + "' where email='"
					+ loginEmail + "'";
			Query query = session.createQuery(hqlUpdateAttempt);
			Integer totalAttempts = query.executeUpdate();
			System.out.println("return query true or false " + query);
			System.out.println("loginemail: " + loginEmail + "\n" + "nnoOfAttempts :" + totalAttempts);
			session.getTransaction().commit();
			return totalAttempts;

		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();

		} finally {
			if (Objects.nonNull(session)) {
				session.close();
				System.out.println("session closed in finnaly fro add attempts");
			}
		}
		return null;
	}

}
