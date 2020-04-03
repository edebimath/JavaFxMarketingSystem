
package enterence;

import Customer.CustomerMainController;
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

public class EnterenceWindowController implements Initializable {
    
    @FXML private Label label;
    @FXML private Button btn;
    @FXML private Button btn2;
    
    @FXML
    private void CustomerLoad(ActionEvent event){
        LoadWindow("/Customer/CustomerMain.fxml", "Customer Main Controller..");
    }
    
    @FXML
    private void EMarketLoad(ActionEvent event){
        LoadWindow("/EMarket/EMarketmain.fxml", "E-Market Main Controller");
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
