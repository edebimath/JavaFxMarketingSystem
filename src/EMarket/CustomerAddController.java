package EMarket;

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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
    private Label label;
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
    @FXML
    private ComboBox emarketBox;

    ObservableList<String> emarketList = FXCollections.observableArrayList();
    Connection connection = null;
    PreparedStatement ps = null;
    PreparedStatement ps2, ps3 = null;
    ResultSet rs = null;
    Boolean editMode = false;

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) mainPanel.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void addCustomertoEMarket(ActionEvent event) throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/coursework", "root", "");
            String cust_id = CustId.getText();
            String cust_name = CustName.getText();
            String cust_age = CustAge.getText();
            String cust_address = CustAddress.getText();
            String cust_gender;
            if (genderMale.isSelected()) {
                cust_gender = "Male";
            } else {
                cust_gender = "Female";
            }
            //It inserts the customer table.
            String sql = " INSERT INTO customer VALUES (?,?,?,?,?)";
            ps = connection.prepareStatement(sql);
            ps.setString(1, cust_id);
            ps.setString(2, cust_name);
            ps.setString(3, cust_age);
            ps.setString(4, cust_address);
            ps.setString(5, cust_gender);
            ps.executeUpdate();
            String emarket_name = (String) emarketBox.getSelectionModel().getSelectedItem();
            //It inserts customer to emarket.
            String sql2 = "Insert into custofemarket values (?,?)";
            ps3 = connection.prepareStatement(sql2);
            ps3.setString(1, emarket_name);
            ps3.setString(2, cust_id);
            ps3.executeUpdate();

        } catch (SQLException s) {
            s.printStackTrace();
        }
        Alert allert = new Alert(Alert.AlertType.INFORMATION); // we catch the error
        allert.setHeaderText("The process of adding a customer is complete");
        allert.setContentText("New customer " + CustId.getText() + " is created and added to the " + (String) emarketBox.getSelectionModel().getSelectedItem());
        allert.showAndWait();
        Stage stage = (Stage) mainPanel.getScene().getWindow();
        stage.close();
    }
    //CustomerAddController has same.
    private void comboBoxItems() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/coursework", "root", "");
        String sql = "Select * from emarket";
        ps = connection.prepareStatement(sql);
        rs = ps.executeQuery();
        emarketBox.getItems().removeAll(emarketList);
        while (rs.next()) {
            emarketList.add(rs.getString("emarketname"));
        }
        emarketBox.setItems(emarketList);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            comboBoxItems();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(CustomerAddController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
