package com.verification.components;

import com.verification.global;
import com.verification.wire;

import java.util.ArrayList;

import static com.verification.global.FvLogic.and;

public final class and_gate extends component {
    public and_gate(Integer myID, ArrayList<Integer> inputIDs, ArrayList<Integer> outputIDs) {
        super(myID,2,1,global.FvLogic.high,global.FvLogic.low, global.FvLogic.high,false);
        input_wires = new Integer[2];
        output_wires = new Integer[1];
        input_wires = inputIDs.toArray(input_wires);
        output_wires = outputIDs.toArray(output_wires);
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void propogate_controllability() {
        wire outputwire = global.all_nets.get(output_wires[0]);
        wire input1 = global.all_nets.get(input_wires[0]);
        wire input2 = global.all_nets.get(input_wires[1]);
        if (input1.cc0 == -1 || input2.cc0 == -1)
            return;
        outputwire.cc0 = ((input1.cc0 > input2.cc0) ? input2.cc0 : input1.cc0) + 1;
        outputwire.cc1 = input1.cc1 + input2.cc1 + 1;
        global.all_components.get(outputwire.outputgate_id).check_and_propogate_controllability();
    }

    /**
     * @inheritDoc
     */
    @Override
    public global.FvLogic calculate() {
        return and(global.all_nets.get(input_wires[0]).assignment,
                global.all_nets.get(input_wires[1]).assignment);
    }
}
