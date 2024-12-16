package atm.viewer;

import banknoteStorage.BanknoteStorage;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DefaultBanknoteViewer implements Viewer {
    private final BanknoteStorage banknoteStorage;

    @Override
    public int balance() {
        return banknoteStorage.amount();
    }
}
