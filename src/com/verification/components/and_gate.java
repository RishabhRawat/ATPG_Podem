package com.verification.components;

import com.verification.ConfictedImplicationException;
import com.verification.global;
import com.verification.wire;

import java.util.ArrayList;

import static com.verification.global.FvLogic.and;

public final class and_gate extends component {
    public and_gate(){
        inputs = 2;
        outputs = 1;
        input_wires = new Integer[2];
        output_wires = new Integer[1];
    }

    /**
     * @inheritDoc
     */
    @Override
    public void propogate_controllability() {
        wire outputwire = (wire) global.all_components.get(output_wires[0]);
        wire input1 = (wire) global.all_components.get(input_wires[0]);
        wire input2 = (wire) global.all_components.get(input_wires[1]);
        if(input1.cc0 == -1 || input2.cc0 == -1)
            return;
        outputwire.cc0 = ((input1.cc0>input2.cc0)?input2.cc0:input1.cc0)+1;
        outputwire.cc1 = input1.cc1 + input2.cc1 + 1;
        ((component)global.all_components.get(outputwire.outputgate_id)).propogate_controllability();
        ((component)global.all_components.get(outputwire.outputgate_id)).propogate_controllability();
    }

    /**
     * @inheritDoc
     */
    @Override
    public ArrayList<wire> imply() throws ConfictedImplicationException {
        global.FvLogic outputValue = and(((wire)global.all_components.get(input_wires[0])).assignment,
                ((wire)global.all_components.get(input_wires[1])).assignment);
        wire outputwire = ((wire)global.all_components.get(output_wires[0]));
        if(outputwire.assignment == global.FvLogic.X || outputValue == outputwire.assignment){
            ArrayList<wire> temp = new ArrayList<>();
            outputwire.assignment = outputValue;
            temp.add(outputwire);
            return temp;
        }
        else
            throw new ConfictedImplicationException();
    }
}
