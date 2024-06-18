package VendingMachine;

import java.util.EventObject;

public class DisplayPanelEvent extends EventObject {
    private String buttonPressed;
    private String inputItemIdNumber;
    private String inputMoney;


    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public DisplayPanelEvent(Object source) {
        super(source);
    }

    public DisplayPanelEvent(Object source, String buttonPressed) {
        super(source);
        this.buttonPressed = buttonPressed;
    }

    public DisplayPanelEvent(Object source, String buttonPressed, String inputItemIdNumber, String inputMoney) {
        super(source);

        this.inputItemIdNumber = inputItemIdNumber;
        this.buttonPressed = buttonPressed;
        this.inputMoney = inputMoney;
    }

    public String getButtonPressed() {
        return buttonPressed;
    }

    public String getInputItemIdNumber() {
        return inputItemIdNumber;
    }

    public String getInputMoney() {
        return inputMoney;
    }
}
