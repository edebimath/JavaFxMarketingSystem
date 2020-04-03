
package EMarket;

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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class EMarketConstructController implements Initializable {
    
    ObservableList<EMarket> emarketList = FXCollections.observableArrayList();
    @FXML
    private Button btn, btn2, btn3;
    @FXML
    private TableView<EMarket> EmarketTable;
    @FXML
    private Label label;
    @FXML
    private TableColumn<EMarket,String> emarketIdColumn;
    @FXML
    private TableColumn<EMarket,String> emarketNameColumn;
    @FXML
    private AnchorPane mainPanel;
    
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    Statement st = null;
    
    
    public static class EMarket {
        private final SimpleStringProperty EmarketId;
        private final SimpleStringProperty EmarketName;
        
        public EMarket(String EmarketId, String EmarketName){
            this.EmarketId = new SimpleStringProperty(EmarketId);
            this.EmarketName = new SimpleStringProperty(EmarketName);
            
        }

        public String getEmarketId() {
            return EmarketId.get();
        }

        public String getEmarketName() {
            return EmarketName.get();
        }
    }
    
    private void loadData() throws ClassNotFoundException,SQLException{
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/coursework","root","");
        String sql = "Select * from emarket";
        ps = connection.prepareStatement(sql);
        rs = ps.executeQuery();
        try{
            while(rs.next()){
            String emarketId = rs.getString("emarketid");
            String emarketName = rs.getString("emarketname");
            emarketList.add(new EMarket(emarketId, emarketName));
            }
        } catch (SQLException e){    
        }
        
        EmarketTable.getItems().setAll(emarketList);
            
    }
    @FXML
    private void cancel(ActionEvent event){
        Stage stage =(Stage) mainPanel.getScene().getWindow();
        stage.close();
    }
    @FXML
    private void deleteEMarket(ActionEvent event) throws SQLException, ClassNotFoundException{
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/coursework","root","");
        
        EMarket deletemar = EmarketTable.getSelectionModel().getSelectedItem();
        if(deletemar == null){
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setHeaderText("You didn't select the E-Market..");
            al.setContentText("Please choose E-Market for delete operation..");
            al.showAndWait();    
        } else{
            String sql = "delete from emarket where emarketname='"+deletemar.getEmarketName()+"'";
            System.out.println(sql);
            Statement st = connection.createStatement();
            st.executeUpdate(sql);
            //2nd query deletes all the customers of the e-market. 
            String sql2="delete from custofemarket where emarketname='"+deletemar.getEmarketName()+"'";
            Statement st2= connection.createStatement();
            st2.executeUpdate(sql2);
            
        Alert al = new Alert(Alert.AlertType.INFORMATION);
        al.setHeaderText("Delete operation is executed..");
        al.setContentText("Please choose new operation to execute..");
        al.showAndWait();
            
        Stage stage = (Stage) mainPanel.getScene().getWindow();
        stage.close();
        }
    }
    
    @FXML
    private void editEMarket(ActionEvent event) throws ClassNotFoundException, SQLException, IOException{
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/coursework","root","");
        
        EMarket edit = EmarketTable.getSelectionModel().getSelectedItem();
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("EMarketAdd.fxml"));
        Parent parent = loader.load();
        
        EMarketAddController controller = loader.getController();
        controller.getTextForUpdate(edit);
        
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Editing operation");
        stage.setScene(new Scene (parent));
        stage.show();
           
        Stage stage1 = (Stage) mainPanel.getScene().getWindow();
        stage1.close();
        
    }
    
    private void InitCol(){
        emarketIdColumn.setCellValueFactory(new PropertyValueFactory<>("EmarketId"));
        emarketNameColumn.setCellValueFactory(new PropertyValueFactory<>("EmarketName"));
        
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            database.connection();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(EMarketConstructController.class.getName()).log(Level.SEVERE, null, ex);
        }
        InitCol();
        try {
            loadData();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(EMarketConstructController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}