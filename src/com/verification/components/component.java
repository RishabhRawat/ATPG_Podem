package com.verification.components;

import com.verification.BranchnBound.BnBNode;
import com.verification.global;
import com.verification.wire;

import java.util.*;

public abstract class component {
    public int inputs, outputs;
    public Integer[] input_wires, output_wires;
    public Integer hashID;
    public final global.FvLogic hardValue, easyValue, non_controlling_value;
    public final boolean inverting;

    protected component(Integer hashID, int inputs, int outputs, global.FvLogic hardValue, global.FvLogic easyValue, global.FvLogic non_controlling_value, boolean inverting ){
        this.hashID = hashID;
        this.inputs = inputs;
        this.outputs = outputs;
        this.hardValue = hardValue;
        this.easyValue = easyValue;
        this.non_controlling_value = non_controlling_value;
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
    private HashMap<Integer,String> imply(BnBNode newestNode, Integer faultWire) {
        HashMap<Integer,String> result = new HashMap<>();
        global.FvLogic output_value = calculate();
        for (Integer i : output_wires) {
            wire output_wire = global.all_nets.get(i);
            if(faultWire.equals(i)){
                if(((output_value == global.FvLogic.low || output_value == global.FvLogic.D_bar) && global.all_nets.get(faultWire).assignment == global.FvLogic.D) ||
                        ((output_value == global.FvLogic.high || output_value == global.FvLogic.D)&& global.all_nets.get(faultWire).assignment == global.FvLogic.D_bar)){
                    result.put(i,"ImplicationConflict");
                }
                else {
                    result.put(i,"FaultActivation");
                }
            }
            else {
                if (output_wire.assignment != output_value && output_wire.assignment_node != null && output_wire.assignment_node.isActive()) {
                    result.put(i, "ImplicationConflict");
                } else if (output_wire.assignment != output_value) {
                    output_wire.assignment_node = newestNode;
                    output_wire.assignment = output_value;
                    result.put(i, "ImplicationSuccess");
                } else {
                    result.put(i, "ImplicationMatch");
                }
            }
        }
        return result;
    }

    public abstract global.FvLogic calculate();

    //Returns the primary output, it propagates to... maybe
    public Stack<Integer> x_path_check(Stack<Integer> inputStack) {
        Stack<Integer> returnStack;

        if(inputStack != null)
            returnStack = (Stack<Integer>) inputStack.clone();
        else
            returnStack = new Stack<Integer>();

        returnStack.add(hashID);
        if (this.getClass().getSimpleName().equals("PO")) {
            calculate();
            return returnStack;
        }
        for (Integer outID : output_wires) {
            wire out = global.all_nets.get(outID);
            if (out.assignment == global.FvLogic.X || out.assignment == global.FvLogic.D  || out.assignment == global.FvLogic.D_bar) {
                returnStack = global.all_components.get(out.outputgate_id).x_path_check(returnStack);
                if(returnStack != null)
                    return returnStack;
           }
        }
        return null;
    }

    // returns true if success
    public boolean check_and_imply(Integer faultWire) {
        ArrayList<BnBNode> inputNodes = new ArrayList<>();
        HashMap<Integer,String> implication_result;

        if(input_wires != null) {
            for (Integer i : input_wires) {
                if (global.all_nets.get(i).assignment_node == null || !global.all_nets.get(i).assignment_node.isActive()) {
                    global.all_nets.get(i).assignment = global.FvLogic.X;
                    global.all_nets.get(i).assignment_node = global.rootNode;
                }
                inputNodes.add(global.all_nets.get(i).assignment_node);
            }
        }
        Comparator<BnBNode> cmp = new Comparator<BnBNode>() {
            @Override
            public int compare(BnBNode o1, BnBNode o2) {
                return (o1.nodeNumber - o2.nodeNumber);
            }
        };
        if(this instanceof PI)
            implication_result =  imply(((PI)this).assignment_node,faultWire);
        else
            implication_result =  imply(Collections.max(inputNodes, cmp),faultWire);

        for (Integer wireID:implication_result.keySet()) {
            boolean breakfor = false;
            switch (implication_result.get(wireID)){
                case "ImplicationConflict":
                    return false;
                case "FaultActivation":
//                    breakfor = true;
                    break;
                case "ImplicationMatch":
//                    breakfor = true;
                    break;
                case "ImplicationSuccess":
                    global.all_components.get(global.all_nets.get(wireID).outputgate_id).check_and_imply(faultWire);
                    break;
            }
            if (breakfor) break;
        }
        return true;
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
