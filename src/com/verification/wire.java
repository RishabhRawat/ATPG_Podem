package com.verification;

/**
 * Created by risha on 02-03-2016.
 */
public final class wire {

    private enum value {
        high,
        low,
        X,
        D,
        D_bar
    }

    protected int cc0 = -1;
    protected int cc1 = -1;
    protected Integer inputgate_id, outputgate_id;
    protected Integer wire_id;
    public wire(Integer id){
        wire_id = id;
    }
    protected value assignment = value.X;
    protected int assignment_node;
}
