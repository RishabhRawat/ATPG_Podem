package com.verification.components;

import com.verification.ConfictedImplicationException;
import com.verification.InvalidOperationException;
import com.verification.global;
import com.verification.wire;

import java.util.ArrayList;

public class PI extends component {
    public PI(Integer myID, ArrayList<Integer> inputIDs, ArrayList<Integer> outputIDs) throws InvalidOperationException {
        hashID = myID;
        inputs = 0;
        outputs = 1;
        input_wires = null;
        output_wires = new Integer[1];
        output_wires = outputIDs.toArray(output_wires);
        if(inputIDs != null)
            throw new InvalidOperationException();
    }

    /**
     * @inheritDoc
     */
    @Override
    public void propogate_controllability() {
        wire outputwire = global.all_nets.get(output_wires[0]);
        outputwire.cc0 = 1;
        outputwire.cc1 = 1;
        global.all_components.get(outputwire.outputgate_id).propogate_controllability();
    }


    @Override
    protected ArrayList<wire> imply() throws ConfictedImplicationException {
            throw new ConfictedImplicationException();
    }
}
