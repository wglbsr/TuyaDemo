package com.tuya.open.sdk.utils;

import com.tuya.open.sdk.mq.MqConfigs;
import com.tuya.open.sdk.mq.MqEnv;
import org.apache.pulsar.client.api.*;
import java.util.concurrent.*;

/**
 * @author: bilahepan
 * @date: 2019/4/16 下午6:41
 */
public class PulsarConsumerPoolFactory {

    public static ConcurrentHashMap<Integer, Consumer> getConsumerPool() {
        return InitializingConsumerPool.pool;
    }


    private static class InitializingConsumerPool {
        private static final ConcurrentHashMap<Integer, Consumer> pool = new ConcurrentHashMap<Integer, Consumer>() {{
            for (int i = 1; i <= MqConfigs.CONSUMER_NUM; i++) {
                put(i, getConsumerInstance());
            }
        }};


        private static Consumer getConsumerInstance() {
            try {
                return PulsarClientFactory.getPulsarClient().newConsumer().topic(String.format("%s/out/%s", MqConfigs.accessId, MqEnv.PROD.getValue()))
                        .subscriptionName(String.format("%s-sub", MqConfigs.accessId)).subscriptionType(SubscriptionType.Failover)
                        .deadLetterPolicy(DeadLetterPolicy.builder().maxRedeliverCount(MqConfigs.MAX_REDELIVER_COUNT).build()).subscribe();
            } catch (PulsarClientException e) {
                System.err.println("PulsarConsumerPoolFactory init error! e=" + e);
                return null;
            }
        }
    }

}