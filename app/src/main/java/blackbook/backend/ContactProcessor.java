//this class is a tool-kit for processing Contact information and providing
//data persistence to the Blackbook application.
package blackbook.backend;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.*;

public class ContactProcessor{

    private static FileInputStream fis;
    private static ObjectInputStream ois;
    private static FileOutputStream fos;
    private static ObjectOutputStream oos;


    //retrieves serialized Contact objects from the contacts.bin file
    public static ObservableList<Contact> getSavedContacts() {
        ObservableList<Contact> list = FXCollections.observableArrayList();

        try{
            fis = new FileInputStream("contacts.bin");
            ois = new ObjectInputStream(fis);
            try {
                while (true) {
                Contact contact = (Contact) ois.readObject();
                list.add(contact);
                }
            }
            catch(ClassNotFoundException e){
                //Done printing
            }
            fis.close();
            ois.close();
        }
        catch(IOException e){
            //contacts.bin is corrupt or does not exist,
            //continue program with empty list
        }
        return list;
    }


    //Overwrites the contacts.bin file with a truncated or extended version of the list
    public static boolean overwriteSave(ObservableList<Contact> list){
        try{
            fos = new FileOutputStream("contacts.bin");
            oos = new ObjectOutputStream(fos);
            Contact contact;
            for(int i = 0; i < list.size(); i++){
                contact = list.get(i);
                oos.writeObject(contact);
            }
            fos.close();
            oos.close();
        }
        catch(IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}