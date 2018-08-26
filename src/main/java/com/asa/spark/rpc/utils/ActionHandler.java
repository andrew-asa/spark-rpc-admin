package com.asa.spark.rpc.utils;

import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author andrew_asa
 * @date 2018/8/26.
 */
public class ActionHandler implements SignalHandler {

    private Signal signal;

    private SignalHandler prevHandler;

    private List<Action> actions = Collections.synchronizedList(new java.util.LinkedList<Action>());


    public ActionHandler(Signal signal) {

        this.signal = signal;
        prevHandler = Signal.handle(signal, this);
    }

    @Override
    public void handle(Signal signal) {

        Signal.handle(signal, prevHandler);
        final boolean[] escalate = {true};
        actions.forEach(new Consumer<Action>() {

            @Override
            public void accept(Action action) {

                if (action.action()) {
                    escalate[0] = false;
                }
            }
        });
        if (escalate[0]) {
            prevHandler.handle(signal);
        }
        Signal.handle(signal, this);
    }

    public void register(Action action) {

        actions.add(action);
    }
}
