package VendingMachine;

import java.io.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AUX_CLS {

    public static void saveTransactions(ArrayList<Transaction> transactions, String filePath) {
        try (FileOutputStream fileOut = new FileOutputStream(filePath);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(transactions);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static ArrayList<Transaction> loadTransactions(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            try (FileInputStream fileInputStream = new FileInputStream(file);
                 ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
                return (ArrayList<Transaction>) objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
        return new ArrayList<>();
    }

    public static int getNextTransactionId(String filePath) {
        ArrayList<Transaction> transactions = null;
        try (FileInputStream fileIn = new FileInputStream(filePath);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            transactions = (ArrayList<Transaction>) in.readObject();
        } catch (FileNotFoundException e) {
            return 1;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        if (transactions != null && !transactions.isEmpty()) {
            Transaction lastTransaction = transactions.get(transactions.size() - 1);
            return lastTransaction.getTransactionId() + 1;
        } else {
            return 1;
        }
    }

    public static void saveTransactionsToCSV(ArrayList<Transaction> transactions, String filePath) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat decimalFormat = new DecimalFormat("0.00", symbols);
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("ID,DATE AND TIME,STATUS,INPUT MONEY,CHANGE,ITEM ID,ITEM NAME,ITEM PRICE,QUANTITY\n");
            for (Transaction transaction : transactions) {
                writer.append(String.valueOf(transaction.getTransactionId())).append(",");
                writer.append(transaction.getDateAndTime()).append(",");
                writer.append(transaction.getTransactionStatus()).append(",");
                writer.append(decimalFormat.format(transaction.getInputMoney())).append(",");
                writer.append(decimalFormat.format(transaction.getChange())).append(",");
                writer.append(String.valueOf(transaction.getItemId())).append(",");
                writer.append(transaction.getItemName()).append(",");
                writer.append(decimalFormat.format(transaction.getItemPrice())).append(",");
                writer.append(String.valueOf(transaction.getRemainingQuantity())).append("\n");
            }
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static List<String[]> readTransactionsFromCSV(String filePath) {
        List<String[]> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] values = line.split(",");
                records.add(values);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return records;
    }

    public static float parseMoneyString(String moneyString) {
        if (moneyString == null || moneyString.trim().isEmpty()) {
            return 0;
        }
        String cleanedString = moneyString.replace("€", "").trim();
        float value = Float.parseFloat(cleanedString);
        return value;
    }


    public static String formatMoney(float amount) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("0.00", symbols);
        return df.format(amount) + " €";
    }

    public static int readItemIdInt(String numberString) {
        if (numberString == null || numberString.trim().isEmpty()) {
            return 0;
        }
        return Integer.parseInt(numberString.trim());
    }
}
