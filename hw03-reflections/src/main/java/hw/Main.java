package hw;

import hw.testRunner.TestRunner;
import hw.tests.HwTest;

public class Main {
    public static void main(String[] args) {
        new TestRunner().run(HwTest.class);
    }
}
