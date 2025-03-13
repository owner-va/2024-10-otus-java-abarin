import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeqNumber {
    private static final Logger logger = LoggerFactory.getLogger(SeqNumber.class);
    private int value = 0;
    private String lastState = "even";
    private boolean isReverse = false;

    public synchronized void action(String state) {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                while (lastState.equals(state)) {
                    this.wait();
                }
                if (value <= 1 && isReverse) {
                    lastState = state;
                    notifyAll();
                    break;
                }
                value += isReverse ? -1 : 1;
                logger.info(String.valueOf(value));
                if (value == 10) {
                    isReverse = true;
                }
                lastState = state;
                notifyAll();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        var seqNumber = new SeqNumber();
        new Thread(() -> seqNumber.action("even"), "even").start();
        new Thread(() -> seqNumber.action("odd"), "odd").start();
    }
}
