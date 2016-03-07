package com.verification;

import java.util.HashMap;

public final class global {
    public static int cc_complete_count = 0;
    public static int total_input, total_output;

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
            switch(this) {
                case high:
                    return "1";
                case low:
                    return "0";
                default:
                    throw new IllegalArgumentException();
            }
        }
        public static FvLogic not(FvLogic a){
            switch (a){
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
        public static FvLogic and(FvLogic a, FvLogic b){
            if(a == b)
                return a;
            switch (a){
                case low:
                    return low;
                case high:
                    return b;
                case X:
                    return X;
                case D:
                    if(b == D_bar)
                        return low;
                    else
                        return and(b,a);
                default:
                    throw new IllegalArgumentException();
            }

        }
        public static FvLogic or(FvLogic a, FvLogic b){
            if(a == b)
                return a;
            switch (a){
                case low:
                    return b;
                case high:
                    return high;
                case X:
                    return X;
                case D:
                    if(b == D_bar)
                        return high;
                    else
                        return and(b,a);
                default:
                    throw new IllegalArgumentException();
            }

        }
    }

    public static HashMap<Integer,Object> all_components = new HashMap<>();

    public boolean check_controllability_completion()
    {
        return (cc_complete_count > 0) && (cc_complete_count == total_output);
    }
}
