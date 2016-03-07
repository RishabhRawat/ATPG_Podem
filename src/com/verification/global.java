package com.verification;

import java.util.HashMap;

/**
 * Created by risha on 02-03-2016.
 */
public final class global {
    public static int cc_complete_count = 0;
    public static int total_input, total_output;
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
    }

    public static HashMap<Integer,Object> all_components = new HashMap<>();

    public boolean check_controllability_completion()
    {
        return (cc_complete_count > 0) && (cc_complete_count == total_output);
    }
}
