package atm;

import banknotes.Banknote;

import java.util.List;

public interface Atm {
    void insert(Banknote banknote);

    List<Banknote> requestBanknoteByAmount(int amount);

    int balance();
}
