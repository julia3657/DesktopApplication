## Exchange Rate Platform

**Software Development Project**

---

### Overview

This project is a **cross-platform desktop application** for tracking and managing **foreign exchange rates**. The system provides real-time currency conversion, peer-to-peer transactions, and data visualization features. It was built with an emphasis on **clean architecture**, **maintainability**, and **user experience**.

---

### Features

* **Real-time exchange rates** from multiple currencies
* **Dynamic statistical analysis** and visualizations (charts, trends, etc.)
* **Peer-to-peer transaction support** with simulated accounts
* **Cross-platform desktop compatibility** (Windows, macOS, Linux)
* **Responsive UI/UX**, adapting to various screen sizes and resolutions

---

### Technologies Used

* **Java 17**
* **Maven** for project build and dependency management
* **JavaFX** for GUI
* **Gson** for JSON handling
* **JUnit** for unit testing
* **Git & GitHub** for version control

---

### How to Run the Project

#### Requirements

* Java 17+
* Maven 3.6+
* IntelliJ IDEA (recommended)

#### Clone the Project

```bash
git clone https://github.com/EECE-430L/exchange-desktop-julia3657.git
cd exchange-desktop-julia3657
```

#### Open in IntelliJ

1. Open IntelliJ IDEA
2. Choose **"Open"** and select the root folder: `exchange-desktop-julia3657`
3. Let Maven import the project automatically

#### Run the Application

1. Locate the main class inside `src/main/java/com/juliahaidarahmad/exchange/`
2. Right-click the main class (e.g., `MainApp.java`) → `Run`
3. Alternatively, from terminal:

```bash
./mvnw clean compile exec:java -Dexec.mainClass="com.juliahaidarahmad.exchange.MainApp"
```

---

### 📄 License

This project is for academic use only. No commercial distribution permitted without permission.

---
