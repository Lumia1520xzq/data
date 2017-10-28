package com.wf.uic.tes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Testlog {

    private Logger logger = LoggerFactory.getLogger(Testlog.class);

    private Log logger2 = LogFactory.getLog(Test.class);

    @Test
    public void test1(){

        for (int i = 0; i <100000 ; i++) {
            logger2.debug("chenyiping logger2 test");
        }


    }
}
