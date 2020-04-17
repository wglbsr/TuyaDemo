
package com.tuya.open.sdk.example;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tuya.open.sdk.mq.AESBase64Utils;
import com.tuya.open.sdk.mq.MqConsumer;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.shade.org.eclipse.util.StringUtil;
import org.tio.websocket.client.WebSocket;
import org.tio.websocket.client.WsClient;
import org.tio.websocket.client.config.WsClientConfig;


public class YouniIotTuyaConsumer {
    static final String PLUSAR_URL = "pulsar+ssl://mqe.tuyacn.com:7285/";
    static final String TUYA_ACCESS_ID = "evxpxfww3wmtwctmnuvy";
    static final String TUYA_ACCESS_KEY = "gmwmfjh9n4kexwf3vvjmxxtetm97dghn";
    static final String TOKEN = "4f0374f2979d967b15f4aca37c177b2cd8e3fa9308bfd11d13b186be58e73e6b";
    static final Logger logger = Logger.getLogger(YouniIotTuyaConsumer.class);

    public static void main(String[] args) throws Exception {
        BasicConfigurator.configure();
        final String token = System.getProperty("token", TOKEN);
        final String showContent = System.getProperty("showContent", "0");
        final boolean showContentFlag = !showContent.equals("0");
        final String websocketUrl = System.getProperty("url", "ws://api.smarthome.yn-iot.cn:8200?token=" + token);
        logger.info("**************************************************websocket url[" + websocketUrl + "]**************************************************");
        WebSocket webSocket = initWebSocket(websocketUrl);
        if (webSocket == null) {
            logger.info("无法建立websocket连接！");
            System.exit(0);
        }
        MqConsumer mqConsumer = MqConsumer.build().serviceUrl(PLUSAR_URL).accessId(TUYA_ACCESS_ID).accessKey(TUYA_ACCESS_KEY)
                .maxRedeliverCount(3).messageListener(message -> {
                            String msg = pack(message, token, showContentFlag);
                            if (!StringUtil.isBlank(msg)) {
                                webSocket.send(pack(message, token, showContentFlag));
                            }
                        }
                );
        mqConsumer.start();
    }

    private static WebSocket initWebSocket(String url) {
        WsClient echo;
        try {
            echo = WsClient.create(
                    url,
//                    "wss://echo.websocket.org",
                    new WsClientConfig(
                            e -> logger.info("websocket connection established！"),
                            e -> {
//                                logger.info(String.format("received message: %s\n", e.data.getWsBodyText()));
                            },
                            e -> logger.info(String.format("closed: %d, %s, %s\n", e.code, e.reason, e.wasClean)),
                            e -> logger.error(String.format("error occurred: %s", e.msg)),
                            Throwable::printStackTrace));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


        try {
            return echo.connect();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * {
     * "cmd": "feedback",
     * "manufacturer": "tuya",
     * "msg": {
     * <p>
     * },
     * "token": "I6017w3f981we29fwlkej45wPQmZ6ew3570QP56wembzv"
     * }
     *
     * @param message
     * @return
     */
    private static String pack(Message message, String token, boolean showContent) {
        String jsonMessage = new String(message.getData());
        MessageVO vo = JSON.parseObject(jsonMessage, MessageVO.class);
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject();
            String realMessage = AESBase64Utils.decrypt(vo.getData(), TUYA_ACCESS_KEY.substring(8, 24));
            if (StringUtil.isBlank(realMessage)) {
                logger.error("内容为空");
                return null;
            }
            jsonObject.put("token", token);
            jsonObject.put("manufacturer", "tuya");
            jsonObject.put("cmd", "feedback");
            jsonObject.put("msg", JSONObject.parseObject(realMessage));
            if (showContent) {
                logger.info(String.format("发送内容:%s", realMessage));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return jsonObject.toJSONString();
    }
}
