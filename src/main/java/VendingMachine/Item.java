package VendingMachine;

import java.io.Serializable;
import java.util.ArrayList;

public class Item implements Serializable {
    private static final ArrayList<Item> items = new ArrayList<>();
    private static int cntId = 1;
    private final int INITIAL_QUANTITY = 4;
    private final String itemName;
    private final int id;
    private final float price;
    private int quantity;

    public Item(String itemName, float price) {
        this.id = cntId++;
        this.itemName = itemName;
        this.price = price;
        this.quantity = INITIAL_QUANTITY;
        items.add(this);
    }

    public Item(int itemId, String itemName, float itemPrice, int itemQuantity) {
        this.id = itemId;
        this.itemName = itemName;
        this.price = itemPrice;
        this.quantity = itemQuantity;
    }

    public static Item getItemById(int userInput) {
        Item matchItem = null;
        for (Item item : items) {
            if (userInput == item.id) {
                matchItem = item;
            }
        }
        return matchItem;
    }

    public static ArrayList<Item> getItems() {
        return items;
    }

    public String getItemName() {
        return itemName;
    }

    public int getId() {
        return id;
    }

    public float getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isAvailable() {
        return (quantity > 0);
    }

    @Override
    public String toString() {
        return "Item{" +
                "INITIAL_QUANTITY=" + INITIAL_QUANTITY +
                ", itemName='" + itemName + '\'' +
                ", id=" + id +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
