package basic.entity;

import javax.persistence.Embeddable;

@Embeddable
public class Address {

    public String city;

    private String street;

    private String zipcode;

}
