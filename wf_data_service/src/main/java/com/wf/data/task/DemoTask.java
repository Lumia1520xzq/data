package com.wf.data.task;

public class DemoTask {

    public void testTask(){

        System.out.println("---- schedule start ----");

        try {
            Thread.sleep(5 * 1000);
            System.out.println("----  ----");

        } catch (InterruptedException e) {
            System.out.println("---  ----");
        }

    }
}
