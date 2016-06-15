# cloud-sdk, 涂鸦云对云API java-sdk


## 项目简介
cloud-sdk是涂鸦云对云API的java封装, 对http方式提供的接口进行了简单的封装,包括
签名, 基础参数设置等. 方便使用java的客户进行接口调用.

API列表见: [Tuya Open API](http://api.tuya.com/)
http方式调用指南: [Tuya docs](http://docs.tuya.com/develop/cloudapi/api/)

## SDK使用

```java

    //创建 cloud client(accessId, accessKey由tuya这边提供)
    String endUri = "http://a1.tuyacn.com/api.json";
    String accessId = "xxxx";
    String accessKey = "xxxxxxxxxxx";
    TuyaCloudClient client = new TuyaCloudClient(accessId,accessKey, endUri);

    //新建请求对象
    RequestMessage request = new RequestMessage();
    request.setApi("s.m.dev.list.group.list");
    request.setApiVersion("1.0");
    request.setOs("iOs");
    request.setDeviceid("ppstrong");
    request.setLang("zh");
    request.setSession("asdfasdf");

    //如果接口需要入参,则能过 map设置
    Map<String,String> params = new HashMap<String,String>();
    params.put("uid","mmmmmmmmmm");
    params.put("countryCode","86");
    params.put("passwd","faint");


    //发送请求, 得到响应
    //如果是成功请求, 则response里的result会是个JSON对象封装的请求结果.
    //如果失败, 请查看errorMsg和errorCode,进行相应的处理.
    ResponseMessage response=client.sendRequest(request);
    if (response.isSuccess()) {
        String result = JSONObject.toJSONString( response.getResult(),true);
        System.out.println(result);
    } else {
        String errorCode = response.getErrorCode();
        String errorMsg = response.getErrorMsg();
        System.out.println(errorMsg);
    }


```


