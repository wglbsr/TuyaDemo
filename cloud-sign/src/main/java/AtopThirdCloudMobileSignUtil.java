import org.apache.commons.lang.StringUtils;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

/**
 * 第三方云的手机签名
 */
public class AtopThirdCloudMobileSignUtil {

	private static TreeMap<String, String> paramsBuild(ApiRequestDO apiRequestDo) {
		TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("a", apiRequestDo.getApi());
        params.put("v", apiRequestDo.getApiContextDo().getApiVersion());
        params.put("lat", apiRequestDo.getApiContextDo().getLat());
        params.put("lon", apiRequestDo.getApiContextDo().getLon());
        params.put("lang", apiRequestDo.getApiContextDo().getLang());
        params.put("deviceId", apiRequestDo.getApiContextDo().getDeviceid());
        params.put("appVersion", apiRequestDo.getApiContextDo().getAppVersion());
        params.put("ttid", apiRequestDo.getApiContextDo().getTtid());
        params.put("os", apiRequestDo.getApiContextDo().getOs());
        params.put("clientId", apiRequestDo.getAppInfoDo().getClientId());

		if (StringUtils.isNotBlank(apiRequestDo.getN4h5())) {
			params.put("n4h5", apiRequestDo.getN4h5());
		}
		params.put("time", apiRequestDo.getT());
		if (StringUtils.isNotBlank(apiRequestDo.getSession())) {
			params.put("sid", apiRequestDo.getSession());
		}
		if (StringUtils.isNotBlank(apiRequestDo.getData())) {
			params.put("postData", apiRequestDo.getData());
		}
		return params;
	}

	private static String signAssembly(TreeMap<String, String> params, String secretKey) {
		//LinkedHashMap 使用LinkedHashMap保持顺序
		StringBuilder str = new StringBuilder();
        str.append(secretKey);
		Set<String> keySet = params.keySet();
		Iterator<String> iter = keySet.iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			if (StringUtils.isBlank(params.get(key))) {
				continue;
			}
			str.append(key);
			str.append("=");
			str.append(params.get(key));
			str.append("|");
		}
        String strValue = str.toString();
        strValue = strValue.substring(0, (strValue.length() - 1));
        return strValue;
	}

	private static String getSign(ApiRequestDO apiRequestDo, String secretKey) {
		TreeMap<String, String> params = paramsBuild(apiRequestDo);
		String signString = signAssembly(params, secretKey);
		return signString;
	}

	public static boolean signValidate(ApiRequestDO apiRequestDo) {
		String secretKey = apiRequestDo.getAppInfoDo().getHashKey();
		String signString = getSign(apiRequestDo, secretKey);
		String sign = MD5Util.getMD5(signString.getBytes());
		if (apiRequestDo.getOpenSign() && !sign.equals(apiRequestDo.getSign().trim())) {
			return false;
		}
		return true;
	}


    public static void main(String[] args) {
        ApiRequestDO apiRequestDo = new ApiRequestDO();

        apiRequestDo.setApi("s.m.user.code.login");
        ApiContextDO apiContextDO = new ApiContextDO();
        apiContextDO.setApiVersion("1.0");
        apiContextDO.setLat("1111");
        apiContextDO.setLon("3333");
        apiContextDO.setLang("cn");
        apiContextDO.setDeviceid("cloud");
        apiContextDO.setAppVersion("1.0");
        apiContextDO.setTtid("ios");
        apiContextDO.setOs("IOS");


        AppInfoDO appInfoDO = new AppInfoDO();
        appInfoDO.setClientId("1cc04144c032p440");
        apiRequestDo.setAppInfoDo(appInfoDO);

        apiRequestDo.setT("1435047037");
        apiRequestDo.setSession("abcededdddd");
        apiRequestDo.setData("{\"countryCode\":\"86\",\"mobile\":\"13233433434\",\"code\":\"2345\"}");

        apiRequestDo.setApiContextDo(apiContextDO);

        String s = getSign(apiRequestDo, "1cc04144c032p440");
        String sign = MD5Util.getMD5(s.getBytes());
        System.out.println(sign);
    }


}
