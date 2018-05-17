package com.jensonjo.mqlistener;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;

/**
 * Created by jensonkakkattil on May, 2018
 */
public class Runner implements CommandLineRunner {

    private final Receiver receiver;

    public Runner(Receiver receiver) {
        this.receiver = receiver;
    }

    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     * @throws Exception on error
     */
    @Override
    public void run(String... args) throws Exception {

    }
}
