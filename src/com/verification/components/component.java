package com.verification.components;

import com.verification.BranchnBound.BnBNode;
import com.verification.global;
import com.verification.wire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public abstract class component {
    public int inputs, outputs;
    public Integer[] input_wires, output_wires;
    public Integer hashID;
    public final global.FvLogic hardValue, easyValue;
    public final boolean inverting;

    protected component(Integer hashID, int inputs, int outputs, global.FvLogic hardValue, global.FvLogic easyValue, boolean inverting ){
        this.hashID = hashID;
        this.inputs = inputs;
        this.outputs = outputs;
        this.hardValue = hardValue;
        this.easyValue = easyValue;
        this.inverting = inverting;
    }

    /**
     * Propogates controllability
     * calculates output based on current assigned values on input nets
     * no output, the value is set to the wires
     */
    protected abstract void propogate_controllability();

    protected boolean d_frontier = false;


    //Implies the output with current input wires and returns the list of implied outputs


    /**
     * Implies the current input net values to output nets
     *
     * @param newestNode
     * @return a arraylist of wires which were implied
     */
    private boolean imply(BnBNode newestNode) {
        global.FvLogic output_value = calculate();
        for (Integer i : output_wires) {
            wire output_wire = global.all_nets.get(i);
            if (output_wire.assignment != output_value && output_wire.assignment_node.isActive()) {
                return false;
            } else if (output_wire.assignment != output_value) {
                output_wire.assignment_node = newestNode;
                output_wire.assignment = output_value;
            }
        }
        return true;
    }

    /**
     * Implies the current input net values to output nets
     *
     * @return a arraylist of wires which were implied
     */
    private boolean imply(BnBNode newestNode, component myComponent) {
        if (this == myComponent)
            return false;
        return imply(newestNode);

    }

    public abstract global.FvLogic calculate();

    //Returns true if xpath exists
    public Integer x_path_check() {
        if (this.getClass().getSimpleName().equals("PO")) {
            calculate();
            return hashID;
        }
        for (Integer outID : output_wires) {
            wire out = global.all_nets.get(outID);
            if (out.assignment == global.FvLogic.X || out.assignment == global.FvLogic.D  || out.assignment == global.FvLogic.D_bar) {
                Integer id = global.all_components.get(out.outputgate_id).x_path_check();
                if(id != -1)
                    return id;

            }
        }

        return -1;
    }

    public boolean check_and_imply() {
        ArrayList<BnBNode> inputNodes = new ArrayList<>();

        for (Integer i : input_wires) {
            if (!global.all_nets.get(i).assignment_node.isActive()) {
                global.all_nets.get(i).assignment = global.FvLogic.X;
                global.all_nets.get(i).assignment_node = global.rootNode;
            }
            inputNodes.add(global.all_nets.get(i).assignment_node);
        }

        Comparator<BnBNode> cmp = new Comparator<BnBNode>() {
            @Override
            public int compare(BnBNode o1, BnBNode o2) {
                return (o1.nodeNumber - o2.nodeNumber);
            }
        };

        return imply(Collections.max(inputNodes, cmp));
    }

    public void check_and_propogate_controllability() {
        if (input_wires != null) {
            for (Integer i : input_wires) {
                if (global.all_nets.get(i).cc0 == -1 || global.all_nets.get(i).cc1 == -1) {
                    return;
                }
            }
        }
        propogate_controllability();
    }

}
