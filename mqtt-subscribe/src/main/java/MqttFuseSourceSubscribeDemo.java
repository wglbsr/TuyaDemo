import com.alibaba.fastjson.JSONObject;
import org.fusesource.mqtt.client.*;

import java.net.InetAddress;

public class MqttFuseSourceSubscribeDemo {

	private static final String	DATA_POINT_KEY	= "dps";

	private static final String	KEY_DATA		= "data";

	public static void main(String[] args) {
		try {
			String appKey = "appKey";
			String appSecret = "appSecret";
			String userName = "cloud_appKey";
			MQTT mqtt = new MQTT();
			//设置mqtt broker的ip和端口  
			mqtt.setHost("tcp://mq.gw.airtakeapp.com:1883");
			//连接前清空会话信息  
			mqtt.setCleanSession(true);
			//设置重新连接的次数  
			mqtt.setReconnectAttemptsMax(-1);
			//设置重连的间隔时间  
			mqtt.setReconnectDelay(2000);
			//设置心跳时间  
			mqtt.setKeepAlive((short) 60);
			//设置缓冲的大小  
			String password = MD5Utils.getMD5((appKey + MD5Utils.getMD5(appSecret.getBytes())).getBytes()).substring(8,
					24);
			InetAddress netAddress = InetAddress.getLocalHost();
			mqtt.setClientId(userName + "_" + netAddress.getHostAddress());
			mqtt.setUserName(userName);
			mqtt.setPassword(password);

			//获取mqtt的连接对象BlockingConnection  
			final FutureConnection connection = mqtt.futureConnection();
			connection.connect();
			connection.subscribe(new Topic[] { new Topic("$queue/device/cloud/out/" + appKey, QoS.AT_LEAST_ONCE) });
			while (true) {
				try {
					Future<Message> futureMessage = connection.receive();
					Message message = futureMessage.await();

					String payload = new String(message.getPayload(), "utf-8");
					JSONObject payloadJSON = JSONObject.parseObject(payload);
					if (!SignUtils.checkSign(payloadJSON, appSecret)) {
						System.out.println("process failed,msg=" + message);
						return;
					}

					String decryptMessageData = AESUtils.decrypt(payloadJSON.getString(KEY_DATA),
							appSecret.substring(8, 24));
					JSONObject data = JSONObject.parseObject(decryptMessageData);
					System.out.println("process msg=" + message + ",dps=" + data.getString(DATA_POINT_KEY));
				} catch (Throwable t) {
					t.fillInStackTrace();
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
