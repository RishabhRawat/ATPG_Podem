package com.verification.BranchnBound;

import com.verification.components.PI;
import com.verification.global;

public class BnBNode{
    static int totalNodes = 0;
    private PI assignedInput;
    global.FvLogic assignedValue;
    boolean flag = false;
    boolean active = true;
    public int nodeNumber;
    public BnBNode(PI input, global.FvLogic inputValue){
        assignedInput = input;
        assignedValue = inputValue;
        nodeNumber = (++totalNodes);
    }
    public PI getAssignedInput() {
        return assignedInput;
    }
    public boolean isFlag() {
        return flag;
    }
    public boolean isActive() {
        return active;
    }

}