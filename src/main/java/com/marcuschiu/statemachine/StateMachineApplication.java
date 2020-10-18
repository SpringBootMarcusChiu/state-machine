package com.marcuschiu.statemachine;

import com.marcuschiu.statemachine.statemachine.EventID;
import com.marcuschiu.statemachine.statemachine.StateID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.statemachine.StateMachine;

@SpringBootApplication
public class StateMachineApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(StateMachineApplication.class, args);
	}

	@Autowired
	private StateMachine<StateID, EventID> stateMachine;

	@Override
	public void run(String... args) throws Exception {
		stateMachine.sendEvent(EventID.E1);
		stateMachine.sendEvent(EventID.E2);
		stateMachine.sendEvent(EventID.END);
	}
}
