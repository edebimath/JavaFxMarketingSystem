
package EMarket;

import DBConnection.database;
import EMarket.EMarketConstructController.EMarket;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class EMarketAddController implements Initializable {
    
    @FXML
    private AnchorPane mainPanel;
    @FXML
    private Label label;
    @FXML
    private TextField ahmet;
    @FXML
    private TextField mehmet;
    @FXML
    private Button addButton;
    @FXML
    private Button check;
    @FXML
    private Button cancelButton;
    
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null ;
    Boolean editMode = false;

    @FXML
    private void cancel(ActionEvent event){
        Stage stage =(Stage) mainPanel.getScene().getWindow();
        stage.close();
    }
    @FXML
    private void addEMarket(ActionEvent event) throws SQLException,ClassNotFoundException{
        try{
            String abc = ahmet.getText();
            String def = mehmet.getText();
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/coursework", "root", "");
            
            if(editMode){
                updateEMarket();
            } else {
                String sql3 = "INSERT INTO emarket VALUES (?,?)";
                ps = connection.prepareStatement(sql3);
                ps.setString(1, abc);
                ps.setString(2, def);
                ps.executeUpdate();
            
            }
        } catch (SQLException s ){
            s.printStackTrace();
        }
        Alert al = new Alert(Alert.AlertType.INFORMATION);
        al.setHeaderText("E-Market is added..");
        al.setContentText("Please select a new operation to execute..");
        al.showAndWait();
        Stage stage = (Stage) mainPanel.getScene().getWindow();
        stage.close();
    }
    public void getTextForUpdate(EMarket eMarket){
        ahmet.setText(eMarket.getEmarketId());
        mehmet.setText(eMarket.getEmarketName());
        editMode = true;
    }
    @FXML//Just for trying. Makes nothing.
    public void yazdır(ActionEvent event){
    }
    
    public void updateEMarket() throws SQLException, ClassNotFoundException{
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/coursework", "root", "");
        EMarket emarket = new EMarket(ahmet.getText(), mehmet.getText());
        
        String sql = "Update emarket set emarketid=?, emarketname=? where emarketid=?";
        
        ps = connection.prepareStatement(sql);
        ps.setString(1, emarket.getEmarketId());
        ps.setString(2, emarket.getEmarketName());
        ps.setString(3, emarket.getEmarketId());
        ps.executeUpdate();
        
        Stage stage = (Stage) mainPanel.getScene().getWindow();
        stage.close();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            database.connection();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(EMarketAddController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}
