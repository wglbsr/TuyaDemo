import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class SignUtils {

	private static final Logger logger		= LoggerFactory.getLogger(SignUtils.class);

	public static final String	SIGN		= "sign";

	public static final String	EQUALS		= "=";

	public static final String	SPLITTER	= "||";

	public static boolean checkSign(JSONObject jsonObject, String appSecret) {
		String signInput = createSignInput(jsonObject, appSecret);
		String sign = MD5Utils.getMD5(signInput.getBytes());
		String comparedSign = jsonObject.getString(SIGN);
		if (comparedSign.equals(sign)) {
			return true;
		} else {
			logger.warn("sign not equals,compared=" + comparedSign + ",computed=" + sign + ",signInput=" + signInput);
			return false;
		}
	}

	private static String createSignInput(JSONObject jsonObject, String appSecret) {
		TreeMap<String, String> params = new TreeMap<String, String>();
		Set<Map.Entry<String, Object>> entrySet = jsonObject.entrySet();
		for (Map.Entry<String, Object> entry : entrySet) {
			if (entry.getValue() != null && !entry.getKey().equals(SIGN)) {
				params.put(entry.getKey(), entry.getValue().toString());
			}
		}

		StringBuilder str = new StringBuilder();
		Set<String> keySet = params.keySet();
		Iterator<String> iter = keySet.iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			if (StringUtils.isBlank(params.get(key))) {
				continue;
			}
			str.append(key);
			str.append(EQUALS);
			str.append(params.get(key));
			str.append(SPLITTER);
		}
		str.append(appSecret);
		return str.toString();
	}

}
