import atm.Atm;
import atm.AtmImpl;
import atm.dispenser.BanknoteDispenser;
import atm.dispenser.DefaultBanknoteDispenserImpl;
import atm.inserter.BanknoteInserter;
import atm.inserter.DefaultBanknoteInserterImpl;
import atm.viewer.DefaultBanknoteViewer;
import atm.viewer.Viewer;
import banknotes.Banknote;
import banknotes.FiveHundredBanknote;
import banknotes.FiveThousandBanknote;
import banknotes.HundredBanknote;
import banknotes.ThousandBanknote;
import banknotes.TwoThousandBanknote;
import banknoteStorage.BanknoteStorage;
import banknoteStorage.DefaultBanknoteStorage;
import exceptions.InsufficientFundsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class AtmTest {

    @Test
    public void positiveSmokeTest() {
        BanknoteStorage banknoteStorage = new DefaultBanknoteStorage();
        BanknoteDispenser dispenser = new DefaultBanknoteDispenserImpl(banknoteStorage);
        BanknoteInserter inserter = new DefaultBanknoteInserterImpl(banknoteStorage);
        Viewer viewer = new DefaultBanknoteViewer(banknoteStorage);
        Atm atm = new AtmImpl(dispenser, inserter, viewer);
        for (int i = 0; i < 3; i++) {
            atm.insert(new FiveHundredBanknote());
            atm.insert(new FiveThousandBanknote());
            atm.insert(new HundredBanknote());
            atm.insert(new ThousandBanknote()); //
            atm.insert(new TwoThousandBanknote());
        }

        List<Banknote> banknoteList = atm.requestBanknoteByAmount(24800);

        Assertions.assertEquals(13, banknoteList.size());
        Assertions.assertEquals(1000, atm.balance());
    }

    @Test
    public void negativeSmokeTest() {
        BanknoteStorage banknoteStorage = new DefaultBanknoteStorage();
        BanknoteDispenser dispenser = new DefaultBanknoteDispenserImpl(banknoteStorage);
        BanknoteInserter inserter = new DefaultBanknoteInserterImpl(banknoteStorage);
        Viewer viewer = new DefaultBanknoteViewer(banknoteStorage);
        Atm atm = new AtmImpl(dispenser, inserter, viewer);
        for (int i = 0; i < 3; i++) {
            atm.insert(new FiveHundredBanknote());
            atm.insert(new FiveThousandBanknote());
            atm.insert(new HundredBanknote());
            atm.insert(new ThousandBanknote()); //
            atm.insert(new TwoThousandBanknote());
        }

        assertThrows(InsufficientFundsException.class, () -> {
            atm.requestBanknoteByAmount(25801);
        });
    }
}
