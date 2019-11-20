package cn.devit.util.proguard;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;

import org.junit.Test;

import proguard.retrace.ReTrace;

public class RetractDemo {

    @Test
    public void from_file() throws Exception {
        LineNumberReader reader = new LineNumberReader(
                new BufferedReader(new InputStreamReader(
                        new FileInputStream(R.example_stacktrace8_txt),
                        "UTF-8")));
        new ReTrace(ReTrace.STACK_TRACE_EXPRESSION, false,
                R.example_proguard_map_txt).retrace(reader,
                        new PrintWriter(System.out));
    }
    @Test
    public void from_file2() throws Exception {
        LineNumberReader reader = new LineNumberReader(
                new BufferedReader(new InputStreamReader(
                        new FileInputStream(R.example_stacktrace13_txt),
                        "UTF-8")));
        new ReTrace(ReTrace.STACK_TRACE_EXPRESSION, false,
                R.example_proguard_map_txt).retrace(reader,
                        new PrintWriter(System.out));
    }

}
