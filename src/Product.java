public class Product {
    private final int productId;
    private String productName;
    private double price;
    private int quantity;
    String category;

    Product (int productId, String productName, double price, int quantity, String category) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.category = category;
    }

    int getProductId(){
        return this.productId;
    }
    String getProductName(){
        return this.productName;
    }
    double getPrice(){
        return this.price;
    }
    int getQuantity(){
        return this.quantity;
    }
    String getCategory(){
        return this.category;
    }

    void setProductName(String productName) { this.productName = productName; }
    void setPrice(double price){ this.price = price; }
    void setQuantity(int quantity) { this.quantity = quantity; }
    void setCategory(String category) { this.category = category; }

}