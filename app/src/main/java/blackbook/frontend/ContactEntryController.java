package blackbook.frontend;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.ResourceBundle;
import blackbook.backend.Contact;
import blackbook.backend.ContactProcessor;
import blackbook.backend.Email;
import blackbook.backend.PhoneNumber;
import blackbook.backend.StreetAddress;
import blackbook.backend.CustomMapLayer;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import com.gluonhq.maps.MapLayer;

public class ContactEntryController implements Initializable {
    @FXML
    private ChoiceBox<String> provChoiceBox;
    @FXML
    private Button submit;
    @FXML
    private TextField nameField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField cityField;
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
    @FXML
    private TextField numberField;
    @FXML
    private TextField phoneTypeField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField emailTypeField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField addressTypeField;
    @FXML
    private Label warning;
    @FXML
    private VBox vbox;
    @FXML
    private Label title;
    
    private Contact editCon;
    

    private MapView mapView = createMapView();
    private MapPoint mp;
    private CustomMapLayer cml;
    private CustomMapLayer editLayer;
    
    private String[] provinces = {"NL", "PEI", "NS", "NB", "QC",
                                    "ON", "MB", "SK", "AB", "BC", "YT", "NT", "NU"};
    private ObservableList<PhoneNumber> phoneList = FXCollections.observableArrayList();
    private ObservableList<Email> emailList = FXCollections.observableArrayList();
    private ObservableList<StreetAddress> streetList = FXCollections.observableArrayList();
    private double x = 0;
    private double y = 0;
    private boolean editMode = false;
    
    public void setValues(Contact contact) {
        String name = contact.getName();
        LocalDate DoB = contact.getDoB();
        Integer age = contact.getAge();
        String city = contact.getCity();
        String province = contact.getProvince();
        LinkedList<PhoneNumber> phoneNumbers = contact.getPhoneNumbers();
        LinkedList<Email> emails = contact.getEmailList();
        LinkedList<StreetAddress> streetAddresses = contact.getStreetAddresses();
        Double latitude = contact.getLatitude();
        Double longitude = contact.getLongitude();
        
        
        if (DoB != null) {
            datePicker.setValue(DoB);
        }

        if(name != null){
            nameField.setText(name);
        }

        if(city != null){
            cityField.setText(city);
        }

        if(province != null){
        	provChoiceBox.setValue(province);
        }
        
        if((latitude != null) && (longitude != null)) {
        	mapView.setCenter(latitude, longitude);
        	mp = new MapPoint(latitude, longitude);
        	editLayer = new CustomMapLayer(mp);
        	mapView.addLayer(editLayer);
        }
        
        phoneList.addAll(phoneNumbers);
        emailList.addAll(emails);
        streetList.addAll(streetAddresses);
        
        title.setText("Edit Contact:");
        editMode = true;
        editCon = contact;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        provChoiceBox.getItems().addAll(provinces);

        numberColumn.setCellValueFactory(new PropertyValueFactory<PhoneNumber, String>("number"));
        phoneTypeColumn.setCellValueFactory(new PropertyValueFactory<PhoneNumber, String>("type"));
        phoneTable.setItems(phoneList);

        emailColumn.setCellValueFactory(new PropertyValueFactory<Email, String>("email"));
        emailTypeColumn.setCellValueFactory(new PropertyValueFactory<Email, String>("type"));
        emailTable.setItems(emailList);

        addressColumn.setCellValueFactory(new PropertyValueFactory<StreetAddress, String>("address"));
        addressTypeColumn.setCellValueFactory(new PropertyValueFactory<StreetAddress, String>("type"));
        streetTable.setItems(streetList);
        
        
        vbox.getChildren().add(mapView);
        VBox.setVgrow(mapView, Priority.ALWAYS);
        
        vbox.setOnMouseClicked(event -> {
        	if(event.getClickCount() == 2) {
        		if(editLayer != null) {
        			mapView.removeLayer(editLayer);
        			editLayer = null;
        		}
        		
        		
            	mp = mapView.getMapPosition(event.getX(), event.getY());
            	
            	if(cml == null) {
            		cml = new CustomMapLayer(mp);
            		mapView.addLayer(cml);
                	mapView.flyTo(0, mp, 0.1);
            		return;
            	}
            	
            	mapView.removeLayer(cml);
            	cml = new CustomMapLayer(mp);
            	mapView.addLayer(cml);
            	mapView.flyTo(0, mp, 0.1);
        	}
        });
        
    }
    
    public MapView createMapView() {
    	MapView mapView = new MapView();
    	mapView.setPrefSize(300,300);
    	mapView.setZoom(6);
    	mapView.setCenter(46.4983, -66.1596); //Center of New Brunswick :)
    	return mapView;
    }
    
    @FXML
    public void onClearPinClick() {
    	if(cml != null) {
    		mapView.removeLayer(cml);
    		mp = null;
    	}
    }
    
    @FXML
    public void hboxPressed(MouseEvent mouseEvent){
        x = mouseEvent.getSceneX();
        y = mouseEvent.getSceneY();
    }

    @FXML
    public void hboxDragged(MouseEvent mouseEvent){
        Stage stage = (Stage) provChoiceBox.getScene().getWindow();
        stage.setY(mouseEvent.getScreenY() - y);
        stage.setX(mouseEvent.getScreenX() - x);
    }

    public void addPhoneNumber(){
        String number = numberField.getText();
        if(number.isBlank() || number.isEmpty()){
            warning.setText("Error: Phone number cannot be blank");
            return;
        }
        String type = phoneTypeField.getText();

        PhoneNumber pn = new PhoneNumber(number, type);

        phoneList.add(pn);

        numberField.clear();
        phoneTypeField.clear();
        warning.setText("");
    }

    public void removePhoneNumber(){
        PhoneNumber pn = phoneTable.getSelectionModel().getSelectedItem();
        phoneList.remove(pn);
        warning.setText("");
    }

    public void addEmail(){
        String email = emailField.getText();
        if(email.isBlank() || email.isEmpty()){
            warning.setText("Error: Email cannot be blank");
            return;
        }
        String type = emailTypeField.getText();

        Email e = new Email(email, type);

        emailList.add(e);

        emailField.clear();
        emailTypeField.clear();
        warning.setText("");
    }
    public void removeEmail(){
        Email e = emailTable.getSelectionModel().getSelectedItem();
        emailList.remove(e);
        warning.setText("");
    }
    public void addAddress(){
        String address = addressField.getText();
        if(address.isBlank() || address.isEmpty()){
            warning.setText("Error: Address cannot be blank");
            return;
        }

        String type = addressTypeField.getText();

        StreetAddress sa = new StreetAddress(address, type);

        streetList.add(sa);

        addressField.clear();
        addressTypeField.clear();
        warning.setText("");
    }
    public void removeAddress(){
        StreetAddress sa = streetTable.getSelectionModel().getSelectedItem();
        streetList.remove(sa);
        warning.setText("");
    }

    public void buildContact() throws IOException {
        String name = nameField.getText();
        if(name.isEmpty() || name.isBlank()){
            warning.setText("Error: Must have a name");
            return;
        };

        Contact contact = new Contact(name);

        LocalDate date = datePicker.getValue();
        if(!(date == null)){
            contact.setDoB(date);
        }

        String city = cityField.getText();
        if(!(city.isBlank() || city.isEmpty())){
            contact.setCity(city);
        }

        String province = provChoiceBox.getValue();
        if(!(province == null)){
            contact.setProvince(province);
        }

        if(!(phoneList.isEmpty())){
            contact.setPhoneNumbers(phoneList);
        }

        if(!(emailList.isEmpty())){
            contact.setEmails(emailList);
        }

        if(!(streetList.isEmpty())){
            contact.setStreetAddresses(streetList);
        }
        
        if(mp != null) {
        	contact.setLatitude(mp.getLatitude());
        	contact.setLongitude(mp.getLongitude());
        }

        ObservableList<Contact> list = ContactProcessor.getSavedContacts();
        
        
        list.add(contact);
        if(editMode) {
        	list.remove(editCon);
        }
        ContactProcessor.overwriteSave(list);
        
        Stage stage = (Stage) submit.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void closeWindow(){
        Stage stage = (Stage) submit.getScene().getWindow();
        stage.close();
    }
}
