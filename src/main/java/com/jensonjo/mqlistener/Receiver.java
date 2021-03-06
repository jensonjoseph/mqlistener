package com.jensonjo.mqlistener;

import java.nio.charset.StandardCharsets;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

/**
 * Created by jensonkakkattil on May, 2018
 */
@Component
public class Receiver implements ChannelAwareMessageListener {

    /**
     * Callback for processing a received Rabbit message.
     * <p>Implementors are supposed to process the given Message,
     * typically sending reply messages through the given Session.
     *
     * @param message the received AMQP message (never <code>null</code>)
     * @param channel the underlying Rabbit Channel (never <code>null</code>)
     * @throws Exception Any.
     */
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {

        String str = new String(message.getBody(), StandardCharsets.UTF_8);
        System.out.println("***** Received <" + str + "> *****");
        System.out.println("***** Channel : " + channel.toString() + " *****");
    }

}
