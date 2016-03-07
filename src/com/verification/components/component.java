package com.verification.components;

import com.verification.ConfictedImplicationException;
import com.verification.wire;

import java.util.ArrayList;

/**
 * Created by risha on 02-03-2016.
 */
public abstract class component {
    public int inputs, outputs;
    public Integer [] input_wires, output_wires;

    /**
     * Propogates controllability
     * calculates output based on current assigned values on input nets
     * no output, the value is set to the wires
     */
    public abstract void propogate_controllability();
    protected boolean d_frontier = false;


    //Implies the output with current input wires and returns the list of implied outputs

    /**
     * Implies the current input net values to output nets
     * @return a arraylist of wires which were implied
     * @throws ConfictedImplicationException
     */
    public abstract ArrayList<wire> imply() throws ConfictedImplicationException;

    //Returns true if xpath exists
    public boolean x_path_check(){
        ArrayList<wire> outs = imply();
        for (wire out:outs) {
            if()
        }
        return false
    }
}
