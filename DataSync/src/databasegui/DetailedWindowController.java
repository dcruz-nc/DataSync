package databasegui;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class DetailedWindowController implements Initializable {

       @FXML
    private TableView<OrderHistory> orderTableView;

    @FXML
    private TableColumn<OrderHistory, String> customerColumn;

    @FXML
    private TableColumn<OrderHistory, Integer> idColumn;

    @FXML
    private TableColumn<OrderHistory, Double> priceColumn;

    @FXML
    private TableColumn<OrderHistory, String> dateColumn;
    
    @FXML
    private TextField searchField;
            

    private ObservableList<OrderHistory> orderList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("inventoryID"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));

        orderList = FXCollections.observableArrayList();

        try {
          Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/projectdb;user=root;password="
        + "password");
            String query = "SELECT Customers.CustomerName, Inventory.InventoryID, Sales.OrderDate, Inventory.InventoryPrice FROM Customers JOIN Sales ON Customers.CustomerID = Sales.CustomerID JOIN Inventory ON Sales.InventoryID = Inventory.InventoryID";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String customerName = rs.getString("CustomerName");
                int inventoryID = rs.getInt("InventoryID");
                double price = rs.getDouble("InventoryPrice");
                String orderDate = rs.getDate("OrderDate").toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
                orderList.add(new OrderHistory(customerName, inventoryID, price, orderDate));
            }

            orderTableView.setItems(orderList);

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.err.println("Error: " + e);
        }
     FilteredList<OrderHistory> filteredOrders = new FilteredList<>(orderList, p -> true);

    
    orderTableView.setItems(filteredOrders);

    
    searchField.textProperty().addListener((observable, oldValue, newValue) -> {
        filteredOrders.setPredicate(order -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }

            String lowerCaseFilter = newValue.toLowerCase();

            if (order.getCustomerName().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            }

            return false;
        });
    });

    }
    
    public void backAction(ActionEvent event) throws IOException {
            Parent parent = FXMLLoader.load(getClass().getResource("choice-window.fxml"));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("DataSync - Main Menu");
        window.setScene(scene);
        window.show();
        }
    
     public void helpAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText(null);
        alert.setContentText("Made by David Cruz");
        alert.showAndWait();
    }
    
    public class OrderHistory {
        private String customerName;   
        private int inventoryID;
        private double price;
        private LocalDate orderDate;
        
        public OrderHistory(String customerName, int inventoryID, double price, String orderDate) {
    this.customerName = customerName;
    this.inventoryID = inventoryID;
    this.price = price;
    this.orderDate = LocalDate.parse(orderDate, DateTimeFormatter.ISO_LOCAL_DATE);
}
        
        


        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public LocalDate getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(LocalDate orderDate) {
            this.orderDate = orderDate;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
        
        public double getInventoryID() {
            return inventoryID;
        }
        
        public void setInventoryID(int InventoryID) {
            this.inventoryID = inventoryID;
        }
    }
}



