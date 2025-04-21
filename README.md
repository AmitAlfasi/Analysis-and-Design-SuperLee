# SuperLee System: Supermarket Logistics Management 🛒

**Academic Project:** This system was developed as part of the **Software Systems Analysis and Design** course curriculum. It serves as a practical application of analysis and design principles in building information systems.

---

## 🌟 Project Overview

Tired of tangled logistics in supermarket supply chains? **SuperLee** is here to help! This analysis and design project introduces an information system focused on optimizing **supply, storage, and transportation** for supermarkets.

SuperLee tackles the complexities of managing deliveries, coordinating transit between suppliers and branches, tracking orders, and ensuring storage requirements are met. The system is specifically designed to streamline the **Transportation module**, handling everything from order document creation to driver scheduling and store-specific logistics needs.

Whether you're managing a single store or a network of branches, SuperLee provides the tools to keep your shelves stocked and your operations running smoothly.

## ✨ Features

* 🚚 **Robust Transportation Management:** Coordinate deliveries, manage transit routes, and schedule drivers efficiently across multiple branches.
* 📄 **Streamlined Order Processing:** Create, track, and manage orders from suppliers, including details like destination stores and product weights.
* 📦 **Branch & Storage Awareness:** Define and manage store-specific requirements, shifts, and employee data related to logistics.
* 💾 **Persistent Data Storage:** Utilizes **SQLite** for reliable data management, including a provided simulation database (`identifier.sqlite`) for realistic testing.
* 🖥️ **Dual Interface:** Interact via a straightforward **Command-Line Interface (CLI)** for quick operations or a user-friendly **Graphical User Interface (GUI)** for visual management.

## 🛠️ Installation and Setup

Get SuperLee up and running in a few simple steps:

1.  **Clone the Repository:**
    ```bash
    git clone [https://github.com/IttaySegal/Analyze-and-Design-SuperLee.git](https://github.com/IttaySegal/Analyze-and-Design-SuperLee.git)
    cd Analyze-and-Design-SuperLee
    ```

2.  **Prerequisites:**
    * Ensure you have a compatible **Java Development Kit (JDK)** installed on your system.
    * No external library installation is typically required if using an IDE like IntelliJ IDEA, as dependencies should be managed via the project files.

3.  **IDE Setup (Recommended):**
    * Import the project into your favorite Java IDE (e.g., IntelliJ IDEA, Eclipse). The IDE should automatically handle project setup and dependencies.

4.  **Database:**
    * The system relies on SQLite. The `identifier.sqlite` file located in the project root directory is pre-populated to simulate real-time operational data. Ensure this file is accessible by the application.

## 🚀 Usage

You can interact with the SuperLee system in two ways:

* **Command-Line Interface (CLI):**
    * For terminal enthusiasts! Access functions to manage orders and logistics.
    * The main entry point for CLI operations can be explored in: `dev/src/CLI_Layer/OrderDocumentCli.java`.
    * Compile and run this class to start the CLI.

* **Graphical User Interface (GUI):**
    * Prefer a visual approach? Launch the GUI to manage orders, suppliers, and transportation details intuitively.
    * The main entry point for the GUI is: `dev/src/GUI_Layer/OrderDocumentGui.java`.
    * Compile and run this class to launch the GUI application.

## 📁 Code Structure & Key Files

The project follows a layered architecture to separate concerns:

* **Presentation Layer (CLI/GUI):** Handles user interaction.
    * `dev/src/CLI_Layer/OrderDocumentCli.java`: Implements the command-line interface.
    * `dev/src/GUI_Layer/OrderDocumentGui.java`: Implements the graphical user interface.
* **Controller Layer:** Acts as a bridge between the UI and the business logic.
    * `dev/src/ControllerLayer/ControllerGen.java`: Initializes and manages core controllers (Truck, Transit, Product, etc.).
* **Business Logic Layer:** Contains the core rules and processes.
    * `dev/src/BussinesLogic/BranchStore.java`: Manages branch-specific data like shifts and employee logistics roles.
* **Domain Layer:** Represents the core entities of the system.
    * `dev/src/DomainLayer/OrderDocument.java`: Defines the structure and behavior of order documents, including supplier/destination info and product handling.
* **Data Layer:** (Implicitly managed via Controllers/SQLite) Handles data persistence.

* **Pre-compiled Application:**
    * `release/adss2023_v02.jar`: A packaged JAR file containing the compiled project for easy execution (may require specific runtime environment setup).

---

Feel free to explore the code and understand how different components interact to manage supermarket logistics!
