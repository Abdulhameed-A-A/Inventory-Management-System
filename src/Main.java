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
//            case 4 -> deleteProduct();
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
    String inputName = validateInput("Enter product name: ", String.class);
    double inputPrice = validateInput("Enter the price of the product: ", Double.class);
    int inputQuantity = validateInput("Enter the Quantity of the Product: ", Integer.class);
    displayCategory();
    String inputCategory = validateInput("Enter the category for the product: ", String.class);

    if(inventoryManagement.addProduct(inputName, inputPrice, inputQuantity, inputCategory)){
        IO.println("Product added Successfully");
    }

}

void displayProduct(){
    inventoryManagement.displayProduct();
}

void filterProduct(){
        String filterInput = validateInput("Enter method to which you want to search with an '=' and the value", String.class);

        List<Product> filterResult = inventoryManagement.filterProducts(filterInput);

        filterResult.forEach(product -> IO.println(product.getProductId() + ". | Name: " + product.getProductName() + " | Price: " + product.getPrice() + " | Category: " + product.getCategory()));

}

void deleteProduct(){
    //Delete Product Ui Logic goes here
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