package com.verification.components;

import com.verification.ConfictedImplicationException;
import com.verification.global;
import com.verification.wire;

import java.util.ArrayList;

public class fanout_gate extends component {
    public fanout_gate(int out){
        inputs = 1;
        outputs = out;
        input_wires = new Integer[1];
        output_wires = new Integer[out];
    }
    @Override
    public void propogate_controllability() {
        wire input = (wire) global.all_components.get(input_wires[0]);

        for (Integer output:output_wires) {
            wire outputwire = (wire) global.all_components.get(output);
            outputwire.cc0 = input.cc0;
            outputwire.cc1 = input.cc1;
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public ArrayList<wire> imply() throws ConfictedImplicationException {
        ArrayList<wire> temp = new ArrayList<>();
        wire inputwire = ((wire)global.all_components.get(output_wires[input_wires[0]]));
        for (Integer wireID:output_wires) {
            wire outputwire = ((wire)global.all_components.get(output_wires[wireID]));
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
