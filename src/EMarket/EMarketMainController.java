
package EMarket;

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

public class EMarketMainController implements Initializable {
    
    @FXML
    private Label label;
    @FXML
    private Button showEMarket;
    @FXML
    private Button addEMarket;
    @FXML
    private Button addCustomertoEMarket;
    @FXML
    private Button showCustomersOfEMarket;
    
    @FXML
    private void showEMarketTable(ActionEvent event){
        LoadWindow("EMarketConstruct.fxml", "Showing E-Markets..");
    }
    @FXML
    private void addEMarketTable(ActionEvent event){
        LoadWindow("EMarketAdd.fxml", "E-Market loader..");
    }
    @FXML
    private void addCustomertoEMarket(ActionEvent event){
        LoadWindow("CustomerAdd.fxml", "Adding new Customer to E-Market..");
    }
    @FXML
    private void showCustomersofEMarket(ActionEvent event){
        LoadWindow("ShowCustomer.fxml", "Customers of selected E-Market..");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            database.connection();
        } catch (SQLException | ClassNotFoundException ex) {
           
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
            e.printStackTrace();
        }
    }    
    
}
