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

    boolean addProduct(String inputProduct) {
        String[] fields = inputProduct.split(":");

        for (String field : fields) {
            String[] innerField = field.split(",");

            String productName = null;
            double price = 0;
            int quantity = 0;
            String category = null;

            for (String inner : innerField) {
                String[] finalField = inner.split("=");

                if (finalField.length != 2) {
                    return false;
                }

                String key = finalField[0].trim().toLowerCase();
                String value = finalField[1].trim();

                try {
                    switch (key) {
                        case "name" -> productName = value;
                        case "price" -> price = Double.parseDouble(value);
                        case "quantity" -> quantity = Integer.parseInt(value);
                        case "category" -> category = value.toUpperCase();
                        default -> IO.println("Unknown field: " + key);
                    }

                } catch (NumberFormatException e) {
                    IO.println("Invalid number format for: " + key);
                    return false;
                }
            }

            if (productName == null || category == null) {
                IO.println("Missing required fields");
                return false;
            }

            Product product = new Product(productId, productName, price, quantity, category);

            productId++;
            products.add(product);
        }

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

    boolean deleteProduct(String deleteInput){
        String[] fields = deleteInput.split("=");

        if(fields.length != 2){
            return false;
        }

        String key = fields[0].trim().toLowerCase();
        String value = fields[1].trim().toLowerCase();

        Predicate<Product> condition;

        switch (key) {
            case "name" -> condition = product ->
                    product.getProductName().equalsIgnoreCase(value);
            case "id" -> {
                int id = Integer.parseInt(value);
                condition = product -> product.getProductId() == id;
            }
            default -> {
                return false;
            }
        }

        return products.removeIf(condition);
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
