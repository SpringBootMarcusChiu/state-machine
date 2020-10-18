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

//    @Bean
//    public Action<StateID, EventID> stateAction() {
//        return ctx -> System.out.println("STATE ACTION: " + ctx.getTarget().getId());
//    }
//
//    @Bean
//    public Action<StateID, EventID> stateErrorAction() {
//        return ctx -> System.out.println("ERROR ACTION: " + ctx.getSource().getId() + " " + ctx.getException());
//    }

    @Bean
    public Action<StateID, EventID> stateEntryAction() {
        return ctx -> System.out.println("STATE ENTRY ACTION: " + ctx.getTarget().getId());
    }

    @Bean
    public Action<StateID, EventID> stateDoAction() {
        return ctx -> System.out.println("STATE DO ACTION: " + ctx.getTarget().getId());
    }

    @Bean
    public Action<StateID, EventID> stateExitAction() {
        return ctx -> System.out.println("STATE EXIT ACTION: " + ctx.getSource().getId() + " -> " + ctx.getTarget().getId());
    }

    @Override
    public void configure(StateMachineStateConfigurer<StateID, EventID> states) throws Exception {
        states.withStates()
                .initial(StateID.SI)
//                .state(StateID.S1, stateAction(), stateErrorAction()) // ERROR calling stateErrorAction() for no reason
                .stateEntry(StateID.S1, stateEntryAction())
                .stateDo(StateID.S1, stateDoAction())
                .stateExit(StateID.S1, stateExitAction())
                .end(StateID.SF)
                .states(EnumSet.allOf(StateID.class));
    }

    @Bean
    public Action<StateID, EventID> transitionAction() {
        return ctx -> System.out.println("TRANSITION ACTION: " + ctx.getTarget().getId());
    }

    @Bean
    public Action<StateID, EventID> transitionErrorAction() {
        return ctx -> System.out.println("TRANSITION ERROR ACTION: " + ctx.getTarget().getId());
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<StateID, EventID> transitions) throws Exception {
        transitions
                .withExternal()
                .source(StateID.SI).target(StateID.S1).event(EventID.E1).action(transitionAction(), transitionErrorAction())
                .and().withExternal()
                .source(StateID.S1).target(StateID.S2).event(EventID.E2).guardExpression("0 > 1")
                .and().withExternal()
                .source(StateID.S2).target(StateID.SF).event(EventID.END);
    }

    @Bean
    public StateMachineListener<StateID, EventID> listener() {
        return new StateMachineListenerAdapter<StateID, EventID>() {
            @Override
            public void stateChanged(State<StateID, EventID> from, State<StateID, EventID> to) {
                System.out.println("LISTENER: state changed to " + to.getId());
            }
        };
    }
}
