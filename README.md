Good — we’ll turn this into something that looks like a real backend portfolio project on GitHub.

You can copy this directly into your `README.md`.

---

# Inventory Management System (Java)

## Project Overview

The Inventory Management System is a backend-style Java application that allows users to manage products in a store environment. It supports core inventory operations such as adding, updating, deleting, filtering, and tracking products, including low stock detection.

The system also includes file-based persistence to ensure that product data is saved and restored between sessions, simulating real-world backend storage behavior.

---

## Features

* Add new products to inventory
* Update product details (name, price, quantity, category)
* Delete products from inventory
* Search and filter products
* Low stock detection system
* Purchase simulation (reduces stock safely)
* Restock functionality (increases stock safely)
* File persistence (save and load product data)
* Duplicate ID prevention during data loading
* Input validation and error handling

---

## Key Concepts Used

* Object-Oriented Programming (OOP)
* Java Collections (`List`, `Set`, `Map`)
* Streams API and Lambda Expressions
* Optional for safe null handling
* File I/O (BufferedReader, FileWriter)
* Exception handling and validation
* Generics (for reusable input handling)
* Switch expressions (`yield`)
* Code modularization and reusable methods

---

## Project Structure

```text
Product → Data model (represents a product)
InventoryManagementSystem → Business logic layer (core operations)
Main → User interaction layer (console-based UI)
```

---

## Data Persistence

The system uses file storage to persist product data between sessions.

* Products are saved in a structured format:

  ```
  id,name,price,quantity,category
  ```
* Data is loaded at startup with validation and duplicate protection.

---

## How to Run

1. Clone the repository:

```bash
git clone https://github.com/Abdulhameed-A-A/Inventory-Management-System.git
```

2. Open the project in any Java IDE (IntelliJ, Eclipse, VS Code)

3. Run the `Main` class

---

## What I Learned

While building this project, I gained practical experience with core Java and backend development concepts.

I worked extensively with Java Collections such as `List`, `Set`, and `Map`, and applied them in real scenarios like managing products, preventing duplicates, and handling structured data.

I improved my understanding of Streams, lambda expressions, and functional-style operations such as filtering and predicates, which helped me write cleaner and more readable code.

I also strengthened my error handling and validation skills by building step-by-step checks that ensure data consistency and prevent system failures.

One of the most important lessons was understanding the value of reusable methods. While often emphasized in theory, this project helped me see how they reduce duplication and improve maintainability in real systems.

Additionally, I explored newer Java features such as switch expressions (`yield`), `Optional`, and `record` types, which helped reduce boilerplate and improve code clarity.

I also implemented generics to create reusable input-handling logic, allowing flexible and type-safe user input processing.

Overall, this project helped me transition from writing basic Java programs to thinking in terms of system design, backend logic, and maintainable architecture.

---

## Project Goal

This project was built as part of my journey toward becoming a backend developer. It focuses on building strong fundamentals in Java, system design thinking, and preparing for database-backed applications using SQL, JDBC, and Spring Boot.

---

## Future Improvements

* Replace file storage with SQL database (JDBC)
* Add REST API using Spring Boot
* Add user authentication system
* Add order and customer management modules

---

## Author

**Adewunmi Abdulhameed Oladokun**
Java Backend Learning Journey
