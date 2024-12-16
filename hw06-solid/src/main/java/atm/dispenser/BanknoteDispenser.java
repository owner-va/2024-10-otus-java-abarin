package atm.dispenser;

import banknotes.Banknote;

import java.util.List;

public interface BanknoteDispenser {
    List<Banknote> requestBanknoteByAmount(int amount);
}
