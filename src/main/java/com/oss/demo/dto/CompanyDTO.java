package com.oss.demo.dto;

public class CompanyDTO {
    private String name;
    private String catchPhrase;
    private String bs;
    

    public CompanyDTO(String name, String catchPhrase, String bs) {
        this.name = name;
        this.catchPhrase = catchPhrase;
        this.bs = bs;
    }
    
    
    // Default constructor (required by Jackson)
    public CompanyDTO() {}

    public String getName() { return name; }
    public String getCatchPhrase() { return catchPhrase; }
    public String getBs() { return bs; }
}


