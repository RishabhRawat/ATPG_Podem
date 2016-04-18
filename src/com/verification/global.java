package com.verification;

import com.verification.BranchnBound.BnBNode;
import com.verification.BranchnBound.BnBStack;
import com.verification.components.*;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class global {
    public static int cc_complete_count = 0;
    public static int total_input = 0, total_output = 0;
    public static BnBNode rootNode;

    /**
     * 5 valued logic, lookup Roth D-valued Logic
     */
    public enum FvLogic {
        high,
        low,
        D,
        D_bar,
        X;

        @Override
        public String toString() {
            switch (this) {
                case high:
                    return "1";
                case low:
                    return "0";
                case D:
                    return "D";
                case D_bar:
                    return "D_bar";
                case X:
                    return "X";
                default:
                    throw new IllegalArgumentException();
            }
        }

        public static FvLogic not(FvLogic a) {
            switch (a) {
                case low:
                    return high;
                case high:
                    return low;
                case X:
                    return X;
                case D:
                    return D_bar;
                case D_bar:
                    return D;
                default:
                    throw new IllegalArgumentException();
            }
        }

        public static FvLogic and(FvLogic a, FvLogic b) {
            if (a == b)
                return a;
            switch (a) {
                case low:
                    return low;
                case high:
                    return b;
                case X:
                    return X;
                case D:
                    if (b == D_bar)
                        return low;
                    else
                        return and(b, a);
                default:
                    throw new IllegalArgumentException();
            }

        }

        public static FvLogic or(FvLogic a, FvLogic b) {
            if (a == b)
                return a;
            switch (a) {
                case low:
                    return b;
                case high:
                    return high;
                case X:
                    return X;
                case D:
                    if (b == D_bar)
                        return high;
                    else
                        return and(b, a);
                default:
                    throw new IllegalArgumentException();
            }

        }
    }

    public static HashMap<Integer, wire> all_nets = new HashMap<>();
    public static HashMap<Integer, component> all_components = new HashMap<>();
    static HashMap<Integer,String> Allsa0faults = new HashMap<>();
    static HashMap<Integer,String> Allsa1faults = new HashMap<>();

    private static boolean check_controllability_completion() {
        return (cc_complete_count > 0) && (cc_complete_count == total_output);
    }

    //D is for sa0 fault,
    static Integer getNextFault(){
        for(Integer aFaultSite: Allsa0faults.keySet()) {
            if(Allsa0faults.get(aFaultSite)=="") {
                all_nets.get(aFaultSite).assignment= global.FvLogic.D;
                return aFaultSite;
            }
        }
        for(Integer aFaultSite: Allsa1faults.keySet()) {
            if(Allsa0faults.get(aFaultSite)==""){
                all_nets.get(aFaultSite).assignment = global.FvLogic.D_bar;
                return aFaultSite;
            }
        }
        return null;
    }


    public static final wire highWire = new wire(0);
    public static final wire lowWire = new wire(0);

    public static void execute(String content) throws ScriptException, NoSuchMethodException, InvalidOperationException {

        //Initialising rail wires.. Powering up circuit :P
        {
            rootNode = new BnBNode(null, null);
            highWire.assignment = FvLogic.high;
            lowWire.assignment = FvLogic.low;
            highWire.assignment_node = rootNode;
            lowWire.assignment_node = rootNode;
            all_nets.put(0, lowWire);
            all_nets.put(1, highWire);
        }

        //Parsing the circuit
        {
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
            ScriptObjectMirror mirror = (ScriptObjectMirror) engine.eval("load('src/com/verification/parse.js')");
            Invocable invocable = (Invocable) engine;
            Object result = invocable.invokeFunction("create_schema", content);
            ((Map) engine.eval("all_nets")).forEach((key, value) -> {
                wire newWire = new wire(((Number)value).intValue());
                global.all_nets.put(((Number)value).intValue(), newWire);
            });

            ((Map) engine.eval("all_components")).forEach((key, value) -> {
                try {
                    ArrayList<Number> in_temp = new ArrayList<>(((Map) (((Map) value).get("inputs"))).values());
                    ArrayList<Number> out_temp = new ArrayList<>(((Map) (((Map) value).get("outputs"))).values());
                    ArrayList<Integer> in = new ArrayList<>();
                    ArrayList<Integer> out = new ArrayList<>();

                    in_temp.forEach((number) -> {
                        in.add(new Integer(number.intValue()));
                        all_nets.get(new Integer(number.intValue())).outputgate_id = new Integer((String) key);
                    });
                    out_temp.forEach((number) -> {
                        out.add(new Integer(number.intValue()));
                        all_nets.get(new Integer(number.intValue())).inputgate_id = new Integer((String) key);
                    });

                    component gate;
                    switch ((((Map) value).get("type")).toString()) {
                        case "PI":
                            gate = new PI(Integer.parseInt((String) key), in, out);
                            break;
                        case "PO":
                            gate = new PO(Integer.parseInt((String) key), in, out);
                            break;
                        case "and_gate":
                            gate = new and_gate(Integer.parseInt((String) key), in, out);
                            break;
                        case "or_gate":
                            gate = new or_gate(Integer.parseInt((String) key), in, out);
                            break;
                        case "not_gate":
                            gate = new not_gate(Integer.parseInt((String) key), in, out);
                            break;
                        case "fanout_gate":
                            gate = new fanout_gate(Integer.parseInt((String) key), in, out);
                            break;
                        default:
                            throw new InvalidOperationException();
                    }
                    all_components.put(Integer.parseInt((String) key), gate);
                } catch (InvalidOperationException e) {
                    e.printStackTrace();
                }
            });
        }

        //Setting up controllability
        all_components.forEach((id,component) -> {
            if(component instanceof PI){
                component.check_and_propogate_controllability();
            }
        });
        if(!check_controllability_completion())
            throw new RuntimeException();

        //Initialising fault list
        all_nets.forEach((key,value)->{
            if(key != 1 && key!= 0){
                Allsa0faults.put(key,"");
                Allsa1faults.put(key,"");
            }
        });


        //Algorithm start
        Integer faultSite = getNextFault();
        while(faultSite != null){
            BnBStack podemStack = new BnBStack(faultSite);
            podemStack.execute();
        }
    }
}
