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

    List<Product> getProducts() {
        return products;
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

        return switch (key.toLowerCase()) {
            case "name" ->
                    product -> product.getProductName().equalsIgnoreCase(value);
            case "category" ->
                    product -> product.getCategory().equalsIgnoreCase(value);
            case "price" -> {
                double price;
                try {
                    price = Double.parseDouble(value);
                } catch (NumberFormatException e) {
                    yield _ -> false;
                }
                yield product -> Math.abs(product.getPrice() - price) < 0.0001;
            }
            case "quantity" -> {
                int quantity;
                try {
                    quantity = Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    yield _ -> false;
                }
                yield product -> product.getQuantity() == quantity;
            }
            case "id" -> {
                int id;
                try {
                    id = Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    yield _ -> false;
                }
                yield product -> product.getProductId() == id;
            }

            default -> _ -> false;
        };
    }

    String addProduct(String inputProduct) {
        String[] productEntries = inputProduct.split(":");

        for (String entry : productEntries) {
            Map<String, String> productData = parseInput(entry, ",");

            String productName = productData.get("name");
            String category = productData.get("category");
            String priceInput = productData.get("price");
            String quantityInput = productData.get("quantity");

            if (productName == null || productName.isBlank()) {
                return "Error: Product name is required";
            }

            if (category == null || category.isBlank()) {
                return "Error: Category is required";
            }

            if (!categories.contains(category)) {
                return "Error: Invalid category -> " + category;
            }

            double price;
            int quantity;

            try {
                if (priceInput == null) {
                    return "Error: Price is required";
                }
                price = Double.parseDouble(priceInput);
                if (price < 0) {
                    return "Error: Price cannot be negative";
                }
            } catch (NumberFormatException e) {
                return "Error: Invalid price value -> " + priceInput;
            }

            try {
                if (quantityInput == null) {
                    return "Error: Quantity is required";
                }
                quantity = Integer.parseInt(quantityInput);
                if (quantity < 0) {
                    return "Error: Quantity cannot be negative";
                }
            } catch (NumberFormatException e) {
                return "Error: Invalid quantity value -> " + quantityInput;
            }

            Product product = new Product(productId, productName, price, quantity, category);

            products.add(product);
            productId++;
        }

        return "Product added successfully";
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

    String deleteProduct(String input) {
        Map<String, String> data = parseInput(input, ",");

        if (data.isEmpty()) {
            return "Error: Invalid delete format";
        }

        String key = data.keySet().iterator().next();
        String value = data.get(key);

        Predicate<Product> condition = buildCondition(key, value);

        boolean removed = products.removeIf(condition);
        if (!removed) {
            return "Error: Product not found";
        }

        return "Product deleted successfully";
    }

    String updateProduct(int productId, String updates) {
        Optional<Product> foundProduct = findProductById(productId);

        if (foundProduct.isEmpty()) {
            return "Error: Product not found";
        }

        Product product = foundProduct.get();

        Map<String, String> updateData = parseInput(updates, ",");

        if (updateData.isEmpty()) {
            return "Error: Invalid update format";
        }

        for (Map.Entry<String, String> entry : updateData.entrySet()) {

            String key = entry.getKey();
            String value = entry.getValue();

            try {
                switch (key.toLowerCase()) {
                    case "name" -> {
                        if (value.isBlank()) {
                            return "Error: Product name cannot be empty";
                        }

                        product.setProductName(value);
                    }
                    case "price" -> {
                        double price = Double.parseDouble(value);
                        if (price < 0) {
                            return "Error: Price cannot be negative";
                        }

                        product.setPrice(price);
                    }
                    case "quantity" -> {
                        int quantity = Integer.parseInt(value);
                        if (quantity < 0) {
                            return "Error: Quantity cannot be negative";
                        }

                        product.setQuantity(quantity);
                    }
                    case "category" -> {
                        if (!categories.contains(value)) {
                            return "Error: Invalid category -> " + value;
                        }

                        product.setCategory(value);
                    }

                    default -> {
                        return "Error: Unknown field -> " + key;
                    }
                }
            } catch (NumberFormatException e) {
                return "Error: Invalid value for -> " + key;
            }
        }

        return "Product updated successfully";
    }
}