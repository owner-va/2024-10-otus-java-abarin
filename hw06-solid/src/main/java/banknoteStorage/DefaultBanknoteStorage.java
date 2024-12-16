package banknoteStorage;

import banknotes.Banknote;
import exceptions.NotFoundNominal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultBanknoteStorage implements BanknoteStorage {

    private final Map<Integer, List<Banknote>> map = new HashMap<>();

    private int amount = 0;

    @Override
    public int amount() {
        return this.amount;
    }

    @Override
    public void insertBanknote(Banknote banknote) {
        map.computeIfAbsent(banknote.nominal(), k -> new ArrayList<>()).add(banknote);
        amount = amount + banknote.nominal();
    }

    @Override
    public Banknote pickBanknote(int nominal) {
        if (map.containsKey(nominal)) {
            List<Banknote> banknoteList = map.get(nominal);
            Banknote banknote = banknoteList.removeFirst();
            amount = amount - banknote.nominal();
            return banknote;
        }
        throw new NotFoundNominal("Not found banknote in storage by nominal:" + nominal);
    }

    @Override
    public List<Banknote> getBanknotesByNominal(int nominal) {
        if (map.containsKey(nominal)) {
            return List.copyOf(map.get(nominal));
        }
        return List.of();
    }

    @Override
    public List<Integer> getNominals() {
        return List.copyOf(map.keySet());
    }
}
