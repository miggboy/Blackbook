package blackbook.backend;

import javafx.collections.ObservableList;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.LinkedList;

public class Contact implements Serializable {

    private String name;
    private Integer age;
    private LocalDate DoB;
    private String city;
    private String province;
    private LinkedList<Email> emails;
    private LinkedList<PhoneNumber> phoneNumbers;
    private LinkedList<StreetAddress> streetAddresses;
    private Double latitude;
    private Double longitude;
    private final int ID;

    public Contact(String name) {
        this.name = name;
        DoB = null;
        age = null;
        city = null;
        province = null;
        emails = new LinkedList<>();
        phoneNumbers = new LinkedList<>();
        streetAddresses = new LinkedList<>();
        latitude = null;
        longitude = null;
        
        double randomDouble = Math.random();
        ID = (int) (randomDouble * 1000) + 1;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDoB() {
        return DoB;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }

    public Integer getAge(){
        return age;
    }

    public LinkedList<Email> getEmailList() {
        return emails;
    }

    public LinkedList<PhoneNumber> getPhoneNumbers(){
        return phoneNumbers;
    }

    public LinkedList<StreetAddress> getStreetAddresses() {
        return streetAddresses;
    }
    
    public Double getLatitude() {
    	return latitude;
    }
    
    public Double getLongitude() {
    	return longitude;
    }
    
    public int getID() {
    	return ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDoB(LocalDate DoB) {
        this.DoB = DoB;
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(DoB, currentDate);
        age = period.getYears();
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setProvince(String province) {
        this.province = province;
    }
    public void addEmail(String address, String type) {
        Email email = new Email(address, type);
        emails.add(email);
    }

    public void setEmails(ObservableList<Email> list){
        int size = list.size();
        for(int i = 0; i < size; i++){
            emails.add(list.get(i));
        }
    }

    public void addPhoneNumber(String number, String type){
        PhoneNumber phoneNumber = new PhoneNumber(number, type);
        phoneNumbers.add(phoneNumber);
    }

    public void setPhoneNumbers(ObservableList<PhoneNumber> list){
        int size = list.size();
        for(int i = 0; i < size; i++){
            phoneNumbers.add(list.get(i));
        }
    }

    public void addStreetAddress(String address, String type){
        StreetAddress streetAddress = new StreetAddress(address, type);
        streetAddresses.add(streetAddress);
    }

    public void setStreetAddresses(ObservableList<StreetAddress> list){
        int size = list.size();
        for(int i = 0; i < size; i++){
            streetAddresses.add(list.get(i));
        }
    }
    
    public void setLatitude(Double lat) {
    	latitude = lat;
    }
    
    public void setLongitude(Double lon) {
    	longitude = lon;
    }
    
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        Contact otherContact = (Contact) obj;

        return ID == otherContact.ID;
    }

    public String toString(){
        return name;
    }
}
