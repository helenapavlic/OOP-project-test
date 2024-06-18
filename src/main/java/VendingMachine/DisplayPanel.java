package VendingMachine;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DisplayPanel extends JPanel {

    private final Font textAreaFont = new Font("Arial", Font.BOLD, 30);
    private final Font labelFont = new Font("Arial", Font.PLAIN, 18);
    private final Font numberPadFont = new Font("Arial", Font.PLAIN, 20);
    private final Font toolBarFont = new Font("Arial", Font.PLAIN, 18);

    private final String[] buttonLabels = {
            "7", "8", "9",
            "4", "5", "6",
            "1", "2", "3",
            "DEL", "0", "OK"
    };
    private final ArrayList<JButton> numPadButtons = new ArrayList<>();

    private JPanel numberPadPanel;
    private JPanel textPanel;
    private JPanel toolBarPanel;

    private JLabel selectedItemLabel;
    private JTextArea selectedItemTextArea;
    private JLabel inputMoneyLabel;
    private JTextArea inputMoneyTextArea;
    private JButton numberPadButton;

    private JButton adminButton;
    private JButton cancelButton;
    private DisplayPanelListener displayPanelListener;

    public DisplayPanel() {
        setPreferredSize(new Dimension(0, 0));
        initComponents();
        layoutComponents();
        borders();
    }

    private void borders() {
        Border inner = BorderFactory.createTitledBorder("Display");
        Border outer = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        setBorder(BorderFactory.createCompoundBorder(outer, inner));
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(5, 10));
        add(numberPadPanel, BorderLayout.CENTER);
        add(textPanel, BorderLayout.NORTH);
        add(toolBarPanel, BorderLayout.SOUTH);
    }

    private void initComponents() {
        textPanel = createTextPanel();
        numberPadPanel = createNumPadPanel();
        toolBarPanel = createToolBarPanel();
    }

    // TOOL BAR
    private JPanel createToolBarPanel() {
        toolBarPanel = new JPanel();
        toolBarPanel.setPreferredSize(new Dimension(200, 40));
        adminButton = new JButton("Admin");
        adminButton.setFocusable(false);
        adminButton.setActionCommand("ADMIN");
        adminButton.setFont(toolBarFont);
        adminButton.addActionListener(new ToolBarListener());

        cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("CANCEL");
        cancelButton.setFocusable(false);
        cancelButton.setEnabled(false);
        cancelButton.setFont(toolBarFont);
        cancelButton.addActionListener(new ToolBarListener());

        toolBarPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        toolBarPanel.add(adminButton);
        toolBarPanel.add(cancelButton);

        return toolBarPanel;
    }

    // NUMBER PAD
    private JPanel createNumPadPanel() {
        numberPadPanel = new JPanel();
        numberPadPanel.setPreferredSize(new Dimension(200, 220));
        for (String label : buttonLabels) {
            numberPadButton = createNumButton(label);
            if (label.equalsIgnoreCase("del") || label.equalsIgnoreCase("ok")) {
                numberPadButton.setEnabled(false);
            }
        }

        numberPadPanel.setLayout(new GridLayout(4, 3, 4, 4));
        for (JButton button : numPadButtons) {
            numberPadPanel.add(button);
        }
        return numberPadPanel;
    }

    private JButton createNumButton(String label) {
        numberPadButton = new JButton(label);
        numberPadButton.setActionCommand(label);
        numberPadButton.setFocusable(false);
        numberPadButton.setFont(numberPadFont);
        numberPadButton.addActionListener(new NumberPadListener());
        numPadButtons.add(numberPadButton);
        return numberPadButton;
    }

    // TEXT PANEL
    private JPanel createTextPanel() {
        textPanel = new JPanel();
        textPanel.setPreferredSize(new Dimension(200, 150));
        selectedItemLabel = new JLabel("Selected item");
        selectedItemLabel.setFont(labelFont);

        selectedItemTextArea = new JTextArea(1, 10);
        selectedItemTextArea.setEditable(false);
        selectedItemTextArea.setFont(textAreaFont);

        inputMoneyLabel = new JLabel("Input");
        inputMoneyLabel.setFont(labelFont);

        inputMoneyTextArea = new JTextArea(1, 10);
        inputMoneyTextArea.setEditable(false);
        inputMoneyTextArea.setFont(textAreaFont);

        textPanel.setLayout(new GridLayout(4, 1, 4, 4));
        textPanel.add(selectedItemLabel);
        textPanel.add(selectedItemTextArea);
        textPanel.add(inputMoneyLabel);
        textPanel.add(inputMoneyTextArea);

        return textPanel;
    }

    //    METODE ZA AKTIVACIJU
// Metoda za postavljanje teksta u inputMoneyTextArea
    public void setInputMoneyText(float amount) {
        inputMoneyTextArea.setText(AUX_CLS.formatMoney(amount));
    }

    public void reset() {
        for (JButton button : numPadButtons) {
            if (button.getActionCommand().equalsIgnoreCase("del") || button.getActionCommand().equalsIgnoreCase("ok")) {
                button.setEnabled(false);
            }
        }
        cancelButton.setEnabled(false);
        selectedItemTextArea.setText("");
        inputMoneyTextArea.setText("");
    }

    public void activateButtons() {
        for (String label : buttonLabels) {
            numberPadButton = createNumButton(label);
            if (label.equalsIgnoreCase("del") || label.equalsIgnoreCase("ok")) {
                numberPadButton.setEnabled(true);
            }
        }
        cancelButton.setEnabled(true);

    }

//    PRIVATNE KLASE

    public void setDisplayPanelListener(DisplayPanelListener listener) {
        this.displayPanelListener = listener;
    }

    private class NumberPadListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals("DEL")) {
                String currentText = selectedItemTextArea.getText();
                if (currentText.length() > 0) {
                    selectedItemTextArea.setText(currentText.substring(0, currentText.length() - 1));
                    if (selectedItemTextArea.getText().isEmpty()) {
                        numPadButtons.stream().filter(btn -> btn.getActionCommand().equals("DEL")).findFirst().ifPresent(btn -> btn.setEnabled(false));
                        numPadButtons.stream().filter(btn -> btn.getActionCommand().equals("OK")).findFirst().ifPresent(btn -> btn.setEnabled(false));
                        if (inputMoneyTextArea.getText().isEmpty()) {
                            cancelButton.setEnabled(false);
                        }
                    }
                }
            } else if (command.equals("OK")) {
                // Handle OK button click
                String action = "OK";
                String inputMoney = inputMoneyTextArea.getText();
                String itemId = selectedItemTextArea.getText();
                // Create an event and pass to listener
                if (displayPanelListener != null) {
                    displayPanelListener.displayPanelEventOccurred(new DisplayPanelEvent(this, action, itemId, inputMoney));
                }
                numPadButtons.forEach(btn -> {
                    if (!btn.getActionCommand().equals("OK") && !btn.getActionCommand().equals("DEL")) {
                        btn.setEnabled(true);
                    }
                });
            } else {
                selectedItemTextArea.append(command);
                numPadButtons.stream().filter(btn -> btn.getActionCommand().equals("DEL")).findFirst().ifPresent(btn -> btn.setEnabled(true));
                numPadButtons.stream().filter(btn -> btn.getActionCommand().equals("OK")).findFirst().ifPresent(btn -> btn.setEnabled(true));
                cancelButton.setEnabled(true);
            }
        }
    }

    private class ToolBarListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String actionCommand = e.getActionCommand();
            if (actionCommand.equals("ADMIN")) {
                DisplayPanelEvent displayPanelEvent = new DisplayPanelEvent(this, actionCommand);
                if (displayPanelListener != null) {
                    displayPanelListener.displayPanelEventOccurred(displayPanelEvent);
                }
            } else if (actionCommand.equals("CANCEL")) {
                String itemId = selectedItemTextArea.getText();
                String inputMoney = inputMoneyTextArea.getText();
                DisplayPanelEvent displayPanelEvent = new DisplayPanelEvent(this, actionCommand, itemId, inputMoney);
                if (displayPanelListener != null) {
                    displayPanelListener.displayPanelEventOccurred(displayPanelEvent);
                }
            }
        }
    }
}
