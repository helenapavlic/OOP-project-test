package VendingMachine;

import java.util.Arrays;
import java.util.List;

public enum AllItems {

    PRODUCT1("Chips", 1.50F),
    PRODUCT2("Chocolate Bar", 2.00F),
    PRODUCT3("Cookies", 1.75F),
    PRODUCT4("Candy", 1.25F),
    PRODUCT5("Soda", 1.50F),
    PRODUCT6("Juice", 2.50F),
    PRODUCT7("Popcorn", 1.80F),
    PRODUCT8("Gum", 1.00F),
    PRODUCT9("Granola Bar", 2.20F),
    PRODUCT10("Pretzels", 1.60F),
    PRODUCT11("Nuts", 3.00F),
    PRODUCT12("Fruit Snack", 2.30F);

    private final String itemName;
    private final float price;

    AllItems(String itemName, float price) {
        this.itemName = itemName;
        this.price = price;
    }

    public static List<AllItems> getAllItems() {
        return Arrays.asList(AllItems.values());
    }

    public String getItemName() {
        return itemName;
    }

    public float getPrice() {
        return price;
    }
}
