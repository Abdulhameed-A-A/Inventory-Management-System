import java.io.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class InventoryManagementService {
    private final ArrayList<Product> products;
    private int productId = 1;
    private final int LOW_STOCK_THRESHOLD = 5;
    private boolean loaded = false;
    private final String PRODUCTS_FILE_PATH = "products.txt";
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

    InventoryManagementService(){
        this.products = new ArrayList<>();
    }

    List<Product> getProducts(){
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
    private record UpdateContext(Product product, Map<String, String> updates) {}

    private Optional<UpdateContext> buildUpdateContext(int productId, String updates) {

        Optional<Product> foundProduct = findProductById(productId);
        if (foundProduct.isEmpty()) {
            return Optional.empty();
        }

        Map<String, String> updateData = parseInput(updates, ",");
        if (updateData.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new UpdateContext(foundProduct.get(), updateData));
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

    String deleteProduct(int deleteInput) {

        Optional<Product> foundProduct = findProductById(deleteInput);

        if (foundProduct.isEmpty()) {
            return "Error: Product not found";
        }

        Product product = foundProduct.get();

        products.remove(product);

        return "Product deleted successfully";
    }

    String previewDelete(int deleteInput) {
        Optional<Product> foundProduct = findProductById(deleteInput);

        if(foundProduct.isEmpty()){
            return "Error: Product now found";
        }

        Product product = foundProduct.get();

        return """
                ============================================================
                                      DELETE PREVIEW
                ============================================================
                You are about to delete the following product:
                
                ID: %d | Name: %s | Price: %.2f | Quantity: %d | Category: %s
                
                WARNING: This action cannot be undone!
                """.formatted(
                        product.getProductId(),
                        product.getProductName(),
                        product.getPrice(),
                        product.getQuantity(),
                        product.getCategory());
    }

    String confirmDelete(int deleteInput, String confirmation){
        if (!confirmation.equalsIgnoreCase("yes") && !confirmation.equalsIgnoreCase("y")){
            return "Delete Cancelled";
        }

        return deleteProduct(deleteInput);
    }

    String updateProduct(int productId, String updates) {
        Optional<UpdateContext> contextOpt = buildUpdateContext(productId, updates);

        if(contextOpt.isEmpty()){
            return "Error: Product not found or Invalid update format";
        }

        UpdateContext context = contextOpt.get();
        Product product = context.product;
        Map<String, String> updateData = context.updates;

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

    String previewUpdate(int productId, String updates) {
        Optional<UpdateContext> contextOpt = buildUpdateContext(productId, updates);

        if(contextOpt.isEmpty()){
            return "Error: Product not found or invalid update format";
        }

        UpdateContext context = contextOpt.get();
        Product product = context.product();
        Map<String, String> updateData = context.updates();

        StringBuilder preview = new StringBuilder();

        preview.append("""
        ============================================================
                        UPDATE PREVIEW
        ============================================================
        """);

        for (Map.Entry<String, String> entry : updateData.entrySet()) {

            String key = entry.getKey();
            String value = entry.getValue();

            switch (key.toLowerCase()) {
                case "name" -> preview.append("""
                    Name:
                      Old -> %s
                      New -> %s
                    """.formatted(product.getProductName(), value));

                case "price" -> preview.append("""
                    Price:
                      Old -> %.2f
                      New -> %s
                    """.formatted(product.getPrice(), value));

                case "quantity" -> preview.append("""
                    Quantity:
                      Old -> %d
                      New -> %s
                    """.formatted(product.getQuantity(), value));

                case "category" -> preview.append("""
                    Category:
                      Old -> %s
                      New -> %s
                    """.formatted(product.getCategory(), value));

                default -> {
                    return "Error: Unknown Field -> " + key;
                }
            }
        }

        return preview.toString();
    }

    String confirmUpdate(int productId, String updates, String confirmation) {
        if (!confirmation.equalsIgnoreCase("yes") && !confirmation.equalsIgnoreCase("y")) {

            return "Update Cancelled";
        }

        return updateProduct(productId, updates);
    }

    String restockProduct(int productId, int restockValue){
        Optional<Product> foundProduct = findProductById(productId);

        if(foundProduct.isEmpty()){
            return "Error: Product not found";
        }

        Product product = foundProduct.get();

        if(restockValue<= 0 ){
            return "Error: Restock quantity must be more greater than zero";
        }

        int newQuantity = product.getQuantity() + restockValue;
        product.setQuantity(newQuantity);
        return "Successfully restocked " + product.getProductName() + " by an amount of " + restockValue;
    }

    String purchaseProduct(int productId, int purchaseQuantity) {
        Optional<Product> foundProduct = findProductById(productId);

        if(foundProduct.isEmpty()){
            return "Error: Product not found";
        }

        Product product = foundProduct.get();
        int currQuantity = product.getQuantity();

        if(purchaseQuantity <= 0){
            return "Error: Purchase quantity must be greater than zero";
        } else if (purchaseQuantity > currQuantity){
            return "Error: Insufficient stock available";
        }

        int newQuantity = currQuantity - purchaseQuantity;
        product.setQuantity(newQuantity);
        return "Successfully purchased " + purchaseQuantity + " of " + product.getProductName();
    }

    List<Product> lowStock(){
        return products.stream()
                .filter(p -> p.getQuantity() < LOW_STOCK_THRESHOLD)
                .collect(Collectors.toList());
    }

    String saveProductsToFile() {
        try {
            FileWriter writer = new FileWriter(PRODUCTS_FILE_PATH);

            for(Product product: products) {
                String line = product.getProductId() +
                        "," + product.getProductName() +
                        "," + product.getPrice() +
                        "," + product.getQuantity() +
                        "," + product.getCategory();

                writer.write(line + "\n");
            }
            writer.close();
            return "Products successfully saved to File";
        } catch (IOException e){
            return "Error: Something Went Wrong";
        }
    }

    String loadProductsFromFile(){
        if(loaded){
            return "Info: Already Loaded from file";
        }

        StringBuilder errors = new StringBuilder();
        int lineNumber = 0;

        Set<Integer> seenIds = new HashSet<>();

        for (Product p : products){
            seenIds.add(p.getProductId());
        }

        try (BufferedReader reader = new BufferedReader( new FileReader(PRODUCTS_FILE_PATH))){
            String line;

            while((line = reader.readLine()) != null){
                lineNumber++;

                String[] parts = line.split(",");

                if(parts.length != 5){
                    errors.append("Line ")
                            .append(lineNumber)
                            .append(": Bad line Structure\n");

                    continue;
                }

                try {
                    int id = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();
                    double price = Double.parseDouble(parts[2].trim());
                    int quantity = Integer.parseInt(parts[3].trim());
                    String category = parts[4].trim();

                    if(seenIds.contains(id)){
                        errors.append("Line ")
                                .append(lineNumber)
                                .append(": Duplicate product Ids ")
                                .append(id)
                                .append("\n");

                        continue;
                    }

                    seenIds.add(id);

                    Product product = new Product(id, name, price, quantity, category);
                    products.add(product);

                    if(id >= productId) {
                        productId = id + 1;
                    }
                } catch (NumberFormatException e) {
                    errors.append("Line ")
                            .append(lineNumber)
                            .append(": Invalid number format \n");
                }
            }
            reader.close();

            loaded = true;

            if(errors.length() > 0){
                return "Products loaded with errors:\n" + errors;
            }
            return "Products Successfully loaded from File";
        } catch (FileNotFoundException e){
            return "Error: File not found";
        } catch (IOException e){
            return "Error: Something went wrong";
        }
    }
}