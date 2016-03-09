package com.verification.components;

import com.verification.BranchnBound.BnBNode;
import com.verification.InvalidOperationException;
import com.verification.global;

import java.util.ArrayList;

public class PO extends component {
    public global.FvLogic assignment = global.FvLogic.X;
    public BnBNode assignment_node;
    public PO(Integer myID, ArrayList<Integer> inputIDs, ArrayList<Integer> outputIDs) throws InvalidOperationException {
        super(myID,1,0,global.FvLogic.high,global.FvLogic.high,false);
        input_wires = new Integer[1];
        output_wires = null;
        output_wires = new Integer[1];
        input_wires = inputIDs.toArray(output_wires);
        global.total_output++;
        if(outputIDs.size()!=0)
            throw new InvalidOperationException();
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void propogate_controllability() {
        global.cc_complete_count++;
    }

    /**
     * @inheritDoc
     */
    @Override
    public global.FvLogic calculate() {
        assignment = global.all_nets.get(input_wires[0]).assignment;
        assignment_node = global.all_nets.get(input_wires[0]).assignment_node;
        return null;
    }
}
