package com.verification.components;

import com.verification.BranchnBound.BnBNode;
import com.verification.InvalidOperationException;
import com.verification.global;
import com.verification.wire;

import java.util.ArrayList;

public class PI extends component {
    public global.FvLogic assignment = global.FvLogic.X;
    public BnBNode assignment_node;

    public PI(Integer myID, ArrayList<Integer> inputIDs, ArrayList<Integer> outputIDs) throws InvalidOperationException {
        hashID = myID;
        inputs = 0;
        outputs = 1;
        input_wires = null;
        output_wires = new Integer[1];
        output_wires = outputIDs.toArray(output_wires);
        global.total_input++;
        if(inputIDs.size()!=0)
            throw new InvalidOperationException();
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void propogate_controllability() {
        wire outputwire = global.all_nets.get(output_wires[0]);
        outputwire.cc0 = 1;
        outputwire.cc1 = 1;
        global.all_components.get(outputwire.outputgate_id).check_and_propogate_controllability();
    }

    @Override
    public global.FvLogic calculate() {
        return assignment;
    }
}
