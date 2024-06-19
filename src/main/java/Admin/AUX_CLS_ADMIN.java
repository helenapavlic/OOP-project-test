package Admin;

import VendingMachine.Transaction;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class AUX_CLS_ADMIN {

    public static ArrayList<Transaction> readTransactionsFromCsv(String filePath) {
        ArrayList<Transaction> transactions = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] values = line.split(",");

                int transactionId = Integer.parseInt(values[0]);
                String dateAndTime = values[1];
                String transactionStatus = values[2];
                float inputMoney = Float.parseFloat(values[3]);
                float change = Float.parseFloat(values[4]);
                int itemId = Integer.parseInt(values[5]);
                String itemName = values[6];
                float itemPrice = Float.parseFloat(values[7]);
                int remainingQuantity = Integer.parseInt(values[8]);

                Transaction transaction = new Transaction(transactionId, dateAndTime, transactionStatus, inputMoney, change, itemId, itemName, itemPrice, remainingQuantity);
                transactions.add(transaction);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading transactions: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return transactions;
    }

    public static void exportDataToCSV(String absolutePath, List<Transaction> transactions) {
        try (FileWriter writer = new FileWriter(absolutePath)) {
            writer.write("ID,DATE AND TIME,STATUS,INPUT MONEY,CHANGE,ITEM ID,ITEM NAME,ITEM PRICE,QUANTITY\n");
            for (Transaction transaction : transactions) {
                writer.write(transaction.getTransactionId() + "," +
                        transaction.getDateAndTime() + "," +
                        transaction.getTransactionStatus() + "," +
                        transaction.getInputMoney() + "," +
                        transaction.getChange() + "," +
                        transaction.getItemId() + "," +
                        transaction.getItemName() + "," +
                        transaction.getItemPrice() + "," +
                        transaction.getRemainingQuantity() + "\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving transactions: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static List<Transaction> filterTransactions(String filePath, String[] filters) {
        ArrayList<Transaction> transactions = readTransactionsFromCsv(filePath);
        List<Transaction> filteredTransactions = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date dateFrom = null, dateTo = null;
        try {
            if (filters[0] != null) dateFrom = sdf.parse(filters[0]);
            if (filters[1] != null) dateTo = sdf.parse(filters[1]);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        String statusFilter = null;
        if (filters[2] != null) {
            statusFilter = switch (filters[2]) {
                case "Successful transaction" -> "success";
                case "Not enough money error" -> "moneyError";
                case "Item out of stock error" -> "stockError";
                case "Item not found error" -> "itemError";
                case "Cancelled transaction" -> "cancelled";
                default -> statusFilter;
            };
        }
        for (Transaction transaction : transactions) {
            boolean matches = true;

            if (dateFrom != null && dateTo == null) {
                Date transactionDate = null;
                try {
                    transactionDate = sdf.parse(transaction.getDateAndTime());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                if (transactionDate == null || !sdf.format(transactionDate).equals(sdf.format(dateFrom))) {
                    matches = false;
                }
            } else if (dateFrom != null && dateTo != null) {
                Date transactionDate = null;
                try {
                    transactionDate = sdf.parse(transaction.getDateAndTime());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                if (transactionDate == null || transactionDate.before(dateFrom) || transactionDate.after(dateTo)) {
                    matches = false;
                }
            }
            if (statusFilter != null && !statusFilter.equals(transaction.getTransactionStatus())) {
                matches = false;
            }
            if (filters[3] != null && !filters[3].equals(String.valueOf(transaction.getTransactionId()))) {
                matches = false;
            }
            if (matches) {
                filteredTransactions.add(transaction);
            }
        }
        return filteredTransactions;
    }

    public static List<Transaction> filterTransactionsByItems(String filePath, String[] filters) {
        ArrayList<Transaction> transactions = readTransactionsFromCsv(filePath);
        List<Transaction> filteredTransactions = new ArrayList<>();

        String itemsFilter = filters[0];
        boolean useInStockFilter = filters[1] != null;
        String stockStatusFilter = filters[2];
        for (Transaction transaction : transactions) {
            boolean matches = true;
            if (itemsFilter != null) {
                String[] itemNames = itemsFilter.split(";");
                List<String> itemNamesList = Arrays.asList(itemNames);
                if (!itemNamesList.contains(transaction.getItemName())) {
                    matches = false;
                }
            }
            if (useInStockFilter) {
                if (stockStatusFilter.equals("In stock") && transaction.getRemainingQuantity() <= 0) {
                    matches = false;
                } else if (stockStatusFilter.equals("Out of stock") && transaction.getRemainingQuantity() > 0) {
                    matches = false;
                }
            }
            if (matches) {
                filteredTransactions.add(transaction);
            }
        }
        return filteredTransactions;
    }

    public static List<Transaction> removeDuplicatesFromList(List<Transaction> transactions) {
        List<Transaction> uniqueTransactions = new ArrayList<>();
        Set<Integer> transactionIds = new HashSet<>();
        for (Transaction transaction : transactions) {
            if (!transactionIds.contains(transaction.getTransactionId())) {
                uniqueTransactions.add(transaction);
                transactionIds.add(transaction.getTransactionId());
            }
        }
        return uniqueTransactions;
    }

}
