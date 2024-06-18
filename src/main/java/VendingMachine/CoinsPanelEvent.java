package VendingMachine;

import java.util.EventObject;

public class CoinsPanelEvent extends EventObject {
    private String coinValue;

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public CoinsPanelEvent(Object source) {
        super(source);
    }

    public CoinsPanelEvent(Object source, String coinValue) {
        super(source);
        this.coinValue = coinValue;
    }

    public String getCoinValue() {
        return coinValue;
    }
}
