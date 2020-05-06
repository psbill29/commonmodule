package com.xworks.commonmodule.service;

import java.security.SecureRandom;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class RandomPassGenerator {

	private Logger log = Logger.getLogger(RandomPassGenerator.class);

	public RandomPassGenerator() {
		log.info(this.getClass().getSimpleName() + ":created ");
	}

	public String passGenerator() {

		log.info(this.getClass().getSimpleName() + ":inside the password generator");

		StringBuilder decodedPass = new StringBuilder();
		String chars = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm";
		int length = 8;

		SecureRandom random = new SecureRandom();
		for (int i = 0; i < length; i++) {
			int Index = random.nextInt(chars.length());
			decodedPass.append(chars.charAt(Index));
		}
		log.info("system generated password is :" + decodedPass);

		return decodedPass.toString();

	}

}
