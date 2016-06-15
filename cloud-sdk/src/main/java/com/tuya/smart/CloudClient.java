package com.tuya.smart;

import com.tuya.smart.internal.AtopThirdCloudMobileSignUtil;
import java.util.List;
import java.util.ArrayList;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.NameValuePair;
import org.apache.http.Consts;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import com.tuya.smart.internal.CloudResponseHandler;
import com.tuya.smart.model.RequestMessage;
import com.tuya.smart.model.ResponseMessage;

public class CloudClient {

    private String accessId;
    private String accessKey;
    private String endUri;

    public CloudClient(String accessId, String accessKey,String endUri) {
        this.accessId = accessId;
        this.accessKey = accessKey;
        this.endUri = endUri;
    }

    public ResponseMessage sendRequest(RequestMessage request) {
        
        CloseableHttpClient httpclient = HttpClients.createDefault();

        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("postData", request.getPostData()));

        UrlEncodedFormEntity entity = 
            new UrlEncodedFormEntity(formparams, Consts.UTF_8);

        String url = endUri +"?" + requestToQueryStr(request);
        HttpPost httppost = new HttpPost(url);
        httppost.setEntity(entity);


        CloudResponseHandler handler = new CloudResponseHandler();
            
        try {
            ResponseMessage response = httpclient.execute(httppost, handler);
            return response;
        } catch (Exception e) {
            System.out.println("执行http请求出错");
            e.printStackTrace();
        }
        return null;
    }

    public String requestToQueryStr(RequestMessage request) {
        StringBuilder sb = new StringBuilder("");

        sb.append("a=").append(request.getApi()).append("&");
        sb.append("time=").append(request.getTime()).append("&");
        sb.append("sid=").append(request.getSession()).append("&");

        sb.append("lang=").append(request.getLang()).append("&");

        sb.append("v=").append(request.getApiVersion()).append("&");

        sb.append("os=").append(request.getOs()).append("&");
        sb.append("deviceId=").append(request.getDeviceid()).append("&");

        sb.append("clientid=").append(this.accessId).append("&");

        request.setClientId(this.accessId);

        //生成签名
        String sign=AtopThirdCloudMobileSignUtil.getSign(request, this.accessKey);
        sb.append("sign=").append(sign);

        return sb.toString();
    }



}
