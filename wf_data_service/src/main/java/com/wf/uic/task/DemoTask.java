package com.wf.uic.task;

public class DemoTask {

    public void testTask(){

        System.out.println("---- schedule start ----");

        try {
            Thread.sleep(5 * 1000);
        } catch (InterruptedException e) {
        }

    }
}
