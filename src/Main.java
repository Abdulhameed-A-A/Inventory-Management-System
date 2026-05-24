static Scanner scanner = new Scanner(System.in);
static InventoryManagementService inventoryManagementService = new InventoryManagementService();

void main() {
    loadProductsFromFile();
    printHeader();
    runMenu();
}

void printHeader(){
    IO.println("""
        ==========================================================
                    INVENTORY MANAGEMENT SYSTEM
        ==========================================================
        """);
}

void displayMainMenu() {
    IO.println("""
         1) Add Product
         2) Display Products
         3) Filter Products

         4) Delete Product
         5) Update Product
        
         6) Restock Product
         7) Purchase Product
         9) Low Stock

         9) Exit

        ==========================================================
        """);
}

void runMenu(){
    boolean running = true;
    while (running) {
        displayMainMenu();
        int option = validateInput("Enter your option (1 - 9): ", Integer.class);
        switch (option) {
            case 1 -> {
                addProduct();
                IO.println("Product added successfully\n");
            }
            case 2 -> displayProducts();
            case 3 -> filterProducts();
            case 4 -> deleteProduct();
            case 5 -> updateProduct();
            case 6 -> restockProduct();
            case 7 -> purchaseProduct();
            case 8 -> lowStocks();
            case 9 -> {
                IO.println("Exiting system... Goodbye!");
                running = false;
            }
            default -> IO.println("Invalid option. Please choose between 1 - 6.\n");
        }
    }
}

void addProduct() {

    String inputProducts = validateInput(
            """
            ============================================================
                              ADD NEW PRODUCT
            ============================================================

            FORMAT:
              name=...,price=...,quantity=...,category=...

            MULTIPLE PRODUCTS:
              Separate each product using :

            EXAMPLE:
              name=Laptop,price=2500,quantity=4,category=Electronics:
              name=Chair,price=120,quantity=10,category=Furniture

            AVAILABLE CATEGORIES:
              Electronics, Office, Tools, Cleaning,
              Furniture, Accessories, Food,
              Medical, Sporting, Books

            ============================================================
            Enter product details: \
            """,
            String.class
    );

    String result = inventoryManagementService.addProduct(inputProducts);

    IO.println("\n" + result + "\n");

    saveProductsToFile();
}

void displayProducts() {

    List<Product> products = inventoryManagementService.getProducts();

    IO.println("""
            ============================================================
                              PRODUCT INVENTORY
            ============================================================
            """);

    if (products.isEmpty()) {
        IO.println("No products available.\n");
        return;
    }

    IO.println("Total Products: " + products.size());

    IO.println("""
            
            ------------------------------------------------------------
            ID | NAME               | PRICE     | QTY | CATEGORY
            ------------------------------------------------------------
            """);

    for (Product product : products) {
        printProduct(product);
    }

    IO.println("------------------------------------------------------------\n");
}

void filterProducts() {

    String filterInput = validateInput(
            """
            ============================================================
                              FILTER PRODUCTS
            ============================================================

            AVAILABLE FILTERS:
              - id
              - name
              - price
              - quantity
              - category

            FORMAT:
              key=value

            EXAMPLES:
              name=Laptop
              price=2000
              quantity=5
              category=Electronics
              id=1

            ============================================================
            Enter filter: \
            """,
            String.class
    );

    List<Product> filteredProducts = inventoryManagementService.filterProducts(filterInput);

    IO.println("""
            
            ============================================================
                              FILTER RESULTS
            ============================================================
            """);

    if (filteredProducts.isEmpty()) {
        IO.println("No matching products found.\n");
        return;
    }

    IO.println("Matched Products: " + filteredProducts.size());

    IO.println("""
            
            ------------------------------------------------------------
            ID | NAME               | PRICE     | QTY | CATEGORY
            ------------------------------------------------------------
            """);

    filteredProducts.forEach(this::printProduct);

    IO.println("------------------------------------------------------------\n");
}

void deleteProduct() {

    IO.println("""
            ============================================================
                              DELETE PRODUCT
            ============================================================
            """);

    int deleteInput = validateInput(
            """
            DELETE USING:
              - id

            FORMAT:
              key=value

            EXAMPLES:
              id=3

            ============================================================
            Enter delete command: \
            """,
            Integer.class
    );

    String preview = inventoryManagementService.previewDelete(deleteInput);

    IO.println("\n" + preview);

    if (preview.startsWith("Error")) {
        return;
    }

    String confirmation = validateInput("\nProceed with delete? (yes/no): ", String.class);

    String result = inventoryManagementService.confirmDelete(deleteInput, confirmation);

    IO.println("\n" + result + "\n");
    saveProductsToFile();
}

void updateProduct() {

    IO.println("""
            ============================================================
                              UPDATE PRODUCT
            ============================================================
            """);

    int productId = validateInput("Enter Product ID: ", Integer.class);

    String updateInput = validateInput(
            """

            AVAILABLE FIELDS:
              - name
              - price
              - quantity
              - category

            FORMAT:
              field=value

            MULTIPLE UPDATES:
              field=value,field=value

            ============================================================
            Enter update details: \
            """,
            String.class
    );

    String preview = inventoryManagementService.previewUpdate(productId, updateInput);

    IO.println("\n" + preview);

    if (preview.startsWith("Error")) {
        return;
    }

    String confirmation = validateInput("\nProceed with update? (yes/no): ", String.class);

    String result = inventoryManagementService.confirmUpdate(productId, updateInput, confirmation);
    IO.println("\n" + result + "\n");
    saveProductsToFile();
}

void restockProduct(){
    IO.println("""
            ============================================================
                              RESTOCK PRODUCT
            ============================================================
            """);

    int inputId = validateInput("""
            RESTOCK USING:
            -id
            
            Enter product id:\
            """, Integer.class);
    int inputRestockQuantity = validateInput("Enter Restock Quantity: ", Integer.class);
    String restockResult = inventoryManagementService.restockProduct(inputId, inputRestockQuantity);

    IO.println(restockResult);
}

void purchaseProduct(){
    IO.println("""
            ============================================================
                              Purchase PRODUCT
            ============================================================
            """);

    int inputId = validateInput("Enter Product Id: ", Integer.class);
    int inputPurchaseQuantity = validateInput("Enter Purchase Quantity: ", Integer.class);

    String purchaseResult = inventoryManagementService.purchaseProduct(inputId, inputPurchaseQuantity);
    IO.println(purchaseResult);
}

void lowStocks(){
    IO.println("""
            ============================================================
                              LOW STOCKS
            ============================================================
            """);

    List<Product> lowStocksResult = inventoryManagementService.lowStock();

    if(lowStocksResult.isEmpty()){
        IO.println("No low Stocks");
    }

    IO.println("Low Stocks: " + lowStocksResult.size());

    IO.println("""
            
            ------------------------------------------------------------
            ID | NAME               | PRICE     | QTY | CATEGORY
            ------------------------------------------------------------
            """);

    lowStocksResult.forEach(this::printProduct);

    IO.println("------------------------------------------------------------\n");
}

void loadProductsFromFile(){
    String loadResult = inventoryManagementService.loadProductsFromFile();
    IO.println(loadResult);
}

void saveProductsToFile(){
    String saveResult = inventoryManagementService.saveProductsToFile();
    IO.println(saveResult);
}

void printProduct(Product product) {
    System.out.printf("%-3d| %-20s| %-12.2f| %-5d| %-15s%n",
            product.getProductId(),
            product.getProductName(),
            product.getPrice(),
            product.getQuantity(),
            product.getCategory()
    );
}

static <T> T validateInput(String message, Class<T> type) {
    String input;

    while (true) {
        IO.print(message);
        input = scanner.nextLine();

        try{
            if(type == Integer.class){
                return type.cast(Integer.parseInt(input));
            } else if (type == Double.class) {
                return type.cast(Double.parseDouble(input));
            } else if (type == String.class) {
                if (!input.isBlank()) {
                    return type.cast(input);
                }

                IO.println("This field cannot be Empty");
                continue;
            }

            throw new IllegalArgumentException("Unsupported Type");

        } catch (NumberFormatException e){
            IO.println("Enter a valid Number");
        }
    }
}