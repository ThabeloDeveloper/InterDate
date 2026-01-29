package com.mecaroid.interdate.Models;

import java.io.Serializable;

public class model implements Serializable {
    String username,gender,age,
            about,looking_for,profile,qualification,
            qualification_1,qualification_2,country_code,country
            ,location,user_id,willing_to_relocate,child,religion,hobbies,race,
    /////////Messaging///////
    sender_id,receiver_id,image,message,status,date;

    public model(String username, String gender, String age, String about, String looking_for, String profile, String qualification, String qualification_1, String qualification_2, String country_code, String country, String location, String user_id, String willing_to_relocate, String child, String religion, String hobbies, String race, String sender_id, String receiver_id, String image, String message, String status, String date) {
        this.username = username;
        this.gender = gender;
        this.age = age;
        this.about = about;
        this.looking_for = looking_for;
        this.profile = profile;
        this.qualification = qualification;
        this.qualification_1 = qualification_1;
        this.qualification_2 = qualification_2;
        this.country_code = country_code;
        this.country = country;
        this.location = location;
        this.user_id = user_id;
        this.willing_to_relocate = willing_to_relocate;
        this.child = child;
        this.religion = religion;
        this.hobbies = hobbies;
        this.race = race;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.image = image;
        this.message = message;
        this.status = status;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getLooking_for() {
        return looking_for;
    }

    public void setLooking_for(String looking_for) {
        this.looking_for = looking_for;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getQualification_1() {
        return qualification_1;
    }

    public void setQualification_1(String qualification_1) {
        this.qualification_1 = qualification_1;
    }

    public String getQualification_2() {
        return qualification_2;
    }

    public void setQualification_2(String qualification_2) {
        this.qualification_2 = qualification_2;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getWilling_to_relocate() {
        return willing_to_relocate;
    }

    public void setWilling_to_relocate(String willing_to_relocate) {
        this.willing_to_relocate = willing_to_relocate;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
