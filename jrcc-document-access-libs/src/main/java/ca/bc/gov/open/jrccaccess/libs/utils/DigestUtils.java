package ca.bc.gov.open.jrccaccess.libs.utils;

import jakarta.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class provides message digest algorithm tool.
 */
public class DigestUtils {

	/**
	 * Provide MD5 message-digest algorithm
	 * @param content the content needs to be processed.
	 * @return the result after applying MD5 on the content
	 */
	public static String computeMd5(String content) {

		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(content.getBytes(StandardCharsets.UTF_8));
			return DatatypeConverter.printHexBinary(md.digest());
		} catch (NoSuchAlgorithmException e) {
			// can't happen
			return null;
		}

	}

	private DigestUtils(){
		throw new IllegalStateException("DigestUtils class");
	}
}
