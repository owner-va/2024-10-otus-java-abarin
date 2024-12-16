package atm.inserter;

import banknotes.Banknote;
import banknoteStorage.BanknoteStorage;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DefaultBanknoteInserterImpl implements BanknoteInserter {

    private final BanknoteStorage banknoteStorage;

    @Override
    public void insert(Banknote banknote) {
        banknoteStorage.insertBanknote(banknote);
    }
}
