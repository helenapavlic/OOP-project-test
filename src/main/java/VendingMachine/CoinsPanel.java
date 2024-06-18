package VendingMachine;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CoinsPanel extends JPanel {

    private final String[] coinsArr = {"1c", "2c", "5c", "10c", "20c", "50c", "1€", "2€"};
    private final Font numbersFont = new Font("Calibri", Font.BOLD, 18);
    private JButton button;
    private ArrayList<JButton> buttons;

    private CoinsPanelListener coinsPanelListener;


    public CoinsPanel() {
        initComponents();
        layoutComponents();
        activatePanel();
        borders();
    }

    private void borders() {
        Border inner = BorderFactory.createTitledBorder("Coins");
        Border outer = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        setBorder(BorderFactory.createCompoundBorder(outer, inner));
    }

    private void activatePanel() {
        for (JButton button : buttons) {
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String actionFromButton = button.getActionCommand();
                    CoinsPanelEvent coinsPanelEvent = new CoinsPanelEvent(this, actionFromButton);
                    if (coinsPanelListener != null) {
                        coinsPanelListener.coinsPanelEventOccurred(coinsPanelEvent);
                    }
                }
            });
        }
    }

    private void layoutComponents() {
        setPreferredSize(new Dimension(0, 0));
        setLayout(new GridLayout(2, 4, 5, 5));
        for (JButton button : buttons) {
            add(button);
        }
    }

    private void initComponents() {
        buttons = new ArrayList<>();
        for (String string : coinsArr) {
            buttons.add(coinButton(string));
        }
    }

    private JButton coinButton(String textOnButton) {
        button = new JButton(textOnButton);
        button.setActionCommand(textOnButton);
        button.setFont(numbersFont);
        button.setFocusable(false);
        return button;
    }

    public void setCoinsPanelListener(CoinsPanelListener coinsPanelListener) {
        this.coinsPanelListener = coinsPanelListener;
    }
}

