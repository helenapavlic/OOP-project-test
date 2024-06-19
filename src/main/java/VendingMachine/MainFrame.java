package VendingMachine;

import Admin.AdminMainFrame;
import AdminLogin.AdminLoginEvent;
import AdminLogin.AdminLoginFrame;
import AdminLogin.AdminLoginListener;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private ItemsPanel itemsPanel;

    private DisplayPanel displayPanel;
    private CoinsPanel coinsPanel;
    private float totalMoneyInserted = 0.0f;

    public MainFrame() {
        super("Vending machine");
        setLogo();
        initComps();
        layoutComps();
        activateFrame();
    }

    private void layoutComps() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.8;
        gbc.weighty = 0.6;
        add(displayPanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0.8;
        gbc.weighty = 0.4;
        add(coinsPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 3;
        gbc.weightx = 1.2;
        gbc.weighty = 1.0;
        add(itemsPanel, gbc);
    }

    private void initComps() {
        setSize(800, 800);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        itemsPanel = new ItemsPanel();
        coinsPanel = new CoinsPanel();
        displayPanel = new DisplayPanel();
    }

    private void setLogo() {
        ImageIcon imageIcon = new ImageIcon("src/main/resources/Images/vending-machine.png");
        setIconImage(imageIcon.getImage());
    }

    private void activateFrame() {
        displayPanel.setDisplayPanelListener(new DisplayPanelListener() {
            @Override
            public void displayPanelEventOccurred(DisplayPanelEvent displayPanelEvent) {
                String buttonPressed = displayPanelEvent.getButtonPressed();

                if (buttonPressed.equals("OK")) {
                    String totalMoneyInput = displayPanelEvent.getInputMoney();
                    String itemIdInput = displayPanelEvent.getInputItemIdNumber();
                    float inputMoneyFloat = AUX_CLS.parseMoneyString(totalMoneyInput);
                    int itemIdInteger = AUX_CLS.readItemIdInt(itemIdInput);
                    Transaction transaction = new Transaction(itemIdInteger, inputMoneyFloat);
                    String transactionStatus = transaction.getTransactionStatus();
                    float change = transaction.getChange();
                    String formattedChange = AUX_CLS.formatMoney(change);
                    handleTransactionStatus(transactionStatus, formattedChange, transaction);

                } else if (buttonPressed.equals("CANCEL")) {
                    String totalMoneyInput = displayPanelEvent.getInputMoney();
                    float inputMoneyFloat = AUX_CLS.parseMoneyString(totalMoneyInput);
                    Transaction transaction = new Transaction(inputMoneyFloat);
                    float change = transaction.getChange();
                    String formattedChange = AUX_CLS.formatMoney(change);
                    JOptionPane.showMessageDialog(null, "Cancelled transaction! \nChange: " + formattedChange, "Cancelled transaction", JOptionPane.INFORMATION_MESSAGE);
                    displayPanel.reset();
                    totalMoneyInserted = 0.0f;

                } else if (buttonPressed.equals("ADMIN")) {
                    AdminLoginFrame adminLoginFrame = new AdminLoginFrame();
                    adminLoginFrame.setAdminLoginListener(new AdminLoginListener() {
                        @Override
                        public void adminLoginEventOccurred(AdminLoginEvent event) {
                            boolean isSuccess = event.isLoginSuccessful();
                            if (isSuccess){
//                                OPEN ADMIN FRAME
                                new AdminMainFrame();
                                System.out.println("SUCCESS");
                            }
                        }
                    });

//                    admin!!
                }

//                System.out.println("button pressed: " + displayPanelEvent.getButtonPressed());
//                System.out.println("input money: " + displayPanelEvent.getInputMoney());
//                System.out.println("item id: " + displayPanelEvent.getInputItemIdNumber());
            }
        });

        coinsPanel.setCoinsPanelListener(new CoinsPanelListener() {
            @Override
            public void coinsPanelEventOccurred(CoinsPanelEvent coinsPanelEvent) {
                updateInputMoneyTextArea(coinsPanelEvent.getCoinValue());
                displayPanel.activateButtons();
            }
        });
    }


    private void handleTransactionStatus(String transactionStatus, String formattedChange, Transaction transaction) {
        if (transactionStatus.equalsIgnoreCase("success")) {
            itemsPanel.updatePanel(transaction.getItem());
            JOptionPane.showMessageDialog(this, "Successful transaction! \nChange: " + formattedChange, "Successful transaction", JOptionPane.INFORMATION_MESSAGE);
            displayPanel.reset();
            totalMoneyInserted = 0.0f;

        } else if (transactionStatus.equalsIgnoreCase("stockError")) {
            JOptionPane.showMessageDialog(this, "Item is out of stock! \nChange: " + formattedChange, "Item out of stock", JOptionPane.ERROR_MESSAGE);
            displayPanel.reset();
            totalMoneyInserted = 0.0f;

        } else if (transactionStatus.equalsIgnoreCase("moneyError")) {
            JOptionPane.showMessageDialog(this, "Not enough money for transaction!", "Not enough funds", JOptionPane.ERROR_MESSAGE);

        } else if (transactionStatus.equalsIgnoreCase("itemError")) {
            JOptionPane.showMessageDialog(this, "Check input number!", "Item does not exist", JOptionPane.ERROR_MESSAGE);

        } else {
            JOptionPane.showMessageDialog(this, "Unknown action!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateInputMoneyTextArea(String coinValue) {
        float coinAmount = convertCoinToFloat(coinValue);
        totalMoneyInserted += coinAmount;
        displayPanel.setInputMoneyText(totalMoneyInserted);
    }


    private float convertCoinToFloat(String coinValue) {
        switch (coinValue) {
            case "1c":
                return 0.01f;
            case "2c":
                return 0.02f;
            case "5c":
                return 0.05f;
            case "10c":
                return 0.10f;
            case "20c":
                return 0.20f;
            case "50c":
                return 0.50f;
            case "1€":
                return 1.00f;
            case "2€":
                return 2.00f;
            default:
                return 0.00f;
        }
    }
}
