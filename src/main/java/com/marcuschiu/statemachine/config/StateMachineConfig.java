package com.marcuschiu.statemachine.config;

import com.marcuschiu.statemachine.statemachine.EventID;
import com.marcuschiu.statemachine.statemachine.StateID;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;

@Configuration
@EnableStateMachine
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<StateID, EventID> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<StateID, EventID> config) throws Exception {
        config.withConfiguration()
                .autoStartup(true) // or call StateMachine<String, String> stateMachine.start();
                .listener(listener());
    }

    @Override
    public void configure(StateMachineStateConfigurer<StateID, EventID> states) throws Exception {
        states.withStates()
                .initial(StateID.SI)
                .end(StateID.SF)
                .states(EnumSet.allOf(StateID.class));
    }

    @Bean
    public Action<StateID, EventID> initAction1() {
        return ctx -> System.out.println("ACTION1: " + ctx.getTarget().getId());
    }

    @Bean
    public Action<StateID, EventID> initAction2() {
        return ctx -> System.out.println("ACTION2: " + ctx.getTarget().getId());
    }

    @Bean
    public Action<StateID, EventID> initAction3() {
        return ctx -> System.out.println("ACTION3: " + ctx.getTarget().getId());
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<StateID, EventID> transitions) throws Exception {
        transitions
                .withExternal()
                .source(StateID.SI).target(StateID.S1).event(EventID.E1).action(initAction1())
                .and().withExternal()
                .source(StateID.S1).target(StateID.S2).event(EventID.E2).action(initAction2())
                .and().withExternal()
                .source(StateID.S2).target(StateID.SF).event(EventID.END).action(initAction3());
    }

    @Bean
    public StateMachineListener<StateID, EventID> listener() {
        return new StateMachineListenerAdapter<StateID, EventID>() {
            @Override
            public void stateChanged(State<StateID, EventID> from, State<StateID, EventID> to) {
                System.out.println("State change to " + to.getId());
            }
        };
    }
}
