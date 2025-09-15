package com.infnet.clientes.models;

public class Customer {
    private int id;
    private String cpf;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    public Customer(int id, String cpf, String firstName, String lastName, String email, String phoneNumber) {
        this.id = id;
        this.cpf = cpf;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public String getCpf() {
        return cpf;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
