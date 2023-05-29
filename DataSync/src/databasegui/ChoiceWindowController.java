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
import java.sql.ResultSet;
import java.sql.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;


public class ChoiceWindowController implements Initializable {

    @FXML
    TextField customerIDTextField;
    
    @FXML
    TextField customerNameTextField;
    
    @FXML
     TextField zipcodeTextField;
    
     @FXML
    TextField  creditLimitTextField;
    
  @FXML
    TextField  itemIDTextField;
    
     @FXML
   TextField  descriptionTextField;
    
    @FXML
     TextField priceTextField;
    
     @FXML       
    TextField  quantityTextField;
    
     @FXML        
    TextField  amountInStockTextField;
     
     @FXML
     TextField nameFilterTextField;
            
     
     
     @FXML
     Label customerAddedLabel;
     
     @FXML
     Label itemAddedLabel;
     
     @FXML 
     Button customerButton;
     
     @FXML
     Button itemButton;
     
     
     
      @FXML
    private TableView<Customer> customerTable;
    
    @FXML
    private TableColumn<Customer, Integer> customerIdColumn;
    
    @FXML
    private TableColumn<Customer, String> customerNameColumn;
    
    @FXML
    private TableColumn<Customer, String> customerZipcodeColumn;
    
    @FXML
    private TableColumn<Customer, Double> creditLimitColumn;
    
    @FXML
    private Button refreshButton;
    
        private ObservableList<Customer> customers = FXCollections.observableArrayList();

        @FXML 
                private TableView<Inventory> inventoryTable;
        
        @FXML
        private TableColumn<Inventory, Integer> inventoryIdColumn;
        
        @FXML 
           private    TableColumn<Inventory, String> inventoryDescColumn;
        
        @FXML
        private TableColumn <Inventory, Double> priceColumn;
        
        @FXML
        private TableColumn <Inventory, Integer> quantityColumn;
        
        @FXML
        private TableColumn <Inventory, Integer> amountColumn;
        
        @FXML
        private TextField IDSearchTextField;
        
        private ObservableList<Inventory> inventory = FXCollections.observableArrayList();
        
        
        
        
        @FXML 
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerZipcodeColumn.setCellValueFactory(new PropertyValueFactory<>("zipcode"));
        creditLimitColumn.setCellValueFactory(new PropertyValueFactory<>("creditLimit"));
        
        inventoryIdColumn.setCellValueFactory(new PropertyValueFactory<>("invID"));
        inventoryDescColumn.setCellValueFactory(new PropertyValueFactory<>("desc"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        
        
        refreshCustomerData();
        refreshInvData();
        
       
FilteredList<Customer> filteredList = new FilteredList<>(customers, p -> true);


nameFilterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
    filteredList.setPredicate(customer -> {
        
        if (newValue == null || newValue.isEmpty()) {
            return true;
        }

      
        String lowerCaseFilter = newValue.toLowerCase();
        return customer.getName().toLowerCase().contains(lowerCaseFilter);
    });
});


customerTable.setItems(filteredList);


IDSearchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
    filteredList.setPredicate(customer -> {
      
        if (newValue == null || newValue.isEmpty()) {
            return true;
        }


        String lowerCaseFilter = newValue.toLowerCase();
        return customer.getName().toLowerCase().contains(lowerCaseFilter) || String.valueOf(customer.getId()).contains(lowerCaseFilter);
    });
});


customerTable.setItems(filteredList);
    }    
    
     @FXML
    private void handleRefreshButton(ActionEvent event) {
        
        refreshCustomerData();
        refreshInvData();
        
        System.out.println("Customers Data Table Refreshed.");
        System.out.println("Inventory Data Table Refreshed.");
    }
    
     
    
      private void refreshCustomerData() {
       
        customers.clear();
        
       
        try (
                Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/projectdb;user=root;password="
        + "password")) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Customers");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getInt("CustomerID"));
                customer.setName(rs.getString("CustomerName"));
                customer.setZipcode(rs.getString("CustomerZipcode"));
                customer.setCreditLimit(rs.getDouble("CreditLimit"));
                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
      
        customerTable.setItems(customers);
    }
      
        private void refreshInvData() {
        
        inventory.clear();
        
        
        try (
                Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/projectdb;user=root;password="
        + "password")) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Inventory");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Inventory inventorys = new Inventory();
                inventorys.setInvID(rs.getInt("InventoryID"));
                inventorys.setDesc(rs.getString("InventoryDescription"));
                inventorys.setPrice(rs.getDouble("InventoryPrice"));
                inventorys.setQuantity(rs.getInt("InventoryQuantityOnHand"));
                inventorys.setAmount(rs.getInt("InventoryAmountInStock"));
                inventory.add(inventorys);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
       
        inventoryTable.setItems(inventory);
    }
    
    
    
    public void enterNewCustomer(ActionEvent event) throws IOException {
         try {    
Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/projectdb;user=root;password="
        + "password");
       Statement statement = conn.createStatement();
       
       int customerId = Integer.parseInt(customerIDTextField.getText());
    String customerName = customerNameTextField.getText();
    int customerZipcode = Integer.parseInt(zipcodeTextField.getText());
    double creditLimit = Double.parseDouble(creditLimitTextField.getText());

    if (customerIDTextField.getText().isEmpty() || customerNameTextField.getText().isEmpty() || 
            zipcodeTextField.getText().isEmpty() || creditLimitTextField.getText().isEmpty()) {
    throw new Exception("Please fill in all the required fields.");
}
    
    String insertCustomerSQL = "INSERT INTO Customers (CustomerID, CustomerName, CustomerZipcode, "
                          + "CreditLimit) " 
                          + "VALUES (" + customerId + ", '" + customerName + "', " + customerZipcode + ", " + creditLimit +
                           ")";
    statement.executeUpdate(insertCustomerSQL);
    
    System.out.println("Customer added successfully.");
    
    customerAddedLabel.setText("Customer Added!");
    
    customerIDTextField.setDisable(true);
    customerNameTextField.setDisable(true);
    zipcodeTextField.setDisable(true);
     creditLimitTextField.setDisable(true);
     customerButton.setDisable(true);
     
     
     
      } catch (SQLException e) {
    e.printStackTrace();
    
}       catch (Exception ex) {
           Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setContentText("Please fill in all of the requirments.");
    alert.showAndWait();
      System.out.println("Error: Failed to add item.");
        }
        }
    
    public void enterNewItem(ActionEvent event) throws IOException {
        try {
            Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/projectdb;user=root;password="
        + "password");
       Statement statement = conn.createStatement();
       
     int inventoryID = Integer.parseInt(itemIDTextField.getText());
String inventoryDesc = descriptionTextField.getText();
double inventoryPrice = Double.parseDouble(priceTextField.getText());
int inventoryQuantity = Integer.parseInt(quantityTextField.getText());
int inventoryAmountInStock = Integer.parseInt(amountInStockTextField.getText());

if (itemIDTextField.getText().isEmpty() || descriptionTextField.getText().isEmpty() || priceTextField.getText().isEmpty()
        || quantityTextField.getText().isEmpty() || amountInStockTextField.getText().isEmpty()) {
    throw new Exception("Please fill in all the required fields.");
}


String insertItemSQL = "INSERT INTO Inventory (InventoryID, InventoryDescription, InventoryPrice, InventoryQuantityOnHand, InventoryAmountInStock) VALUES " 
        + "(" + inventoryID + ", '" + inventoryDesc + "', " + inventoryPrice + ", " + inventoryQuantity + ", " + inventoryAmountInStock + ")";

statement.executeUpdate(insertItemSQL);

                System.out.println("Item added successfully.");
              itemAddedLabel.setText("Item Added!");
              
              itemIDTextField.setDisable(true);
              descriptionTextField.setDisable(true);
              priceTextField.setDisable(true);
              quantityTextField.setDisable(true);
              amountInStockTextField.setDisable(true);
              itemButton.setDisable(true);
             
        } catch (SQLException e) {
    e.printStackTrace();
    
}       catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setContentText("Please fill in all of the requirments.");
    alert.showAndWait();
    System.out.println("Error: Failed to add item.");
        }
    }
    
    @FXML
private void scanAction(ActionEvent event) throws IOException {
    try (
        Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/projectdb;user=root;password=password");
        PreparedStatement stmt = conn.prepareStatement(
            "SELECT * FROM Inventory WHERE InventoryAmountInStock <= 5"
        )
    ) {
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            StringBuilder message = new StringBuilder("Items with 5 or fewer in stock:\n");
            do {
                String itemName = rs.getString("InventoryDescription");
                int amountInStock = rs.getInt("InventoryAmountInStock");
                message.append(itemName).append(": ").append(amountInStock).append("\n");
            } while (rs.next());
            Alert alert = new Alert(Alert.AlertType.INFORMATION, message.toString());
            alert.setHeaderText(null);
            alert.setTitle("Low Stock Alert");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "No items with 5 or fewer in stock.");
            alert.setHeaderText(null);
            alert.setTitle("Low Stock Alert");
            alert.showAndWait();
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

 public void helpAction(ActionEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText(null);
        alert.setContentText("Made by David Cruz");
        alert.showAndWait();
    }

 
    
    public void clearCustomerAction(ActionEvent event) throws IOException {
        
        
        
            customerIDTextField.setDisable(false);
    customerNameTextField.setDisable(false);
    zipcodeTextField.setDisable(false);
     creditLimitTextField.setDisable(false);
     customerButton.setDisable(false);
     
     customerAddedLabel.setText("");
     customerIDTextField.setText("");
    customerNameTextField.setText("");
    zipcodeTextField.setText("");
     creditLimitTextField.setText("");
    }
    
    public void clearItemAction(ActionEvent event) throws IOException {
                     
              itemIDTextField.setDisable(false);
              descriptionTextField.setDisable(false);
              priceTextField.setDisable(false);
              quantityTextField.setDisable(false);
              amountInStockTextField.setDisable(false); 
              itemButton.setDisable(false);
         
              itemAddedLabel.setText("");
              itemIDTextField.setText("");
              descriptionTextField.setText("");
              priceTextField.setText("");
              quantityTextField.setText("");
              amountInStockTextField.setText("");      
    }
    
    public void enterDetailedViewAction(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("detailed-window.fxml"));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("DataSync - Order History");
        window.setScene(scene);
        window.show();
    }
    
  public static class Customer {
        public int id;
        public String name;
        public String zipcode;
        public double creditLimit;
        
        public int getId() {
            return id;
        }
        
        public void setId(int id) {
            this.id = id;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getZipcode() {
            return zipcode;
        }
        
        public void setZipcode(String zipcode) {
            this.zipcode = zipcode;
        }
        
        public double getCreditLimit() {
            return creditLimit;
        }
        
        public void setCreditLimit(double creditLimit) {
            this.creditLimit = creditLimit;
        }
                          
        
  }
    public static class Inventory {
            public int invID;
            public String desc;
            public double price;
            public int quantity;
            public int amount;
            
        
              
    public int getInvID() {
        return invID;
    }
    
    public void setInvID(int invID) {
        this.invID = invID;
    }
    
    public String getDesc() {
        return desc;
    }
    
    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public int getAmount() {
        return amount;
    }
    
    public void setAmount(int amount) {
        this.amount = amount;
    }
                              
            
        }
        
    } //end
    
 