package com.verification.BranchnBound;

import com.verification.InvalidOperationException;
import com.verification.components.PI;
import com.verification.components.PO;
import com.verification.components.component;
import com.verification.global;
import com.verification.wire;

import java.util.*;

/**
 * The Branch and Bound tree implemented as a stack
 */
public class BnBStack extends Stack<BnBNode> {
    /**
     * Inserts a new node to the stack corrosponding to a assignment at input
     * @param input the PI being assigned
     * @param inputValue the value being assigned
     * @return the inserted node
     */
    private Integer faultSite = null;

    private void insertNode(PI input, boolean flag, global.FvLogic inputValue){
        BnBNode node = new BnBNode(input,inputValue);
        input.assignment_node = node;
        input.assignment = inputValue;
        push(node);
        if(!(input.check_and_imply(faultSite))) swap();
    }

     private Comparator<wire> cc0_comparator = new Comparator<wire>() {
        @Override
        public int compare(wire o1, wire o2) {
            return o1.cc0-o2.cc0;
        }
    };
    private Comparator<wire> cc1_comparator = new Comparator<wire>() {
        @Override
        public int compare(wire o1, wire o2) {
            return o1.cc0-o2.cc0;
        }
    };

    private void swap(){
        BnBNode topNode = pop();
        if(topNode.flag){
            topNode.active = false;
            swap();
        }
        else {
            insertNode(topNode.getAssignedInput(),true,global.FvLogic.not(topNode.assignedValue));
        }
    }

    // value should only be high or low
    private void backTrace(Integer wireID, global.FvLogic value) throws InvalidOperationException {
        component associated_gate = global.all_components.get(global.all_nets.get(wireID).inputgate_id);
        global.FvLogic associated_value = value;

        while (!(associated_gate instanceof PI)){
            ArrayList<wire> input_wires = new ArrayList<>();
            wire selectedWire;

            for (int i = 0; i < associated_gate.input_wires.length; i++) {
                if(global.all_nets.get(associated_gate.input_wires[i]).assignment == global.FvLogic.X)
                    input_wires.add(global.all_nets.get(associated_gate.input_wires[i]));
            }
            selectedWire = Collections.max(input_wires,(associated_gate.inverting ^ (associated_value == associated_gate.hardValue) ^
                        (associated_gate.hardValue == global.FvLogic.high))?cc1_comparator:cc0_comparator);
            if(associated_gate.inverting)
                associated_value = global.FvLogic.not(associated_value);
            associated_gate = global.all_components.get(selectedWire.inputgate_id);
        }
        //BACKTRACE COMPLETED
        insertNode((PI)associated_gate,false,associated_value);
    }

    public BnBStack(Integer faultSite) throws InvalidOperationException {
        this.faultSite = faultSite;
    }

    private void solveNextObjective(Stack<Integer> xpath) throws InvalidOperationException {
        Integer objectiveID = -1;
        global.FvLogic objectiveValue;
        if (((global.all_nets.get(faultSite).assignment == global.FvLogic.D)&&
                (global.all_components.get(global.all_nets.get(faultSite).inputgate_id).calculate() == global.FvLogic.high))||
                ((global.all_nets.get(faultSite).assignment == global.FvLogic.D_bar) &&
                (global.all_components.get(global.all_nets.get(faultSite).inputgate_id).calculate() == global.FvLogic.low))){
                //TODO: Add objective check
                component gate = global.all_components.get(Jfrontier(xpath));
                objectiveValue = gate.non_controlling_value;
                boolean foundUnassignedWire = false;
                for (Integer input_wire : gate.input_wires) {
                    if(global.all_nets.get(input_wire).assignment== global.FvLogic.X){
                        objectiveID = input_wire;
                        foundUnassignedWire = true;
                        break;
                    }
                }
                if (!foundUnassignedWire) throw new InvalidOperationException();
            }
            else {
                objectiveID = faultSite;
                if (global.all_nets.get(faultSite).assignment== global.FvLogic.D)
                    objectiveValue = global.FvLogic.high;
                else if (global.all_nets.get(faultSite).assignment== global.FvLogic.D_bar)
                    objectiveValue = global.FvLogic.low;
                else
                    throw new InvalidOperationException();
            }

        backTrace(objectiveID,objectiveValue);
    }

    //Checks the xpath for J frontier gate
    private Integer Jfrontier(Stack<Integer> xpath) throws InvalidOperationException {
        Integer xGate;
        do {
            xGate = xpath.pop();
            boolean afterX = false, beforeD = false;
            Integer[] outWires = global.all_components.get(xGate).output_wires;
            Integer[] inWires = global.all_components.get(xGate).input_wires;
            for (Integer outWire : outWires) {
                afterX|=(global.all_nets.get(outWire).assignment == global.FvLogic.X);
            }
            for (Integer inWire : inWires) {
                beforeD|=(global.all_nets.get(inWire).assignment == global.FvLogic.D || global.all_nets.get(inWire).assignment == global.FvLogic.D_bar);
            }
            if(beforeD && afterX)
                return xGate;
        }while ( !xGate.equals(global.all_nets.get(faultSite).outputgate_id));
        throw new InvalidOperationException();
    }

    private boolean faultTested(){
        //Test to check fault activated
        if ((global.all_nets.get(faultSite).assignment == global.FvLogic.D &&
                global.all_components.get(global.all_nets.get(faultSite).inputgate_id).calculate() == global.FvLogic.high)
                || (global.all_nets.get(faultSite).assignment == global.FvLogic.D_bar &&
                global.all_components.get(global.all_nets.get(faultSite).inputgate_id).calculate() == global.FvLogic.low)){

            //Check id fault Propogated
            for (component value : global.all_components.values()) {
                if(value instanceof PO){
                    if(((PO)value).assignment == global.FvLogic.D || ((PO)value).assignment == global.FvLogic.D_bar)
                        return true;
                }
            }
        }
        return false;

    }


    public void execute() throws InvalidOperationException {
        global.all_components.get(global.all_nets.get(faultSite).outputgate_id).check_and_imply(faultSite);
        try {
            do{
                Stack<Integer> xpath = global.all_components.get(global.all_nets.get(faultSite).outputgate_id).x_path_check(null);
                if (xpath != null) {
                    solveNextObjective(xpath);
                } else
                    swap();
                if(faultTested())
                    System.out.println("aysuafbiwdlhhhhhhhhhb");
            }while (!faultTested());
        }
        catch (EmptyStackException e){
            e.printStackTrace();
        }
    }
}
