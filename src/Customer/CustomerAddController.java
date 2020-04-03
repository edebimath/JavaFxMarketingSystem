package Customer;

import Customer.CustomerConstructController.Customer;
import DBConnection.database;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.event.ActionEvent;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CustomerAddController implements Initializable {

    @FXML
    private AnchorPane mainPanel;
    @FXML
    private Label merhaba;
    @FXML
    private TextField CustId;
    @FXML
    private TextField CustName;
    @FXML
    private TextField CustAge;
    @FXML
    private TextField CustAddress;
    @FXML
    private ToggleGroup genderGroup;
    @FXML
    private RadioButton genderMale;
    @FXML
    private RadioButton genderFemale;
    @FXML
    private Button addButton;
    @FXML
    private Button cancelButton;

    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    Boolean editMode = false;
    

    @FXML//Close button's action.
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) mainPanel.getScene().getWindow();
        //
        stage.close();
    }

    @FXML//Add new customer from TextFields and Radio button of gender.
    private void addCustomer(ActionEvent event) throws SQLException, ClassNotFoundException {
        try {
            //Try to connect database server.
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/coursework", "root", "");
            String cust_id = CustId.getText();
            String cust_name = CustName.getText();
            String cust_age = CustAge.getText();
            String cust_address = CustAddress.getText();
            String cust_gender;
            //RadioButton'lar birbirlerine togglegroup olarak bağlı olduğundan hangisi seçiliyse onu değişkene atıyoruz.
            if (genderMale.isSelected()) {
                cust_gender = "Male";
            } else {
                cust_gender = "Female";
            }
            if (editMode) {//We check if the edit button is pushed.
                updateCustomer();
            } else {
                //First creating a query sentence as a string.
                String sql2 = " INSERT INTO customer VALUES (?,?,?,?,?)";
                //Then we give it as the param of prepareStatement of connection(It assigned to new PreparedStatement)
                //We use PS to assign the values of insertion in an easy way.
                ps = connection.prepareStatement(sql2);
                ps.setString(1, cust_id);
                ps.setString(2, cust_name);
                ps.setString(3, cust_age);
                ps.setString(4, cust_address);
                ps.setString(5, cust_gender);
                ps.executeUpdate();
                //ExecuteUpdate is usable in many cases. executeQuery is not usable in insert and delete functions.
            }
        } catch (SQLException s) {
            s.printStackTrace();

        }
        Alert allert = new Alert(Alert.AlertType.INFORMATION);
        allert.setHeaderText("The process of adding a customer is complete");
        allert.setContentText(CustName.getText()+" added to the Customer System.");
        allert.showAndWait();
        Stage stage = (Stage) mainPanel.getScene().getWindow();
        stage.close(); // we close the window when we clicked the cancel button

    }
    //This code is using in CustomerConstructController. It works as when we update a custumer it loads the previous values.
    public void getTextForUpdate(Customer cust) {
        CustId.setText(cust.getCustId());
        CustName.setText(cust.getCustName());
        CustAge.setText(cust.getCustAge());
        CustAddress.setText(cust.getCustAddress());
        genderGroup.selectedToggleProperty().getName();//!!!I'm not sure
        editMode = true;
    }

    public void updateCustomer() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/coursework", "root", "");
        String CustGender;
            if (genderMale.isSelected()) {
                CustGender = "Male";
            } else {
                CustGender = "Female";
            }
        Customer cust = new Customer(
                CustId.getText(),
                CustName.getText(),
                CustAge.getText(),
                CustAddress.getText(),
                CustGender);

        String sql = "Update customer set name=?, age=?, address=?, gender=? where id=?";

        ps = connection.prepareStatement(sql);
        ps.setString(1, cust.getCustName());
        ps.setString(2, cust.getCustAge());
        ps.setString(3, cust.getCustAddress());
        ps.setString(4, cust.getCustGender());
        ps.setString(5, cust.getCustId());
        ps.executeUpdate();//executeQuery is not working here! It is data manipulation.

        Stage stage = (Stage) mainPanel.getScene().getWindow();
        stage.close();

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            database.connection();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(CustomerAddController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
