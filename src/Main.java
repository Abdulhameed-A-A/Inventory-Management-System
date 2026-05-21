static Scanner scanner = new Scanner(System.in);
static InventoryManagementSystem inventoryManagement = new InventoryManagementSystem();

void main() {

    IO.println("*--------------------------*");
    IO.println("Inventory Management System");
    IO.println("*--------------------------*");
    boolean running = true;

    while (running) {
        disPlayMainMenu();
        int option = validateInput("Enter your Option (1 - 6) : ", Integer.class);

        switch (option){
            case 1 -> addProduct();
            case 2 -> displayProducts();
            case 3 -> filterProducts();
            case 4 -> deleteProduct();
            case 5 -> updateProduct();
            case 6 -> running = false;
        }
    }
}

void disPlayMainMenu(){
    IO.println("1. Add Product");
    IO.println("2. Display Products");
    IO.println("3. Filter Products");
    IO.println("4. Delete Product");
    IO.println("5. Update Product");
    IO.println("6. Exit");
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

    String result = inventoryManagement.addProduct(inputProducts);

    IO.println("\n" + result + "\n");
}

void displayProducts() {

    List<Product> products = inventoryManagement.getProducts();

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

    List<Product> filteredProducts = inventoryManagement.filterProducts(filterInput);

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

    String deleteInput = validateInput(
            """
            ============================================================
                              DELETE PRODUCT
            ============================================================

            DELETE USING:
              - id
              - name

            FORMAT:
              key=value

            EXAMPLES:
              id=3
              name=Laptop

            ============================================================
            Enter delete command: \
            """,
            String.class
    );

    if (deleteInput == null || deleteInput.isBlank()) {
        IO.println("Error: Delete input cannot be empty.\n");
        return;
    }

    String result = inventoryManagement.deleteProduct(deleteInput);

    IO.println("\n" + result + "\n");
}

void updateProduct() {

    IO.println("""
            ============================================================
                              UPDATE PRODUCT
            ============================================================
            """);

    int productId = validateInput(
            "Enter Product ID: ",
            Integer.class
    );

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

            EXAMPLES:
              name=Laptop
              price=2500
              quantity=5
              category=Electronics

              name=Laptop,price=2500
              category=Electronics,quantity=10

            ============================================================
            Enter update details: \
            """,
            String.class
    );

    if (updateInput == null || updateInput.isBlank()) {
        IO.println("Error: Update details cannot be empty.\n");
        return;
    }

    String result = inventoryManagement.updateProduct(productId, updateInput);

    IO.println("\n" + result + "\n");
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