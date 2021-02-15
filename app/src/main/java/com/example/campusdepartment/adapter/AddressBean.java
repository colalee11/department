package com.example.campusdepartment.adapter;

public class AddressBean {
    private String name;
    private String phonenumber;
    private String city;
    private String address;

    public AddressBean(String name, String phonenumber, String city, String address) {
        this.name = name;
        this.phonenumber = phonenumber;
        this.city = city;
        this.address = address;
    }

    @Override
    public String toString() {
        return "AddressBean{" +
                "name='" + name + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public AddressBean() {
    }
}
