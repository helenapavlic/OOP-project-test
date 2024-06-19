package Admin;

import javax.swing.*;
import java.awt.*;

public class FilterPanel extends JPanel {
    private TransactionFilterPanel transactionFilterPanel;
    private ItemFilterPanel itemFilterPanel;
    private FilterPanelListener filterPanelListener;


    public FilterPanel() {
        initComponents();
        layoutComponents();
    }


    private void layoutComponents() {
        setLayout(new BorderLayout(5, 5));
        add(itemFilterPanel, BorderLayout.CENTER);
        add(transactionFilterPanel, BorderLayout.WEST);
    }

    private void initComponents() {
        setPreferredSize(new Dimension(getWidth(), 250));
        transactionFilterPanel = new TransactionFilterPanel();
        itemFilterPanel = new ItemFilterPanel();
    }

    public void resetAll() {
        itemFilterPanel.reset();
        transactionFilterPanel.reset();
    }

    public void resetItemFilters() {
        itemFilterPanel.reset();
    }

    public void resetTransactionFilters() {
        transactionFilterPanel.reset();
    }

    public void setFilterPanelListener(FilterPanelListener filterPanelListener) {
        this.filterPanelListener = filterPanelListener;
        transactionFilterPanel.setFilterPanelListener(filterPanelListener);
        itemFilterPanel.setFilterPanelListener(filterPanelListener);
    }
}


