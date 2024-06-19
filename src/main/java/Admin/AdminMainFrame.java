package Admin;

import VendingMachine.Transaction;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.List;

public class AdminMainFrame extends JFrame {
    private JFileChooser fileChooser1;
    private JFileChooser fileChooser2;
    private ViewPanel adminViewPanel;
    private FilterPanel adminFilterPanel;
    private AdminMenuBar adminMenuBar;
    private String filePath = "src/main/resources/Data/transactions.csv";

    public AdminMainFrame() {
        super("Admin");

        setSize(900, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);

        initFileChoosers();
        initComponents();
        layoutComponents();
        activateComponents();
        setLogo();
    }

    private void initFileChoosers() {
        fileChooser1 = new JFileChooser();
        fileChooser2 = new JFileChooser();
        customizeJFileChooser(fileChooser1, true);
        customizeJFileChooser(fileChooser2, false);
    }

    private void activateComponents() {
        adminFilterPanel.setFilterPanelListener(new FilterPanelListener() {
            @Override
            public void transactionFilterPanelEventOccurred(TransactionFilterPanelEvent transactionFilterPanelEvent) {
                String[] filters = transactionFilterPanelEvent.getFilters();
                List<Transaction> filteredTransactions = AUX_CLS_ADMIN.filterTransactions(filePath, filters);
                if (filteredTransactions.isEmpty()) {
                    JOptionPane.showMessageDialog(AdminMainFrame.this, "No transactions found matching the filter criteria. Resetting filter.", "Filter reset", JOptionPane.INFORMATION_MESSAGE);
                    adminFilterPanel.resetTransactionFilters();
                } else {
                    adminViewPanel.addTransactions(filteredTransactions);
                }
            }

            @Override
            public void itemFilterPanelEventOccurred(ItemFilterPanelEvent itemFilterPanelEvent) {
                String[] filters = itemFilterPanelEvent.getFilters();
                List<Transaction> filteredTransactions = AUX_CLS_ADMIN.filterTransactionsByItems(filePath, filters);
                if (filteredTransactions.isEmpty()) {
                    JOptionPane.showMessageDialog(AdminMainFrame.this, "No transactions found matching the item filter criteria. Resetting item filter.", "Item Filter", JOptionPane.INFORMATION_MESSAGE);
                    adminFilterPanel.resetItemFilters();
                } else {
                    adminViewPanel.addTransactions(filteredTransactions);
                }
            }
        });

        adminMenuBar.setMenuBarListener(menuBarActionCommandString -> {
            if (menuBarActionCommandString.equalsIgnoreCase("EXIT")) {
                int val = JOptionPane.showConfirmDialog(AdminMainFrame.this, "Do you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
                if (val == JOptionPane.YES_OPTION) {
                    AdminMainFrame.this.dispose();
                }
            } else if (menuBarActionCommandString.equalsIgnoreCase("IMPORT_ALL")) {
                boolean success = adminViewPanel.loadDataFromCSV(filePath);
                if (success) {
                    JOptionPane.showMessageDialog(AdminMainFrame.this, "Transactions loaded successfully", "Load Transactions", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(AdminMainFrame.this, "Error loading transactions", "Load Transactions Error", JOptionPane.ERROR_MESSAGE);
                }
            } else if (menuBarActionCommandString.equalsIgnoreCase("IMPORT_SELECTED")) {
                int returnValue = fileChooser1.showOpenDialog(AdminMainFrame.this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser1.getSelectedFile();
                    String selectedFilePath = selectedFile.getAbsolutePath();
                    if (!selectedFilePath.endsWith(".csv")) {
                        JOptionPane.showMessageDialog(AdminMainFrame.this, "Error: Selected file is not a CSV file.", "File Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    filePath = selectedFilePath;
                    adminViewPanel.loadDataFromCSV(filePath);
                    JOptionPane.showMessageDialog(AdminMainFrame.this, "Transactions loaded successfully", "Load Transactions", JOptionPane.INFORMATION_MESSAGE);
                }
            } else if (menuBarActionCommandString.equalsIgnoreCase("EXPORT")) {
                int returnValue = fileChooser2.showSaveDialog(AdminMainFrame.this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser2.getSelectedFile();
                    if (!fileToSave.getAbsolutePath().endsWith(".csv")) {
                        fileToSave = new File(fileToSave.getAbsolutePath() + ".csv");
                    }
                    AUX_CLS_ADMIN.exportDataToCSV(fileToSave.getAbsolutePath(), adminViewPanel.getTransactionsFromTable());
                    JOptionPane.showMessageDialog(AdminMainFrame.this, "Transactions saved successfully", "Save Transactions", JOptionPane.INFORMATION_MESSAGE);
                }
            } else if (menuBarActionCommandString.equalsIgnoreCase("CLEAR_TABLE")) {
                adminViewPanel.clearData();
                JOptionPane.showMessageDialog(AdminMainFrame.this, "Table data cleared.");
            } else if (menuBarActionCommandString.equalsIgnoreCase("CLEAR_FILTER")) {
                adminFilterPanel.resetAll();
                JOptionPane.showMessageDialog(AdminMainFrame.this, "Filter reset.");
            } else if (menuBarActionCommandString.equalsIgnoreCase("REMOVE_DUPLICATES")) {
                List<Transaction> transactions = adminViewPanel.getTransactionsFromTable();
                List<Transaction> withoutDuplicates = AUX_CLS_ADMIN.removeDuplicatesFromList(transactions);
                adminViewPanel.clearData();
                adminViewPanel.addTransactions(withoutDuplicates);
            }
        });
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(5, 5));
        setJMenuBar(adminMenuBar);
        add(adminViewPanel, BorderLayout.CENTER);
        add(adminFilterPanel, BorderLayout.SOUTH);
    }

    private void initComponents() {
        adminMenuBar = new AdminMenuBar();
        adminViewPanel = new ViewPanel();
        adminFilterPanel = new FilterPanel();
    }

    private void setLogo() {
        ImageIcon imageIcon = new ImageIcon("src/main/resources/Images/transaction.png");
        setIconImage(imageIcon.getImage());
    }

    private void customizeJFileChooser(JFileChooser fileChooser, boolean onlyCSV) {
        fileChooser.setCurrentDirectory(new File(filePath));
        FileNameExtensionFilter filterCSV = new FileNameExtensionFilter("CSV files", "csv");
        fileChooser.addChoosableFileFilter(filterCSV);
        if (!onlyCSV) {
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("All files", "*.*"));
        }
        fileChooser.setFileFilter(filterCSV);
    }
}
