package com.verification;

/**
 * Created by risha on 02-03-2016.
 */
public final class global {
    public static int cc_complete_count = 0;
    public static int total_input, total_output;
    public boolean check_controllability_completion()
    {
        return (cc_complete_count > 0) && (cc_complete_count == total_output);
    }
}
