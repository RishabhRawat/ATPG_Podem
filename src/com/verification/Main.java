package com.verification;

import javax.script.ScriptException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, ScriptException, NoSuchMethodException {
        //TODO: change this to generic
        String content = new Scanner(new File("C:\\Users\\risha\\Desktop\\yosys-win32-mxebin-0.6\\yosys-win32-mxebin-0.6\\output.json")).useDelimiter("\\Z").next();
        new global();
        global.init(content);
    }
}
