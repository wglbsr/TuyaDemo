import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

public class MD5Utils {

	private static Logger logger = LoggerFactory.getLogger(MD5Utils.class);

	public static String getMD5(byte[] source) {
		String s = null;
		char[] hexDigits = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
				'f' };

		try {
			MessageDigest e = MessageDigest.getInstance("MD5");
			e.update(source);
			byte[] tmp = e.digest();
			char[] str = new char[32];
			int k = 0;

			for (int i = 0; i < 16; ++i) {
				byte byte0 = tmp[i];
				str[k++] = hexDigits[byte0 >>> 4 & 15];
				str[k++] = hexDigits[byte0 & 15];
			}

			s = new String(str);
		} catch (Exception e) {
			logger.warn("MD5加密异常!", e);
		}

		return s;
	}
}
