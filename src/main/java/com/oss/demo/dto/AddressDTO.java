package com.oss.demo.dto;

public class AddressDTO {
    private String street;
    private String city;
    private String zipcode;

    public AddressDTO(String street, String city, String zipcode) {
        this.street = street;
        this.city = city;
        this.zipcode = zipcode;
    }
    
   
    // Default constructor (required by Jackson)
    public AddressDTO() {}

    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getZipcode() { return zipcode; }
}




