
package EMarket;

import Customer.CustomerConstructController;
import Customer.CustomerConstructController.Customer;
import DBConnection.database;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class ShowCustomerController implements Initializable {
    Connection connection = null; 
    PreparedStatement ps = null;
    ResultSet rs = null;
    Statement st = null;
    
    ObservableList<Customer> customerList = FXCollections.observableArrayList();
    @FXML
    private TableView<Customer> CustomerTable;
    @FXML
    private TableColumn<Customer,String> custIdColumn;
    @FXML
    private TableColumn<Customer,String> custNameColumn;
    @FXML
    private TableColumn<Customer,String> custAgeColumn;
    @FXML
    private TableColumn<Customer,String> custAddressColumn;
    @FXML
    private TableColumn<Customer,String> custGenderColumn;
    @FXML
    private AnchorPane mainPanel;
    @FXML
    private ComboBox emarketBox;
    @FXML 
    private Button btn;
    ObservableList<String> emarketList= FXCollections.observableArrayList();
    ArrayList<String> tempCustomerList = new ArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            database.connection();

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(CustomerConstructController.class.getName()).log(Level.SEVERE, null, ex);
        }
        InitCol();
        try {
            comboBoxItems();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ShowCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //CustomerAddController has the same.
    private void comboBoxItems() throws ClassNotFoundException,SQLException{
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/coursework", "root", "");
        String sql = "Select * from emarket";
        ps = connection.prepareStatement(sql);
        rs = ps.executeQuery();
        emarketBox.getItems().removeAll(emarketList);
        while(rs.next()){
            emarketList.add(rs.getString("emarketname"));
            
        }
        emarketBox.setItems(emarketList);
    }
    
    

    @FXML//Hardest code of the project. Loads the records of customer of the selected E-Market with the emarketname knowledge.
    private void loadData() throws ClassNotFoundException, SQLException{
        CustomerTable.getItems().removeAll();
        //At first we should remove the items of table, after each pushing, because preventing duplicating.
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/coursework","root","");
        String emarket_name=(String) emarketBox.getSelectionModel().getSelectedItem();
        //Assigning the selected item of combobox to a string.
        String sql = "Select custid from custofemarket where `emarketname`='"+emarket_name+"'";
        ps = connection.prepareStatement(sql);
        rs = ps.executeQuery();//RS takes the custid of every customers of selected E-Market.
        while(rs.next()){
            tempCustomerList.add(rs.getString("custid"));
            //We got all the elements of custid in an arraylist.
        }
        for(int i=0; i<tempCustomerList.size();i++){//Runs it for size of arraylist.
            String sql2= "Select * from customer where `id`='"+tempCustomerList.get(i)+"'";
            //*************Her bir custid için yeni bir sorgu oluşturuyoruz. Başka bir yolu yok.****************
            ps = connection.prepareStatement(sql2);
            rs = ps.executeQuery();
            try{
                rs.next();//Değerleri almak için her seferinde bunu çalıştırmamız gerekiyor.
                String custId= rs.getString("id");
                String custName= rs.getString("name");
                String custAge= rs.getString("age");
                String custAddress= rs.getString("address");
                String custGender = rs.getString("gender");
                customerList.add(new CustomerConstructController.Customer(custId, custName, custAge, custAddress, custGender));
                //Observable arrayListe atanıyor burada.
            } catch (SQLException e){
            
            }
        }
        //Döngüden çıkınca da tabloya atanıyor.
        CustomerTable.getItems().setAll(customerList);
        tempCustomerList.clear();//Bunu da boşaltmamız gerekiyor. İkilik yaratmaması için.
        customerList.clear();//Same.
        
    }
    
    private void InitCol(){
        custIdColumn.setCellValueFactory(new PropertyValueFactory<>("CustId"));
        custNameColumn.setCellValueFactory(new PropertyValueFactory<>("CustName"));
        custAgeColumn.setCellValueFactory(new PropertyValueFactory<>("CustAge"));
        custAddressColumn.setCellValueFactory(new PropertyValueFactory<>("CustAddress"));
        custGenderColumn.setCellValueFactory(new PropertyValueFactory<>("CustGender"));
    }
    
}
