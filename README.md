# Java Project: Shop Management System 🏪

The Shop Management System is a Java-based application designed to manage the operations of a retail shop. It incorporates features such as inventory management, employee authentication and graphical interfaces.

## Project Overview

The objective of this project is to develop a system capable of managing various aspects of a shop, including product inventory, sales, and employee access. 

## Getting Started
### Prerequisites
- **Java Development Kit (JDK):** Ensure that you have JDK 17 or higher installed.
- **SQL Database:** Set up a local or remote SQL database for employee session management.
- **Maven:** For dependency management
- **Git:** Clone the project from the repository.

```
git clone https://github.com/angelopozo/gestor-tienda-java.git
```

## Features
### Core Functionality
1) **Unlimited Inventory, Sales, and Products**: Removed any limits on the number of items in inventory, sales transactions, and products available for sale. The system now handles an unrestricted number of entries.
   
2) **Product Removal**: Added functionality to remove specific products from the inventory, updating stock levels accordingly.

3) **Load Inventory from File**: Developed methods to import the shop's inventory from multiple sources including files and databases.
 
4) **Login System**: Developed a secure login system for employees to authenticate before accessing the system.

### Graphical User Interface (GUI)
   - LoginView: A secure login interface for employee authentication.
   - ShopView: A dashboard displaying options to manage products, inventory, and sales.
   - CashView: A window showing the total cash available in the store.
   - ProductView: An intuitive interface for adding, updating, and removing products.
   - InvenrtoryView: A detailed view of the current inventory status.
 
### Data Management
#### Database Integration
   - **SQL Database Support:**
      - JDBC implementation for SQL databases

### Architecture
- **DAO Pattern:** Data Access Object pattern implementation for database operations
- **MVC Architecture:** Clear separation between model, view, and controller components
