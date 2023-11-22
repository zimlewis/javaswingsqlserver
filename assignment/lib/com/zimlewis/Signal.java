package com.zimlewis;

import java.util.ArrayList;

public class Signal {
    private ArrayList<SignalConnection> connectList = new ArrayList<>();

    public Signal(Class<?>... parameterTypes){
    }

    public void emitSignal(Object... parameters) {
        for (SignalConnection connection : connectList) {
            try {
                connection.run(parameters);
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }

    public void connectSignal(SignalFunction function) {
        connectList.add(new SignalConnection(function));
    }


    private static class SignalConnection {
        private final SignalFunction function;

        public SignalConnection(SignalFunction function) {
            this.function = function;
        }

        public void run(Object... parameters) {
            function.run(parameters);
        }
    }

    @FunctionalInterface
    public interface SignalFunction {
        void run(Object... parameters);
    }
}

