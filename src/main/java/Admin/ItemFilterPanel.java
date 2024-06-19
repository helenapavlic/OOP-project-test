package Admin;

import VendingMachine.Item;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ItemFilterPanel extends JPanel {
    private JList<String> itemsList;
    private JScrollPane itemsListScrollPane;
    private JCheckBox useInStockFilter;
    private JRadioButton itemInStock;
    private JRadioButton itemOutOfStock;
    private ButtonGroup buttonGroup;
    private JButton apply;
    private FilterPanelListener filterPanelListener;

    public ItemFilterPanel() {
        initComponents();
        layoutComponents();
        decorate();
        activatePanel();
    }

    private void activatePanel() {
        apply.setEnabled(false);

        itemsList.addListSelectionListener(e -> checkInputs());
        useInStockFilter.addActionListener(e -> {
            boolean selected = useInStockFilter.isSelected();
            itemInStock.setEnabled(selected);
            itemOutOfStock.setEnabled(selected);
            if (selected) {
                itemOutOfStock.setSelected(true);
            } else {
                buttonGroup.clearSelection();
            }
            checkInputs();
        });
        itemInStock.addActionListener(e -> checkInputs());
        itemOutOfStock.addActionListener(e -> checkInputs());

        apply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> selectedItems = itemsList.getSelectedValuesList();
                String itemsFilter = !selectedItems.isEmpty() ? String.join(";", selectedItems) : null;
                String useStockFilter = useInStockFilter.isSelected() ? "true" : null;
                String stockStatusFilter = useInStockFilter.isSelected() ? (itemInStock.isSelected() ? "In stock" : "Out of stock") : null;

                String[] filters = {itemsFilter, useStockFilter, stockStatusFilter};

                if (filterPanelListener != null) {
                    filterPanelListener.itemFilterPanelEventOccurred(new ItemFilterPanelEvent(this, filters));
                }
            }
        });
    }

    private void checkInputs() {
        boolean hasSelection = itemsList.getSelectedIndex() != -1 || useInStockFilter.isSelected();
        apply.setEnabled(hasSelection);
    }

    private void decorate() {
        Border inner = BorderFactory.createTitledBorder("Item filter");
        Border outer = BorderFactory.createEmptyBorder(2, 2, 2, 2);
        setBorder(BorderFactory.createCompoundBorder(outer, inner));
    }

    private void layoutComponents() {
        setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();

        gc.gridx = 0;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_START;

        gc.gridy++;
        add(new JLabel("Items:"), gc);
        add(Box.createHorizontalStrut(50), gc);
        gc.gridx++;
        add(itemsListScrollPane, gc);

        add(Box.createVerticalStrut(60), gc);

        gc.gridx = 0;
        gc.gridy++;
        add(useInStockFilter, gc);

        add(Box.createVerticalStrut(40), gc);

        gc.gridy++;
        add(itemInStock, gc);
        gc.gridx++;
        add(itemOutOfStock, gc);

        add(Box.createVerticalStrut(40), gc);

        gc.gridy++;
        gc.gridx++;
        add(apply, gc);
    }

    private void initComponents() {
        setPreferredSize(new Dimension(440, getHeight()));
        itemsList = new JList<>();
        itemsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        DefaultListModel<String> catModel = new DefaultListModel<>();
        ArrayList<Item> items = Item.getItems();
        for (Item item : items) {
            catModel.addElement(item.getItemName());
        }
        itemsList.setModel(catModel);
        itemsList.setVisibleRowCount(4);
        itemsListScrollPane = new JScrollPane(itemsList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        itemsListScrollPane.setBorder(BorderFactory.createEtchedBorder());

        useInStockFilter = new JCheckBox("Use item in stock filter");
        useInStockFilter.setActionCommand("USE_STOCK_FILTER");
        useInStockFilter.setSelected(false);

        itemInStock = new JRadioButton("In stock");
        itemOutOfStock = new JRadioButton("Out of stock");
        itemInStock.setActionCommand("IN_STOCK");
        itemOutOfStock.setActionCommand("OUT_OF_STOCK");

        buttonGroup = new ButtonGroup();
        buttonGroup.add(itemInStock);
        buttonGroup.add(itemOutOfStock);
        itemInStock.setEnabled(false);
        itemOutOfStock.setEnabled(false);

        apply = new JButton("Apply");
        apply.setActionCommand("APPLY_ITEM");
    }

    public void reset() {
        itemsList.clearSelection();
        useInStockFilter.setSelected(false);
        itemInStock.setEnabled(false);
        itemOutOfStock.setEnabled(false);
        buttonGroup.clearSelection();
        apply.setEnabled(false);
    }

    public void setFilterPanelListener(FilterPanelListener filterPanelListener) {
        this.filterPanelListener = filterPanelListener;
    }
}
