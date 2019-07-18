package ca.gov.bc.open.jrccaccess.autoconfigure.helpers;

import java.util.Random;

public class RandomHelper {

	public static String CHARSET_NUM = "0123456789";

	public static String CHARSET_ALPHALOWER = "abcdefghijklmnopqrstuvwxyz";

	public static String CHARSET_ALPHAUPPER = CHARSET_ALPHALOWER.toUpperCase();

	public static String CHARSET_ALPHA = CHARSET_ALPHAUPPER + CHARSET_ALPHALOWER;

	public static String CHARSET_ALPHANUM = CHARSET_ALPHALOWER + CHARSET_ALPHAUPPER + CHARSET_NUM;

	public static String makeRandomString(int length) {
		return makeRandomString(length, length, CHARSET_ALPHANUM);
	}
	
	public static String makeRandomString(int minLen, int maxLen) {
		return makeRandomString(minLen, maxLen, CHARSET_ALPHANUM);
	}

	public static String makeRandomString(int minLen, int maxLen, String charSet) {
		int len = makeRandomInt(minLen, maxLen);
		String s = "";
		for (int i = 0; i < len; i++) {
			s += charSet.charAt(new Random().nextInt(charSet.length()));
		}
		return s;
	}

	public static Random getRandom() {
		return new Random();
	}

	/** Returns a random integer between min and max (inclusive) */
	public static int makeRandomInt(int min, int max) {
		return (int) makeRandomLong(min, max);
	}

	/** Returns a random long between min and max (inclusive) */
	public static long makeRandomLong(long min, long max) {

		long number = (Math.abs(getRandom().nextLong()) % (max - min + 1)) + min;
		return number;
	}

}
