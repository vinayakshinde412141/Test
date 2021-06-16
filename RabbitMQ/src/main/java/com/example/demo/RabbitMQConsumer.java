package com.example.demo;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumer {

	
	@RabbitListener(queues = "Mobile")
	public void getMessage(Person p) {		
		System.out.println(p.getName());
		
	}
}
