package com.verification.components;

import com.verification.InvalidOperationException;
import com.verification.global;

import java.util.ArrayList;

public class PO extends component {
    public PO(Integer myID, ArrayList<Integer> inputIDs, ArrayList<Integer> outputIDs) throws InvalidOperationException {
        hashID = myID;
        inputs = 1;
        outputs = 0;
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
        return null;
    }
}
