
package com.tuya.open.sdk.example;

import org.apache.pulsar.client.impl.TopicMessageIdImpl;

import com.tuya.open.sdk.mq.MqConsumer;

public class ConsumerExample {

	public static void main(String[] args) throws Exception {
		String url = "pulsar+ssl://mqe.tuyacn.com:7285/";
		String accessId = "";
		String accessKey = "";

		MqConsumer mqConsumer = MqConsumer.build().serviceUrl(url).accessId(accessId).accessKey(accessKey)
				.maxRedeliverCount(3).messageListener(message -> {
					System.out.println("Message received:" + new String(message.getData()) + ",seq="
							+ message.getSequenceId() + ",time=" + message.getPublishTime() + ",consumed time="
							+ System.currentTimeMillis() + ",partition="
							+ ((TopicMessageIdImpl) message.getMessageId()).getTopicPartitionName());
				});
		mqConsumer.start();
	}
}
