import java.nio.MappedByteBuffer;
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

    String addProduct(String inputProduct) {
        String[] productEntries = inputProduct.split(":");

        for (String entry : productEntries) {
            Map<String, String> productData = parseInput(entry, ",");

            String productName = productData.get("name");
            String category = productData.get("category");

            if (productName == null || productName.isBlank()) {
                return "Product name is required";
            }

            if(category == null || category.isBlank()) {
                return "Category is required";
            }

            try {
                double price = Double.parseDouble(productData.getOrDefault("price", "0")
                );

                int quantity = Integer.parseInt(productData.getOrDefault("quantity", "0")
                );

                Product product = new Product(productId, productName, price, quantity, category.toUpperCase());

                products.add(product);
                productId++;
            } catch (NumberFormatException e) {
                return "Price or quantity has invalid number format";
            }
        }

        return "Product added Successfully";
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
        Map<String, String> data = parseInput(updates, ",");

        if(data.isEmpty()){
            return new ArrayList<>();
        }

        String key = data.keySet().iterator().next();
        String value = data.get(key);

        Predicate<Product> condition = buildCondition(key, value);

        return products.stream()
                .filter(condition)
                .collect(Collectors.toList());
    }

    boolean deleteProduct(String deleteInput){
        Map<String, String> data = parseInput(deleteInput, ",");

        if(data.isEmpty()){
            return false;
        }

        String key = data.keySet().iterator().next();
        String value = data.get(key);

        Predicate<Product> condition  = buildCondition(key, value);

        return products.removeIf(condition);
    }

    boolean updateProduct(int productId, String updates) {

        Optional<Product> foundProduct = findProductById(productId);

        if(foundProduct.isEmpty()){
            IO.println("Present Not found");
            return false;
        }

        Product product = foundProduct.get();

        Map<String, String> updatesmap = parseInput(updates, ",");

        for(Map.Entry<String, String> entry : updatesmap.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();

            switch (key){
                case "name" -> product.setProductName(value);
                case "price" -> product.setPrice(Double.parseDouble(value));
                case "quantity" -> product.setQuantity(Integer.parseInt(value));
                default -> IO.println("Unknown Fields");
            }
        }
        return true;
    }

    private Map<String, String> parseInput(String input, String delimiter) {
        Map<String, String> data = new HashMap<>();

        String[] fields = input.split(delimiter);

        for(String field : fields) {
            String[] keyValue = field.split("=");

            if(keyValue.length != 2) {
                continue;
            }

            String key = keyValue[0].trim().toLowerCase();
            String value = keyValue[1].trim();

            data.put(key, value);
        }

        return data;
    }

    private Optional<Product> findProductById(int id) {

        return products.stream()
                .filter(p -> p.getProductId() == id)
                .findFirst();
    }

    private Predicate<Product> buildCondition(String key, String value) {
        return switch (key) {
            case "name" -> product ->
                                    product.getProductName().equalsIgnoreCase(value);
            case "price" -> {
                double price = Double.parseDouble(value);
                yield product -> Math.abs(product.getPrice() - price) < 0.0001;
            }
            case "quantity" -> {
                int quantity = Integer.parseInt(value);
                yield product -> product.getQuantity() == quantity;
            }
            case "id" -> {
                int id = Integer.parseInt(value);
                yield product -> product.getProductId() == id;
            }

            default -> product -> false;
        };
    }
}
