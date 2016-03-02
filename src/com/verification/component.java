package com.verification;

import java.util.HashMap;

/**
 * Created by risha on 02-03-2016.
 */
public abstract class component {
    protected int inputs, outputs;
    protected Integer [] input_wires, output_wires;
    public abstract void propogate_controllability(HashMap list);
    protected boolean d_frontier = false;
}
