package blackbook.frontend;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Optional;
import java.util.ResourceBundle;

import blackbook.backend.Contact;
import blackbook.backend.ContactProcessor;
import blackbook.backend.CustomMapLayer;
import blackbook.backend.Email;
import blackbook.backend.PhoneNumber;
import blackbook.backend.StreetAddress;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import com.gluonhq.maps.MapLayer;

public class InfoScreenController implements Initializable {
    @FXML
    private TableView<PhoneNumber> phoneTable;
    @FXML
    private TableView<Email> emailTable;
    @FXML
    private TableView<StreetAddress> streetTable;
    @FXML
    private TableColumn<PhoneNumber, String> numberColumn;
    @FXML
    private TableColumn<PhoneNumber, String> phoneTypeColumn;
    @FXML
    private TableColumn<Email, String> emailColumn;
    @FXML
    private TableColumn<Email, String> emailTypeColumn;
    @FXML
    private TableColumn<StreetAddress, String> addressColumn;
    @FXML
    private TableColumn<StreetAddress, String> addressTypeColumn;
    private ObservableList<PhoneNumber> phoneList = FXCollections.observableArrayList();
    private ObservableList<Email> emailList = FXCollections.observableArrayList();
    private ObservableList<StreetAddress> streetList = FXCollections.observableArrayList();

    private double x = 0;
    private double y = 0;
    private Contact con;

    @FXML
    private Text nameText;
    @FXML
    private Text DoBText;
    @FXML
    private Text cityText;
    @FXML
    private Text provText;
    @FXML
    private VBox vbox;

    public void setValues(Contact contact){
    	con = contact;
        String name = contact.getName();
        LocalDate DoB = contact.getDoB();
        Integer age = contact.getAge();
        String city = contact.getCity();
        String province = contact.getProvince();
        Double latitude = contact.getLatitude();
        Double longitude = contact.getLongitude();

        if (DoB != null) {
            String dobStr = DoB.toString() + " (Age: " + age.toString() + ")";
            DoBText.setText(dobStr);
        }

        if(name != null){
            nameText.setText(name);
        }

        if(city != null){
            cityText.setText(city);
        }

        if(province != null){
            provText.setText(province);
        }
        
        if((latitude != null) && (longitude != null)) {
        	vbox.setVisible(true);
        	MapView mapView = createMapView(latitude, longitude);
            vbox.getChildren().add(mapView);
            MapPoint mp = new MapPoint(latitude, longitude);
            CustomMapLayer cml = new CustomMapLayer(mp);
    		mapView.addLayer(cml);
            VBox.setVgrow(mapView, Priority.ALWAYS);
        }
        
        phoneList.addAll(contact.getPhoneNumbers());
        emailList.addAll(contact.getEmailList());
        streetList.addAll(contact.getStreetAddresses());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        numberColumn.setCellValueFactory(new PropertyValueFactory<PhoneNumber, String>("number"));
        phoneTypeColumn.setCellValueFactory(new PropertyValueFactory<PhoneNumber, String>("type"));
        phoneTable.setItems(phoneList);

        emailColumn.setCellValueFactory(new PropertyValueFactory<Email, String>("email"));
        emailTypeColumn.setCellValueFactory(new PropertyValueFactory<Email, String>("type"));
        emailTable.setItems(emailList);

        addressColumn.setCellValueFactory(new PropertyValueFactory<StreetAddress, String>("address"));
        addressTypeColumn.setCellValueFactory(new PropertyValueFactory<StreetAddress, String>("type"));
        streetTable.setItems(streetList);
    }

    public MapView createMapView(double lat, double lon) {
    	MapView mapView = new MapView();
    	mapView.setPrefSize(300,300);
    	mapView.setZoom(15);
    	mapView.setCenter(lat, lon);
    	return mapView;
    }
    
    @FXML
    public void onDeleteClick() {
    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setTitle("Contact Deletion");
        alert.setHeaderText("Are you sure you want to delete this contact?");
        alert.setContentText("Once a contact is deleted, you cannot recover it.");

        Optional<ButtonType> result = alert.showAndWait();
        
        
        if(result.isPresent() && result.get() == ButtonType.OK){
        	ObservableList<Contact> list = ContactProcessor.getSavedContacts();
            list.remove(con);
            ContactProcessor.overwriteSave(list);
        }
        
        onExitClick();
    }
    
    @FXML
    public void onEditClick() throws IOException{
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/contactEntry.fxml"));
        Parent root = loader.load();
        ContactEntryController contactEntryController = loader.getController();
        contactEntryController.setValues(con);
        
        Stage stage = (Stage) (vbox.getScene().getWindow());
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Edit Contact");
        stage.show();
    }
    
    @FXML
    protected void onExitClick(){
        Stage stage = (Stage) nameText.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void hboxPressed(MouseEvent mouseEvent){
        x = mouseEvent.getSceneX();
        y = mouseEvent.getSceneY();
    }

    @FXML
    public void hboxDragged(MouseEvent mouseEvent){
        Stage stage = (Stage) phoneTable.getScene().getWindow();
        stage.setY(mouseEvent.getScreenY() - y);
        stage.setX(mouseEvent.getScreenX() - x);
    }
}
