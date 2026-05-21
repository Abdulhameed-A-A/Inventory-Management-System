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
            case 2 -> displayProduct();
            case 3 -> filterProduct();
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

void addProduct(){
    String inputProducts = validateInput(
            """
            Format -> name=..., price=..., quantity=..., category=...
            Multiple products should be separated with :
           \s
            Example:
            name=Laptop,price=2500,quantity=4,category=Electronics:
            name=Chair,price=120,quantity=10,category=Furniture
           \s
            Enter product details:\s
           \s""",
            String.class
    );

    String result = inventoryManagement.addProduct(inputProducts);

    IO.println(result);
}

void displayProduct(){
    List<Product> products = inventoryManagement.getProducts();

    if (products.isEmpty()){
        IO.println("No products Available");
        return;
    }

    IO.println("=== PRODUCT LIST ===");

    IO.println("\nFound " + products.size() + " product(s):\n");
    for (Product product : products) {
        IO.println(
                "ID: " + product.getProductId()
                        + " | Name: " + product.getProductName()
                        + " | Price: " + product.getPrice()
                        + " | Quantity: " + product.getQuantity()
                        + " | Category: " + product.getCategory()
        );
    }

    IO.println("====================");
}

void filterProduct() {
    String filterInput = validateInput(
            """
            Example:
            name=Laptop
            price=2000
            quantity=5
            category=Electronics
            \s
            Enter filter (format: key=value): \s
            """,
            String.class
    );

    List<Product> filterResult = inventoryManagement.filterProducts(filterInput);

    if (filterResult.isEmpty()) {
        IO.println("No matching products found.");
        return;
    }

    IO.println("=== FILTER RESULTS ===");

    filterResult.forEach(product ->
            IO.println(
                    "ID: " + product.getProductId()
                            + " | Name: " + product.getProductName()
                            + " | Price: " + product.getPrice()
                            + " | Quantity: " + product.getQuantity()
                            + " | Category: " + product.getCategory()
            )
    );

    IO.println("======================");
}

void deleteProduct(){
    String deleteInput = validateInput("Enter name/id plus '=' plus value", String.class);

    if(inventoryManagement.deleteProduct(deleteInput)){
        IO.println("Product Successfully Deleted");
    }
}

void updateProduct(){
    int inputId = validateInput("Enter the id of the product you wish to Update: ", Integer.class);
    IO.println("---To Update. Follow The Instruction Below---");
    IO.println("Name Update: write \"name={name}\"");
    IO.println("Price Update: write \"price={price}\"");
    IO.println("Quantity Update: write \"quantity={quantity}\"");
    IO.println("Note to update all or some. Separate with a comma and replace {name}, {price}, {quantity} with actual values");
    String inputUpdate = validateInput("Enter update:", String.class);
    if(inventoryManagement.updateProduct(inputId, inputUpdate)){
        IO.println("Updated Successfully");
    }
}

//Reusable's
void displayCategory() {
    inventoryManagement.displayCategory();
}

//Utility Methods
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
                if (!input.trim().isEmpty()) {
                    return type.cast(input);
                }

                IO.println("This field cannot be Empty");
            }

            throw new IllegalArgumentException("Unsupported Type");

        } catch (NumberFormatException e){
            IO.println("Enter a valid Number");
        }
    }
}