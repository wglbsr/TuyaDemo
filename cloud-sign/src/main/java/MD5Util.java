import java.security.MessageDigest;
import java.text.ParseException;

public class MD5Util {

	public static String getMD5(byte[] source) {
		String s = null;
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(source);
			byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
			// 用字节表示就是 16 个字节
			char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
			// 所以表示成 16 进制需要 32 个字符
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
				// 转换成 16 进制字符的转换
				byte byte0 = tmp[i]; // 取第 i 个字节
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
				// >>> 为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			s = new String(str); // 换后的结果转换为字符串

		} catch (Exception e) {

		}
		return s;
	}

	public static String getMD5(String inputStr) {
        return getMD5(inputStr.getBytes());
//		String md5Str = inputStr;
//		if (inputStr != null) {
//			MessageDigest md;
//			try {
//				md = MessageDigest.getInstance("MD5");
//				md.update(inputStr.getBytes());
//				BigInteger hash = new BigInteger(1, md.digest());
//				md5Str = hash.toString(16);
//				if ((md5Str.length() % 2) != 0) {
//					md5Str = "0" + md5Str;
//				}
//			} catch (NoSuchAlgorithmException e) {
//				logger.error("error message", e);
//			}
//		}
//		return md5Str;
	}

	public static void main(String[] args) throws ParseException {
//        String bb  = "31" + "||" + "5a/b7/55/ed/66/5ab755ed667e662f0e0e4a5cd6c23fd0/2014-12-03/B16D2FE0-76A3-42A6-84DE-1C4181B81412.jpg";
//				String md5value = MD5Util.getMD5(bb.getBytes());
//				System.out.println(md5value);
//
//        String temp="1.1";
//        if(temp.compareTo("1.3") >=0){
//            System.out.println("sfadsfsfdd");
//        }

        String abc="1112221212";
        String ccc=getMD5(abc);
        System.out.println(ccc);

//        String text="5a/7d/6a/07/01/5a7d6a07014ae2a9dee03cf74a98236c/2014-10-04/53CC3D8E-7A50-4C15-9574-33237487FB05.JPG";
//        String md5=getMD5(text);
//        System.out.println(md5);
//        md5=getMD5(text.getBytes());
//        System.out.println(md5);



//		for (int i = 0; i < 100; i++) {
//			int number = new Random().nextInt(5) + 1;
//            System.out.println("number="+number);
//		}
//        String version1="1.1.2";
//        String version2="1.1.3";
//        boolean t=version1.compareTo(version2) >0;
//        System.out.print(t);
	}
}
