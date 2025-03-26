package ru.maslennikov.firstrestapi.dto;

import jakarta.validation.constraints.*;

public class PersonDTO {

    @Size(min = 2, max = 50,message = "Name should be between 2 and 50 characters")
    @NotEmpty
    private String name;

    @Min(value = 0,message = "Age should be greater than 0")
    @NotEmpty
    private int age;

    @Email
    @NotEmpty(message = "Email should not be empty")
    private String email;

    public String getName() {
        return name;
    }

    public void setName( String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail( String email) {
        this.email = email;
    }
}
