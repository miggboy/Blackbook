package blackbook.frontend;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import blackbook.backend.Contact;
import blackbook.backend.ContactProcessor;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class PrimaryController implements Initializable{
    @FXML
    private TextField searchField;
    @FXML
    private TableView<Contact> contactTable;

    @FXML
    private TableColumn<Contact, String> nameColumn;

    @FXML
    private TableColumn<Contact, LocalDate>  dateColumn;

    @FXML
    private TableColumn<Contact, String> cityColumn;

    @FXML
    private TableColumn<Contact, String> provColumn;

    @FXML
    private ImageView minimize;

    private double x = 0;
    private double y = 0;

    @FXML
    public void onContactClick(){
        contactTable.setVisible(true);
    }

    ObservableList<Contact> list = ContactProcessor.getSavedContacts();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        nameColumn.setCellValueFactory(new PropertyValueFactory<Contact, String>("name"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<Contact, LocalDate>("DoB"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<Contact, String>("city"));
        provColumn.setCellValueFactory(new PropertyValueFactory<Contact, String>("province"));

        contactTable.setItems(list);

        FilteredList<Contact> filteredList = new FilteredList<>(list, b -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(Contact -> {
                if(newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    return true;
                }

                String searchKey = newValue.toLowerCase();

                if(Contact.getName().toLowerCase().contains(searchKey)){
                    return true;
                }
                else if(Contact.getDoB() != null && Contact.getDoB().toString().contains(searchKey)){
                    return true;
                }
                else if(Contact.getCity() != null && Contact.getCity().toLowerCase().contains(searchKey)){
                    return true;
                }
                else if(Contact.getProvince() != null && Contact.getProvince().toLowerCase().contains(searchKey)) {
                    return true;
                }else return false;
            });
        });

        SortedList<Contact> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(contactTable.comparatorProperty());

        contactTable.setItems(sortedList);
        
        
		contactTable.setOnMouseClicked(event ->{
			if(event.getClickCount() == 2) {
				try {
					onMoreInfoClick();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
        
        
    }

    @FXML
    public void onAddContactClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/contactEntry.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Contact Entry Scene");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.showAndWait();

        list.clear();

        ObservableList<Contact> reList = ContactProcessor.getSavedContacts();
        Contact[] arr = new Contact[reList.size()];
        for(int i = 0; i < arr.length; i++){
            arr[i] = reList.get(i);
        }

        list.addAll(arr);
    }

    @FXML
    public void onRemoveClick(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setTitle("Contact Deletion");
        alert.setHeaderText("Are you sure you want to delete this contact?");
        alert.setContentText("Once a contact is deleted, you cannot recover it.");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            Contact select = contactTable.getSelectionModel().getSelectedItem();
            list.remove(select);
            ContactProcessor.overwriteSave(list);
        }
    }

    @FXML
    public void onMoreInfoClick() throws IOException {
        //pass Contact object into pop-up
        Contact select = contactTable.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/infoScreen.fxml"));
        Parent root = loader.load();
        InfoScreenController infoScreenController = loader.getController();
        infoScreenController.setValues(select);

        //Show pop-up info screen
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.showAndWait();
    }

    @FXML
    protected void onExitClick(){
        javafx.application.Platform.exit();
    }

    @FXML
    protected void onMinimizeClick(){
        Stage stage = (Stage) minimize.getScene().getWindow();
        stage.setIconified(true);
    }


    @FXML
    public void hboxPressed(MouseEvent mouseEvent){
        x = mouseEvent.getSceneX();
        y = mouseEvent.getSceneY();
    }

    @FXML
    public void hboxDragged(MouseEvent mouseEvent){
        Stage stage = (Stage) contactTable.getScene().getWindow();
        stage.setY(mouseEvent.getScreenY() - y);
        stage.setX(mouseEvent.getScreenX() - x);
    }
}

