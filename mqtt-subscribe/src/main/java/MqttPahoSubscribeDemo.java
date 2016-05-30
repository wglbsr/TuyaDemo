import com.alibaba.fastjson.JSONObject;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.net.InetAddress;

public class MqttPahoSubscribeDemo {

	private static final String	DATA_POINT_KEY	= "dps";

	private static final String	KEY_DATA		= "data";

	public static void main(String[] args) {
		try {
			String appKey = "appKey";
			String appSecret = "appSecret";
			String userName = "cloud_appKey";
			InetAddress netAddress = InetAddress.getLocalHost();
			String clientId = userName + "_" + netAddress.getHostAddress();
			MqttConnectOptions options = new MqttConnectOptions();
			options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
			String password = MD5Utils.getMD5((appKey + MD5Utils.getMD5(appSecret.getBytes())).getBytes()).substring(8,
					24);
			options.setUserName(userName);
			options.setPassword(password.toCharArray());
			options.setConnectionTimeout(10);
			options.setKeepAliveInterval(30);
			options.setCleanSession(true);
			MqttClient mqttClient = new MqttClient("tcp://mq.gw.airtakeapp.com:1883", clientId,
					new MemoryPersistence());
			mqttClient.setCallback(new MessageListener(appSecret));
			mqttClient.setTimeToWait(1000);
			mqttClient.connect(options);
			int[] Qos = { 1 };
			String[] topic1 = { "$queue/device/cloud/out/" + appKey };
			mqttClient.subscribe(topic1, Qos);

			synchronized (MqttPahoSubscribeDemo.class) {
				try {
					MqttPahoSubscribeDemo.class.wait();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public static class MessageListener implements MqttCallback {

		private String appSecret;

		public MessageListener(String appSecret) {
			this.appSecret = appSecret;
		}

		public void connectionLost(Throwable throwable) {
			//注意:需要自己处理连接丢失情况,一般需要进行重连
			throwable.printStackTrace();
		}

		public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
			try {
				String message = new String(mqttMessage.getPayload(), "utf-8");
				JSONObject payload = JSONObject.parseObject(message);

				if (!SignUtils.checkSign(payload, appSecret)) {
					System.out.println("process failed,msg=" + mqttMessage);
					return;
				}

				String decryptMessageData = AESUtils.decrypt(payload.getString(KEY_DATA), appSecret.substring(8, 24));
				JSONObject data = JSONObject.parseObject(decryptMessageData);
				System.out.println("process msg=" + message + ",dps=" + data.getString(DATA_POINT_KEY));
			} catch (Throwable t) {
				t.fillInStackTrace();
			}
			return;
		}

		public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
		}
	}
}
