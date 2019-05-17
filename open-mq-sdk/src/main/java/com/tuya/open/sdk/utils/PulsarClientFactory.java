package com.tuya.open.sdk.utils;

import com.tuya.open.sdk.mq.MqAuthentication;
import com.tuya.open.sdk.mq.MqConfigs;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;

/**
 * @author: bilahepan
 * @date: 2019/4/28 下午5:40
 */
public class PulsarClientFactory {

    public static PulsarClient getPulsarClient() {
        return PulsarClientHolder.CLIENT;
    }

    public static class PulsarClientHolder {
        private static final PulsarClient CLIENT = getClient();

        private static PulsarClient getClient() {
            try {
                return PulsarClient.builder().serviceUrl(MqConfigs.serviceUrl).allowTlsInsecureConnection(true)
                        .authentication(new MqAuthentication(MqConfigs.accessId, MqConfigs.accessKey)).build();
            } catch (PulsarClientException e) {
                System.err.println("PulsarClientFactory init error! e=" + e);
                return null;
            }
        }
    }
}