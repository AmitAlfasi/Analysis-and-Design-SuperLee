# SuperLee System

SuperLee is an analysis and design project for a supermarket information system focusing on supply and storage management, specifically the **Transportation** module.

## Table of Contents

- [Project Overview](#project-overview)
- [Features](#features)
- [Installation and Setup](#installation-and-setup)
- [Usage](#usage)
- [File Descriptions](#file-descriptions)
  
## Project Overview

The SuperLee System aims to support the logistics of a supermarket's supply chain by managing transit, orders, and storage at various branches. This project emphasizes creating and managing order documents, maintaining driver schedules, and setting up store-specific requirements.

## Features

- **Transportation Management**: Manage deliveries, transit coordination, and schedules for various branches.
- **Order Processing**: Track supplier orders, store destinations, and product weights.
- **Database Integration**: Uses SQLite to persist data.
- **CLI and GUI**: Interact with the system through a command-line interface and a basic GUI.

## Installation and Setup

1. **Clone the Repository**:
    ```bash
    git clone https://github.com/IttaySegal/Analyze-and-Design-SuperLee.git
    cd Analyze-and-Design-SuperLee
    ```

2. **Dependencies**:
    - Ensure Java is installed.
    - Install necessary libraries or set up the project within an IDE (like IntelliJ IDEA) with dependencies managed via the included project files.

3. **Database**:
    - The system uses `SQLite`. An identifier database (`identifier.sqlite`) is provided to simulate real-time data.

## Usage

- **CLI**:
    - The CLI provides functions to manage orders and logistics through commands found in `dev/src/CLI_Layer/OrderDocumentCli.java`.
- **GUI**:
    - Launch the GUI to visually manage orders and interact with the transportation system, accessible in `dev/src/GUI_Layer/OrderDocumentGui.java`.

## File Descriptions

- **`dev/src/BussinesLogic/BranchStore.java`**: Defines `BranchStore` for branch management, including shift and employee data.
- **`dev/src/ControllerLayer/ControllerGen.java`**: Initializes core controllers (Truck, Transit, Product, etc.) for handling business logic.
- **`dev/src/CLI_Layer/OrderDocumentCli.java`**: CLI for creating and managing order documents.
- **`dev/src/GUI_Layer/OrderDocumentGui.java`**: GUI components for order management and supplier handling.
- **`dev/src/DomainLayer/OrderDocument.java`**: Represents orders with supplier and destination info, and methods for handling product quantities.
- **`release/adss2023_v02.jar`**: Packaged JAR for the compiled project.
