package banknoteStorage;

import banknotes.Banknote;

import java.util.List;

public interface BanknoteStorage {
    void insertBanknote(Banknote banknote);

    Banknote pickBanknote(int nominal);

    List<Banknote> getBanknotesByNominal(int nominal);

    List<Integer> getNominals();

    int amount();
}
