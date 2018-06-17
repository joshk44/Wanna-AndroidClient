package com.wanna.models;

/**
 * Created by fede on 25/03/15.
 */
public class User {
    //Atributos

    private String user_name;
    private String token;
    private String email;
    private String name;
    private String age;
    private String gender;
    private String session;

    public User() {

    }

    public User(String user_name, String token, String email, String name, String age, String gender, String session) {
        this.user_name = user_name;
        this.token = token;
        this.email = email;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.session = session;
    }

    public String getUser_Name() {
        return user_name;
    }

    public void setUser_Name(String user_name) {
        this.user_name = user_name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }
}
