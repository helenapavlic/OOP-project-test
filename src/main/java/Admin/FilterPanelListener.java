package Admin;

import java.util.EventListener;

public interface FilterPanelListener extends EventListener {
    void transactionFilterPanelEventOccurred(TransactionFilterPanelEvent transactionFilterPanelEvent);
    void itemFilterPanelEventOccurred(ItemFilterPanelEvent itemFilterPanelEvent);

}
