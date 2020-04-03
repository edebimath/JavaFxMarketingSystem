
package Customer;

import DBConnection.database;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CustomerMainController implements Initializable {
    
    @FXML
    private Label label;
    @FXML
    private Button addCustomer;
    @FXML
    private Button showCustomer;
    
    
    @FXML
    private void showCustomerTable(ActionEvent event) {
        LoadWindow("CustomerConstruct.fxml", "Showing Customers..");
    }
    @FXML
    private void addCustomerWindow(ActionEvent event) {
        LoadWindow("CustomerAdd.fxml", "New Customer adding..");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            database.connection();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(CustomerMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    void LoadWindow(String loc,String title){
        
        try{
          
            Parent parent = FXMLLoader.load(getClass().getResource(loc));
            Stage stage = new Stage(StageStyle.DECORATED );
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
        }
        catch(IOException e){
            Logger.getLogger(CustomerMainController.class.getName()).log(Level.SEVERE,null,e);
            
        }
    }
}
