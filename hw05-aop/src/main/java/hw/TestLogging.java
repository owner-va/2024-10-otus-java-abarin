package hw;

import hw.annotations.Log;

class TestLogging implements TestLoggingInterface {

    @Override
    public void calculation(int param) {
    }

    @Log
    @Override
    public void calculation(int param, int paramTwo) {
    }

    @Log
    @Override
    public void calculation(int param, int paramTwo, int paramThree) {

    }
}