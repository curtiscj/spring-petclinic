package org.springframework.samples.petclinic.supplement;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Supplement {

    @JsonProperty("name")
    String name;
    @JsonProperty("price")
    String price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    //default constructor
    public Supplement() {

    }
    //import constructor
    public Supplement(String name, String price) {
        this.name = name;
        this.price = price;
    }


}
