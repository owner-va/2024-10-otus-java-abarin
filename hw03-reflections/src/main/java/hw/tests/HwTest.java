package hw.tests;

import hw.annotations.AfterTest;
import hw.annotations.BeforeTest;
import hw.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HwTest {
    private String string;
    private final Logger logger = LoggerFactory.getLogger(HwTest.class);


    @BeforeTest
    public void setUp() {
        logger.info("calling setUp");
        string = "String";
    }

    @BeforeTest
    public void setUpTwo() {
        logger.info("calling setUpTwo");
    }

    @Test
    public void shouldSuccessTest() {
        logger.info("calling shouldSuccessTest");
        if (!string.equals("String")) {
            throw new RuntimeException("String not equals String");
        }
    }

    @Test
    public void shouldSuccessTwoTest() {
        logger.info("calling shouldSuccessTwoTest");
        if (!string.equals("String")) {
            throw new RuntimeException("String not equals String");
        }
    }

    @Test
    public void shouldFailedTest() {
        logger.info("calling shouldFailedTest");
        if (string.equals("String")) {
            throw new RuntimeException("String equals String");
        }
    }

    @AfterTest
    public void tearDown() {
        logger.info("calling tearDown");
        string = null;
    }
}
