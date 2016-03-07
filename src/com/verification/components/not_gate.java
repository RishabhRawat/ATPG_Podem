package com.verification.components;

import com.verification.ConfictedImplicationException;
import com.verification.global;
import com.verification.wire;

import java.util.ArrayList;

import static com.verification.global.FvLogic.not;


public class not_gate extends component{
    public not_gate(){
        inputs = 1;
        outputs = 1;
        input_wires = new Integer[1];
        output_wires = new Integer[1];
    }

    /**
     * @inheritDoc
     */
    @Override
    public void propogate_controllability() {
        wire outputwire = (wire) global.all_components.get(output_wires[0]);
        wire input1 = (wire) global.all_components.get(input_wires[0]);
        outputwire.cc0 = input1.cc0 + 1;
        outputwire.cc1 = input1.cc1 + 1;
        ((component)global.all_components.get(outputwire.outputgate_id)).propogate_controllability();
    }

    /**
     * @inheritDoc
     */
    @Override
    public ArrayList<wire> imply() throws ConfictedImplicationException {
        global.FvLogic outputValue = not(((wire) global.all_components.get(input_wires[0])).assignment);
        wire outputwire = ((wire) global.all_components.get(output_wires[0]));
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
