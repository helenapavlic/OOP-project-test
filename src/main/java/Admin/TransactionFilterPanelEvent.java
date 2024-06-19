package Admin;

import java.util.EventObject;

public class TransactionFilterPanelEvent extends EventObject {
    private final String[] filters;
    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public TransactionFilterPanelEvent(Object source, String[] filters) {
        super(source);
        this.filters = filters;
    }

    public String[] getFilters() {
        return filters;
    }
}
