package com.company.AdminAPI.views;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;

public class CustomerViewModel {

    private int customerId;
    @NotEmpty(message = "Must provide firstName!")
    private String firstName;
    @NotEmpty(message = "Must provide lastName!")
    private String lastName;
    @NotEmpty(message = "Must provide Street Address!")
    private String street;
    @NotEmpty(message = "Must provide city!")
    private String city;
    @NotEmpty(message = "Must provide zipcode!")
    private String zip;
    @NotEmpty(message = "Must provide email!")
    private String email;
    @NotEmpty(message = "Must provide phone!")
    private String phone;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerViewModel that = (CustomerViewModel) o;
        return getCustomerId() == that.getCustomerId() &&
                Objects.equals(getFirstName(), that.getFirstName()) &&
                Objects.equals(getLastName(), that.getLastName()) &&
                Objects.equals(getStreet(), that.getStreet()) &&
                Objects.equals(getCity(), that.getCity()) &&
                Objects.equals(getZip(), that.getZip()) &&
                Objects.equals(getEmail(), that.getEmail()) &&
                Objects.equals(getPhone(), that.getPhone());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCustomerId(), getFirstName(), getLastName(), getStreet(), getCity(), getZip(), getEmail(), getPhone());
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}