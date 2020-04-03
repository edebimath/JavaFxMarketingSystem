package Customer;

import DBConnection.database;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CustomerConstructController implements Initializable {

    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    Statement st = null;

    ObservableList<Customer> customerList = FXCollections.observableArrayList();
    @FXML
    private TableView<Customer> CustomerTable;
    @FXML
    private TableColumn<Customer, String> custIdColumn;
    @FXML
    private TableColumn<Customer, String> custNameColumn;
    @FXML
    private TableColumn<Customer, String> custAgeColumn;
    @FXML
    private TableColumn<Customer, String> custAddressColumn;
    @FXML
    private TableColumn<Customer, String> custGenderColumn;
    @FXML
    private AnchorPane mainPanel;
    @FXML
    private Button addButton;
    @FXML
    private ComboBox emarketBox;
    ObservableList<String> emarketList = FXCollections.observableArrayList();
   
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        try {
            database.connection();

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(CustomerConstructController.class.getName()).log(Level.SEVERE, null, ex);
        }
        InitCol();
        try {
            loadData();
            comboBoxItems();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(CustomerConstructController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //FXML olmayan her dosyayı initialize etmemiz gerekiyor.
    private void comboBoxItems() throws ClassNotFoundException, SQLException{
        //It loads the variables of combobox.
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/coursework", "root", "");
        String sql = "Select * from emarket";
        ps = connection.prepareStatement(sql);
        rs = ps.executeQuery();//Result set takes the query result.
        emarketBox.getItems().removeAll(emarketList);
        //Before starting to execute variables, combobox should be empty, because of prevent duplication.
        while(rs.next()){
            emarketList.add(rs.getString("emarketname"));
            //We assign an arraylist takes the result table's one column.
        }
        emarketBox.setItems(emarketList);
    }
    
    @FXML
    private void addtoanEMarket(ActionEvent event) throws ClassNotFoundException, SQLException{
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/coursework", "root", "");
        String emarket_name =(String) emarketBox.getSelectionModel().getSelectedItem();
        Customer selection = CustomerTable.getSelectionModel().getSelectedItem();
        //Tablodan seçilen özelliği alıyoruz.
        if (selection == null || emarket_name==null) {
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setHeaderText("You didn't select the Customer or E-Market..");
            al.setContentText("Please choose customer and e-market for adding operation..");
            al.showAndWait();
        } else {
            String sql = "Insert into custofemarket values (?,?)";
            ps = connection.prepareStatement(sql);
            ps.setString(1, emarket_name);
            ps.setString(2, selection.getCustId());
            ps.executeUpdate();
            
            Alert al = new Alert(Alert.AlertType.CONFIRMATION);
            al.setHeaderText("Add operation was executed successfully..");
            al.setContentText(selection.getCustName()+" is added to the "+emarket_name);
            al.showAndWait();
        } 
    }
    //Showing the customers
    private void loadData() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/coursework", "root", "");
        String sql = "Select * from customer";
        ps = connection.prepareStatement(sql);
        rs = ps.executeQuery();
        try {//We don't have to remove the items of the table, because it works in the initialization code.
            while (rs.next()) {//It changes the result set's index after every execution.
                String custId = rs.getString("id");
                String custName = rs.getString("name");
                String custAge = rs.getString("age");
                String custAddress = rs.getString("address");
                String custGender = rs.getString("gender");
                customerList.add(new Customer(custId, custName, custAge, custAddress, custGender));
            }
        } catch (SQLException e) {

        }
        CustomerTable.getItems().setAll(customerList);
        
    }

    public static class Customer {

        private final SimpleStringProperty CustId;
        private final SimpleStringProperty CustName;
        private final SimpleStringProperty CustAge;
        private final SimpleStringProperty CustAddress;
        private final SimpleStringProperty CustGender;

        public Customer(String CustId, String CustName, String CustAge, String CustAddress, String CustGender) {
            this.CustId = new SimpleStringProperty(CustId);
            this.CustName = new SimpleStringProperty(CustName);
            this.CustAge = new SimpleStringProperty(CustAge);
            this.CustAddress = new SimpleStringProperty(CustAddress);
            this.CustGender = new SimpleStringProperty(CustGender);
        }
        //Burada getter'larda SimpleStringProperty özelliği olarak get'ten sonra büyük harfle yazılmalı özellik.
        //Çok farklı bir hata. Kesinlikle dikkat edilmeli.
        public String getCustId() {
            return CustId.get();
        }

        public String getCustName() {
            return CustName.get();
        }

        public String getCustAge() {
            return CustAge.get();
        }

        public String getCustAddress() {
            return CustAddress.get();
        }

        public String getCustGender() {
            return CustGender.get();
        }
    }

    @FXML
    private void deleteCustomer(ActionEvent event) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/coursework", "root", "");

        Customer delete = CustomerTable.getSelectionModel().getSelectedItem();
        if (delete == null) {
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setHeaderText("You didn't select the customer..");
            al.setContentText("Please choose customer for delete operation..");
            al.showAndWait();
        } else {//Deletes the record from main customer table.
            String sql = "delete from customer where id='" + delete.getCustId() + "'";
            Statement st = connection.createStatement();
            st.executeUpdate(sql);
            //Deletes the customer of emarket, if it is.
            Statement st2 = connection.createStatement();
            String sql2 = "delete from custofemarket where custid='"+delete.getCustId()+"'";
            st2.executeUpdate(sql2);

            Alert al = new Alert(Alert.AlertType.INFORMATION);
            al.setHeaderText("Delete operation is executed..");
            al.setContentText(delete.getCustName()+"was deleted from Customer List and E-Market's Customer List, if it is..");
            al.showAndWait();

            Stage stage = (Stage) mainPanel.getScene().getWindow();
            stage.close();

        }
    }

    @FXML//Edit operation works to guides to add operation.
    private void editCustomer(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/coursework", "root", "");

        Customer edit = CustomerTable.getSelectionModel().getSelectedItem();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerAdd.fxml"));
        //FXMLLoader object has created and run as a child operation.
        Parent parent = loader.load();

        CustomerAddController controller = loader.getController();
        controller.getTextForUpdate(edit);
        //With etTextforUpdate we gave the current values to the add operation. User can see current values and can make chance
        //on it.

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Editing operation");
        stage.setScene(new Scene(parent));
        stage.show();

        Stage stage1 = (Stage) mainPanel.getScene().getWindow();
        stage1.close();
    }

    private void InitCol() {
        custIdColumn.setCellValueFactory(new PropertyValueFactory<>("CustId"));//Burada CustId'nin customer class'ında 
        //tanımlandığı gibi yazılması önemli!.
        custNameColumn.setCellValueFactory(new PropertyValueFactory<>("CustName"));
        custAgeColumn.setCellValueFactory(new PropertyValueFactory<>("CustAge"));
        custAddressColumn.setCellValueFactory(new PropertyValueFactory<>("CustAddress"));
        custGenderColumn.setCellValueFactory(new PropertyValueFactory<>("CustGender"));
    }

}
