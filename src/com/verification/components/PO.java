package com.verification.components;

import com.verification.ConfictedImplicationException;
import com.verification.InvalidOperationException;
import com.verification.global;
import com.verification.wire;

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
        if(outputIDs != null)
            throw new InvalidOperationException();
    }

    /**
     * @inheritDoc
     */
    @Override
    public void propogate_controllability() {
        global.cc_complete_count++;
    }

    /**
     * @inheritDoc
     */
    @Override
    protected ArrayList<wire> imply() throws ConfictedImplicationException {
        //TODO: worry about what happens here
        return null;
    }
}
