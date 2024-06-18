package VendingMachine;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemsPanel extends JPanel {
    private static List<JPanel> itemsPanels;
    private final int NUM_PLACES = 12;
    private final Font numbersFont = new Font("Calibri", Font.BOLD, 30);
    private final Font textFont = new Font("Calibri", Font.PLAIN, 20);
    private final Font detailsFont = new Font("Calibri", Font.BOLD, 14);
    DecimalFormat decimalFormat;
    DecimalFormatSymbols symbols;
    private JLabel productIdLabel;
    private JLabel productNameLabel;
    private JLabel priceLabel;
    private JLabel quantityLabel;
    private JPanel itemPanel;
    private Item item;

    public ItemsPanel() {
        styleCurrency();
        initComponents();
        layoutComponents();
        borders();
    }

    private void styleCurrency() {
        symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        decimalFormat = new DecimalFormat("0.00", symbols);
    }

    private void borders() {
        Border inner = BorderFactory.createTitledBorder("Items");
        Border outer = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        setBorder(BorderFactory.createCompoundBorder(outer, inner));
    }

    private void layoutComponents() {
        setLayout(new GridLayout(4, 3, 8, 8));
        setPreferredSize(new Dimension(0, 0));
        for (JPanel panel : itemsPanels) {
            add(panel);
        }
    }

    private void initComponents() {
        List<AllItems> items = AllItems.getAllItems();
        itemsPanels = new ArrayList<>();
        for (AllItems itemFromEnum : items) {
            item = new Item(itemFromEnum.getItemName(), itemFromEnum.getPrice());
            if (item.getId() <= NUM_PLACES) {
                itemsPanels.add(createItemPanel());
            } else {
                System.out.println("There is no space for item in vending machine");
            }
        }

    }

    private JPanel createItemPanel() {
        itemPanel = new JPanel(new GridLayout(4, 1));
        itemPanel.setBorder(BorderFactory.createEtchedBorder());

        productIdLabel = new JLabel(String.valueOf(item.getId()));
        productNameLabel = new JLabel(item.getItemName());

        float price = item.getPrice();

        priceLabel = new JLabel("Price: " + decimalFormat.format(price) + " €");
        quantityLabel = new JLabel("Quantity: " + item.getQuantity());

        productIdLabel.setHorizontalAlignment(SwingConstants.CENTER);
        productNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        priceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        quantityLabel.setHorizontalAlignment(SwingConstants.CENTER);

        styleComponents();
        itemPanel.add(productIdLabel);
        itemPanel.add(productNameLabel);
        itemPanel.add(priceLabel);
        itemPanel.add(quantityLabel);

        return itemPanel;
    }

    private void styleComponents() {
        quantityLabel.setFont(detailsFont);
        productIdLabel.setFont(numbersFont);
        productNameLabel.setFont(textFont);
        priceLabel.setFont(detailsFont);
    }

    public void updatePanel(Item item) {
        this.item = item;
        for (JPanel itemPanel : itemsPanels) {
            JLabel productIdLabel = (JLabel) itemPanel.getComponent(0);
            if (Objects.equals(productIdLabel.getText(), String.valueOf(item.getId()))) {
                JLabel label = (JLabel) itemPanel.getComponent(3);
                if (item.isAvailable()) {
                    label.setText("Quantity: " + item.getQuantity());
                } else {
                    label.setText("Item is out of stock");
                }
            }
        }
    }
}
