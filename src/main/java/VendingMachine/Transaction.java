package VendingMachine;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Transaction implements Serializable {
    private static final String filePathForBinDat = "src/main/resources/Data/transactions.bin";
    private static final String filePathForCsvDat = "src/main/resources/Data/transactions.csv";
    private static final String SUCCESSFUL_TRANSACTION = "success";
    private static final String CANCELLED_TRANSACTION = "cancelled";
    private static final String OUT_OF_STOCK_TRANSACTION = "stockError";
    private static final String ITEM_NOT_FOUND_TRANSACTION = "itemError";
    private static final String NOT_ENOUGH_TOTAL_MONEY_TRANSACTION = "moneyError";
    private static final ArrayList<Transaction> transactions = AUX_CLS.loadTransactions(filePathForBinDat);
    private final int transactionId;
    private final String transactionStatus;
    private final String dateAndTime;
    private final float inputMoney;
    private final float change;
    private final int itemId;
    private final float itemPrice;
    private final String itemName;
    private boolean isSuccessful;
    private Item item;
    private int remainingQuantity;
    private int itemQuantity;

    public Transaction(int userIdInput, float userMoneyInput) {
        this.transactionId = AUX_CLS.getNextTransactionId(filePathForBinDat);
        this.dateAndTime = generateDateAndTime();
        item = Item.getItemById(userIdInput);
        this.inputMoney = userMoneyInput;

        if (item == null) {
            this.transactionStatus = ITEM_NOT_FOUND_TRANSACTION;
            this.isSuccessful = false;
            this.itemId = 0;
            this.itemName = "";
            this.itemPrice = 0.0f;
            this.itemQuantity = 0;
            this.remainingQuantity = 0;
            this.change = calculateChange();
        } else {
            this.itemId = item.getId();
            this.itemName = item.getItemName();
            this.itemPrice = item.getPrice();
            this.itemQuantity = item.getQuantity();

            if (!item.isAvailable()) {
                this.transactionStatus = OUT_OF_STOCK_TRANSACTION;
                this.isSuccessful = false;
                this.change = inputMoney;
            } else if (userMoneyInput < itemPrice) {
                this.transactionStatus = NOT_ENOUGH_TOTAL_MONEY_TRANSACTION;
                this.isSuccessful = false;
                this.change = calculateChange();
                this.remainingQuantity = itemQuantity;
            } else {
                this.transactionStatus = SUCCESSFUL_TRANSACTION;
                this.isSuccessful = true;
                this.remainingQuantity = --itemQuantity;
                item.setQuantity(remainingQuantity);
                this.change = calculateChange();
            }
        }
        transactions.add(this);
        AUX_CLS.saveTransactions(transactions, filePathForBinDat);
        AUX_CLS.saveTransactionsToCSV(transactions, filePathForCsvDat);
    }

    public Transaction(float inputMoney) {
        this.transactionId = AUX_CLS.getNextTransactionId(filePathForBinDat);
        this.dateAndTime = generateDateAndTime();
        this.inputMoney = inputMoney;
        this.isSuccessful = false;
        this.transactionStatus = CANCELLED_TRANSACTION;
        this.item = null;
        this.itemId = 0;
        this.itemQuantity = 0;
        this.itemName = "";
        this.itemPrice = 0.0f;
        this.remainingQuantity = 0;
        this.change = inputMoney;

        transactions.add(this);
        AUX_CLS.saveTransactions(transactions, filePathForBinDat);
        AUX_CLS.saveTransactionsToCSV(transactions, filePathForCsvDat);
    }

    public Transaction(int transactionId, String dateAndTime, String transactionStatus, float inputMoney, float change, int itemId, String itemName, float itemPrice, int remainingQuantity) {
        this.transactionId = transactionId;
        this.dateAndTime = dateAndTime;
        this.transactionStatus = transactionStatus;
        this.inputMoney = inputMoney;
        this.change = change;
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.remainingQuantity = remainingQuantity;
    }

    public static ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    private String generateDateAndTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");
        return now.format(formatter);
    }

    public float calculateChange() {
        float change;
        if (isSuccessful) {
            change = inputMoney - itemPrice;
        } else {
            change = 0.0f;
        }
        return change;
    }


    public int getTransactionId() {
        return transactionId;
    }


    public String getTransactionStatus() {
        return transactionStatus;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public float getInputMoney() {
        return inputMoney;
    }

    public float getChange() {
        return change;
    }

    public Item getItem() {
        return item;
    }

    public int getRemainingQuantity() {
        return remainingQuantity;
    }

    public int getItemId() {
        return itemId;
    }

    public float getItemPrice() {
        return itemPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", transactionStatus='" + transactionStatus + '\'' +
                ", dateAndTime='" + dateAndTime + '\'' +
                ", inputMoney=" + inputMoney +
                ", change=" + change +
                ", remainingQuantity=" + remainingQuantity +
                ", itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", itemPrice=" + itemPrice +
                '}';
    }
}
