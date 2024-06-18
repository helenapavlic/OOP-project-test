package VendingMachine;

import java.util.EventListener;

public interface CoinsPanelListener extends EventListener {
    void coinsPanelEventOccurred(CoinsPanelEvent coinsPanelEvent);
}
