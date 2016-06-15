package com.tuya.smart.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import java.util.Date;
import com.alibaba.fastjson.JSONObject;
import java.util.Map;

public class RequestMessage {

    private String api;

	private String apiVersion;

    private Map<String,String> params;

	private String sign;								

	private String session;						

	private String deviceid;

	private String os;	

	private String lang= "zh-Hans";

    private long time;

    private String clientId;

    public RequestMessage() {
        long time = new Date().getTime() / 1000;
        this.time = time;
    }


    public String getPostData() {
        if (params == null || params.size() == 0 ) 
            return "";

        JSONObject json = new JSONObject();
        for (String key : params.keySet()) {
            json.put(key, params.get(key));
        }
        return json.toJSONString();
    }


    public void setApi(String api) {
        this.api=api;
    }
    public String getApi() {
        return this.api;
    }


    public void setApiVersion(String apiVersion) {
        this.apiVersion=apiVersion;
    }
    public String getApiVersion() {
        return this.apiVersion;
    }


    public void setParams(Map<String,String> params) {
        this.params=params;
    }
    public Map<String,String> getParams() {
        return this.params;
    }


    public void setSign(String sign) {
        this.sign=sign;
    }
    public String getSign() {
        return this.sign;
    }


    public void setSession(String session) {
        this.session=session;
    }
    public String getSession() {
        return this.session;
    }


    public void setDeviceid(String deviceid) {
        this.deviceid=deviceid;
    }
    public String getDeviceid() {
        return this.deviceid;
    }


    public void setOs(String os) {
        this.os=os;
    }
    public String getOs() {
        return this.os;
    }




    public void setLang(String lang) {
        this.lang=lang;
    }
    public String getLang() {
        return this.lang;
    }


    public void setTime(long time) {
        this.time=time;
    }
    
    public long getTime() {
        return this.time;
    }

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

    public void setClientId(String clientId) {
        this.clientId=clientId;
    }
    public String getClientId() {
        return this.clientId;
    }

}
