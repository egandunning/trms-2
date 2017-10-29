package com.revature.crypto;

import java.security.Key;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Hash {

	/**
	 * Hash a password with PBKDF2(Password Based Key Derivation Function)
	 * using a fixed salt.
	 * @param password to hash.
	 * @return the hashed password
	 */
	public static byte[] pbkdf2(char[] password) {
		
		KeySpec keySpec = new PBEKeySpec(password, new byte[1], 20, 128);
		Key key = null;
		try {
			key = new SecretKeySpec(
				SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
				.generateSecret(keySpec)
				.getEncoded(), "AES");
		} catch(Exception e) {
			return null;
		}		
		return key.getEncoded();
	}
}
