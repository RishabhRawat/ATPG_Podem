package com.verification.components;

import com.verification.ConfictedImplicationException;
import com.verification.global;
import com.verification.wire;

import java.util.ArrayList;

public class fanout_gate extends component {
    public fanout_gate(Integer myID, ArrayList<Integer> inputIDs, ArrayList<Integer> outputIDs){
        hashID = myID;
        inputs = 1;
        outputs = outputIDs.size();
        input_wires = new Integer[1];
        output_wires = new Integer[outputs];
        input_wires = inputIDs.toArray(input_wires);
        output_wires = outputIDs.toArray(output_wires);
    }
    @Override
    public void propogate_controllability() {
        wire input = global.all_nets.get(input_wires[0]);

        for (Integer output:output_wires) {
            wire outputwire = global.all_nets.get(output);
            outputwire.cc0 = input.cc0;
            outputwire.cc1 = input.cc1;
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    protected ArrayList<wire> imply() throws ConfictedImplicationException {
        ArrayList<wire> temp = new ArrayList<>();
        wire inputwire = global.all_nets.get(output_wires[input_wires[0]]);
        for (Integer wireID:output_wires) {
            wire outputwire = global.all_nets.get(output_wires[wireID]);
            if(outputwire.assignment == global.FvLogic.X || inputwire.assignment == outputwire.assignment){
                outputwire.assignment = inputwire.assignment;
                temp.add(outputwire);
            }
            else
                throw new ConfictedImplicationException();
        }
        return temp;
    }
}
