package com.verification.BranchnBound;

import com.verification.InvalidOperationException;
import com.verification.components.PI;
import com.verification.components.component;
import com.verification.global;
import com.verification.wire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Stack;

/**
 * The Branch and Bound tree implemented as a stack
 */
public class BnBStack extends Stack<BnBNode> {
    /**
     * Inserts a new node to the stack corrosponding to a assignment at input
     * @param input the PI being assigned
     * @param inputValue the value being assigned
     * @return the inserted node
     */
    private wire faultSite = null;

    private BnBNode insertNode(PI input, boolean flag, global.FvLogic inputValue){
        BnBNode node = new BnBNode(input,inputValue);
        push(node);
        //TODO: Implement Implication
        return node;
    }

    private Comparator<wire> cc0_comparator = new Comparator<wire>() {
        @Override
        public int compare(wire o1, wire o2) {
            return o1.cc0-o2.cc0;
        }
    };
    private Comparator<wire> cc1_comparator = new Comparator<wire>() {
        @Override
        public int compare(wire o1, wire o2) {
            return o1.cc0-o2.cc0;
        }
    };


    private void swap(){
        BnBNode topNode = pop();
        if(topNode.flag){
            topNode.active = false;
            swap();
        }
        else {
            insertNode(topNode.getAssignedInput(),true,global.FvLogic.not(topNode.assignedValue));
        }
    }

    // value should only be high or low
    private void backTrace(Integer wireID, global.FvLogic value) throws InvalidOperationException {
        component associated_gate = global.all_components.get(global.all_nets.get(wireID).inputgate_id);
        global.FvLogic associated_value = value;

        while (!(associated_gate instanceof PI)){
            ArrayList<wire> input_wires = new ArrayList<>();
            wire selectedWire;

            for (int i = 0; i < associated_gate.input_wires.length; i++) {
                if(global.all_nets.get(associated_gate.input_wires[i]).assignment == global.FvLogic.X)
                    input_wires.add(global.all_nets.get(associated_gate.input_wires[i]));
            }
            selectedWire = Collections.max(input_wires,(associated_gate.inverting ^ (associated_value == associated_gate.hardValue) ^
                        (associated_gate.hardValue == global.FvLogic.high))?cc1_comparator:cc0_comparator);
            if(associated_gate.inverting)
                associated_value = global.FvLogic.not(associated_value);
            associated_gate = global.all_components.get(selectedWire.inputgate_id);
        }
        //BACKTRACE COMPLETED
        insertNode((PI)associated_gate,false,associated_value);
    }

    public BnBStack(wire faultSite) {
        this.faultSite = faultSite;
    }

    public void execute() {
        Integer a = global.all_components.get(faultSite.outputgate_id).x_path_check();
        System.out.println(a);
    }
}
