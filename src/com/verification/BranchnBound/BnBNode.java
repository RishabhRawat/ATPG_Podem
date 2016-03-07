package com.verification.BranchnBound;

import com.verification.components.PI;
import com.verification.global;

public class BnBNode {
    private PI assignedInput;
    global.FvLogic assignedValue;
    boolean flag = false;
    boolean active = true;
    BnBNode(PI input, global.FvLogic inputValue){
        assignedInput = input;
        assignedValue = inputValue;
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