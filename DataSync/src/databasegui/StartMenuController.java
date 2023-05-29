
package databasegui;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;




public class StartMenuController implements Initializable {

  @FXML
  private Button enterDatabase;
  
  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
       
       
    }    
    
    public void enterDatabaseAction(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("choice-window.fxml"));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("DataSync - Main Menu");
        window.setScene(scene);
        window.show();
                                        
        try {    
Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/projectdb;user=root;password="
        + "password");
       Statement statement = conn.createStatement();

// create customers table
     String createCustomersTableSQL = "CREATE TABLE Customers ("
        + "CustomerID INTEGER NOT NULL PRIMARY KEY,"
        + "CustomerName VARCHAR(25), "
        + "CustomerZipcode INTEGER, "
        + "CreditLimit DOUBLE)";

         statement.execute(createCustomersTableSQL);

// insert customers table
        String insertCustomerSQL = "INSERT INTO Customers (CustomerID, CustomerName, CustomerZipcode, "
        + "CreditLimit) "
        + "VALUES (1, 'David', 28117, 5000),"
        + "(2, 'Rudy', 28115, 3000)";

         statement.executeUpdate(insertCustomerSQL);

// create inventory table
          String createInventoryTableSQL = "CREATE TABLE Inventory ("
              + "InventoryID INTEGER PRIMARY KEY,"
              + "InventoryDescription VARCHAR(25),"
              + "InventoryPrice DOUBLE,"
              + "InventoryQuantityOnHand INTEGER,"
              + "InventoryAmountInStock INTEGER)";

         statement.execute(createInventoryTableSQL);

// insert data in inventory 
            String insertInventorySQL = "INSERT INTO Inventory (InventoryID, InventoryDescription, InventoryPrice, "
               + "InventoryQuantityOnHand, InventoryAmountInStock) "
               + "VALUES (1, 'Ferris Lawn Mower', 10000, 5, 7),"
                + "(2, 'Husqvarna Chainsaw', 600, 10, 15)";

statement.executeUpdate(insertInventorySQL);

// create sales table
          String createSalesTableSQL = "CREATE TABLE Sales (" 
                 + "ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " 
                 + "CustomerID INT NOT NULL REFERENCES Customers(CustomerID), " 
                 + "InventoryID INT NOT NULL REFERENCES Inventory(InventoryID), " 
                 + "QuantitySold INT, " 
                 + "OrderDate DATE NOT NULL, " 
                 + "CONSTRAINT UniqueSale UNIQUE(CustomerID, InventoryID, OrderDate))";

statement.execute(createSalesTableSQL);

// insert sales data
        String insertSalesSQL = "INSERT INTO Sales (CustomerID, InventoryID, QuantitySold, OrderDate) "
                 + "VALUES (1, 1, 50, '2023-04-27'),"
                 + "(2, 2, 90, '2023-04-27')";

         statement.executeUpdate(insertSalesSQL);

         System.out.println("Tables Created and Data Inserted.");
                     
        } catch (SQLException e) {
            System.out.println("SQL Statement Error: " + e.getMessage());                    
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        } 
    }
    
    
    
}
