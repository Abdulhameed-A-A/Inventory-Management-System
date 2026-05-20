import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class InventoryManagementSystem {
    private final ArrayList<Product> products;
    private int productId = 1;
    private final Set<String> categories = Set.of(
            "Electronics",
            "Office",
            "Tools",
            "Cleaning",
            "Furniture",
            "Accessories",
            "Food",
            "Medical",
            "Sporting",
            "Books"
    );

    InventoryManagementSystem(){
        this.products = new ArrayList<>();
    }


    boolean addProduct(String productName, double price, int quantity, String category){
        if(!categories.contains(category)){
            return false;
        }

        Product product = new Product(productId, productName, price, quantity, category);
        productId ++;
        products.add(product);
        return true;
    }

    void displayProduct(){
        for(Product product: products){
            IO.println(product.getProductId() + ". | Name: " + product.getProductName() + " | Price: " + product.getPrice() + " | Category: " + product.getCategory());
        }
    }

    void displayCategory(){
        IO.print("Categories:\n- ");
        for(String category: categories){
            IO.print(category + " , ");
        }
        IO.print("-\n");
    }

    List<Product> filterProducts(String updates){
        String[] fields = updates.split("=");
        if(fields.length != 2){
            return new ArrayList<>();
        }

        String key = fields[0].trim().toLowerCase();
        String value = fields[1].trim().toLowerCase();

        Predicate<Product> condition;

        switch (key) {
            case "name" -> condition = product ->
                    product.getProductName().equalsIgnoreCase(value);
            case "price" -> {
                double price = Double.parseDouble(value);
                condition = product -> product.getPrice() == price;
            }
            case "quantity" -> {
                int quantity = Integer.parseInt(value);
                condition = product -> product.getQuantity() == quantity;
            }
            default -> {
                return new ArrayList<>();
            }
        }

        return products.stream()
                .filter(condition)
                .collect(Collectors.toList());
    }

    void deleteProduct(int inputId){
        for(Product product: products){
            if(product.getProductId() == inputId){
                products.remove(product);
                return;
            }
        }
    }

    boolean updateProduct(int productId, String updates) {

        Optional<Product> foundProduct = products.stream()
                .filter(p -> p.getProductId() == productId)
                .findFirst();

        if(foundProduct.isEmpty()){
            IO.println("Present Not found");
            return false;
        }

        Product product = foundProduct.get();

        String[] fields = updates.split(",");

        for(String field : fields){
            String[] keyValue = field.split("=");

            if(keyValue.length != 2){
                continue;
            }
            String key = keyValue[0].trim().toLowerCase();
            String value = keyValue[1].trim().toLowerCase();

            switch (key){
                case "name" -> product.setProductName(value);
                case "price" -> product.setPrice(Double.parseDouble(value));
                case "quantity" -> product.setQuantity(Integer.parseInt(value));
                default -> IO.println("Unknown Fields");
            }
        }
        return true;
    }
}
