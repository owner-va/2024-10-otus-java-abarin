package atm;

import atm.dispenser.BanknoteDispenser;
import atm.inserter.BanknoteInserter;
import atm.viewer.Viewer;
import banknotes.Banknote;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class AtmImpl implements Atm {
    private final BanknoteDispenser dispenser;
    private final BanknoteInserter inserter;
    private final Viewer viewer;

    @Override
    public void insert(Banknote banknote) {
        inserter.insert(banknote);
    }

    @Override
    public List<Banknote> requestBanknoteByAmount(int amount) {
        return dispenser.requestBanknoteByAmount(amount);
    }

    @Override
    public int balance() {
        return viewer.balance();
    }
}
