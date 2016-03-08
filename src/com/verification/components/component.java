package com.verification.components;

import com.verification.ConfictedImplicationException;
import com.verification.global;
import com.verification.wire;

import java.util.ArrayList;

public abstract class component {
    public int inputs, outputs;
    public Integer [] input_wires, output_wires;
    public Integer hashID;

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
    protected abstract ArrayList<wire> imply() throws ConfictedImplicationException;

    //Returns true if xpath exists
    public Integer x_path_check(){
        if(this.getClass().getSimpleName().equals("PO"))
            return hashID;
        try {
            ArrayList<wire> outs = check_and_imply();
            for (wire out:outs) {
                if(out.assignment == global.FvLogic.X){
                    global.all_components.get(out.outputgate_id).x_path_check();
                }
            }
        }
        catch (ConfictedImplicationException e){
            System.out.println("CONFLICT!!!");
            return -1;
        }
        return -1;
    }

    public ArrayList<wire> check_and_imply() throws ConfictedImplicationException {
        for (int i = 0; i <inputs ; i++) {
            if(!global.all_nets.get(input_wires[i]).assignment_node.isActive())
                global.all_nets.get(input_wires[i]).assignment = global.FvLogic.X;
        }
        return imply();
    }
}
