package com.mecaroid.interdate.Models;

import java.io.Serializable;

public class MingleModel implements Serializable {
    String profile,username,age,gender,
            languages,occupation,hobbies,about_user,kids,country,province,
            city,town,usertorelocate,qualifications,preferredAgeMin,preferredAgeMax,genderCode,preGenderCode
            ,preferredGender,preferredRace,preferToRelocate,UserVerified,user_id,race,religion,status,FirebaseToken,preferredReligion,basically,studentAt;



    public MingleModel() {
    }

    public MingleModel(String profile, String username, String age, String gender, String languages, String occupation, String hobbies, String about_user, String kids, String country, String province, String city, String town, String usertorelocate, String qualifications, String preferredAgeMin, String preferredAgeMax, String genderCode, String preGenderCode, String preferredGender, String preferredRace, String preferToRelocate, String userVerified, String user_id, String race, String religion, String status, String firebaseToken, String preferredReligion, String basically, String studentAt) {
        this.profile = profile;
        this.username = username;
        this.age = age;
        this.gender = gender;
        this.languages = languages;
        this.occupation = occupation;
        this.hobbies = hobbies;
        this.about_user = about_user;
        this.kids = kids;
        this.country = country;
        this.province = province;
        this.city = city;
        this.town = town;
        this.usertorelocate = usertorelocate;
        this.qualifications = qualifications;
        this.preferredAgeMin = preferredAgeMin;
        this.preferredAgeMax = preferredAgeMax;
        this.genderCode = genderCode;
        this.preGenderCode = preGenderCode;
        this.preferredGender = preferredGender;
        this.preferredRace = preferredRace;
        this.preferToRelocate = preferToRelocate;
        UserVerified = userVerified;
        this.user_id = user_id;
        this.race = race;
        this.religion = religion;
        this.status = status;
        FirebaseToken = firebaseToken;
        this.preferredReligion = preferredReligion;
        this.basically = basically;
        this.studentAt = studentAt;
    }

    public String getGenderCode() {
        return genderCode;
    }

    public void setGenderCode(String genderCode) {
        this.genderCode = genderCode;
    }

    public String getPreGenderCode() {
        return preGenderCode;
    }

    public void setPreGenderCode(String preGenderCode) {
        this.preGenderCode = preGenderCode;
    }

    public String getPreferredReligion() {
        return preferredReligion;
    }

    public void setPreferredReligion(String preferredReligion) {
        this.preferredReligion = preferredReligion;
    }

    public String getBasically() {
        return basically;
    }

    public void setBasically(String basically) {
        this.basically = basically;
    }

    public String getStudentAt() {
        return studentAt;
    }

    public void setStudentAt(String studentAt) {
        this.studentAt = studentAt;
    }

    public void setPregreligion(String pregreligion) {
        this.preferredReligion = pregreligion;
    }


    public String getFirebaseToken() {
        return FirebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        FirebaseToken = firebaseToken;
    }







    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }



    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public String getAbout_user() {
        return about_user;
    }

    public void setAbout_user(String about_user) {
        this.about_user = about_user;
    }

    public String getKids() {
        return kids;
    }

    public void setKids(String kids) {
        this.kids = kids;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getUsertorelocate() {
        return usertorelocate;
    }

    public void setUsertorelocate(String usertorelocate) {
        this.usertorelocate = usertorelocate;
    }

    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }

    public String getPreferredAgeMin() {
        return preferredAgeMin;
    }

    public void setPreferredAgeMin(String preferredAgeMin) {
        this.preferredAgeMin = preferredAgeMin;
    }

    public String getPreferredAgeMax() {
        return preferredAgeMax;
    }

    public void setPreferredAgeMax(String preferredAgeMax) {
        this.preferredAgeMax = preferredAgeMax;
    }

    public String getPreferredGender() {
        return preferredGender;
    }

    public void setPreferredGender(String preferredGender) {
        this.preferredGender = preferredGender;
    }

    public String getPreferredRace() {
        return preferredRace;
    }

    public void setPreferredRace(String preferredRace) {
        this.preferredRace = preferredRace;
    }

    public String getPreferToRelocate() {
        return preferToRelocate;
    }

    public void setPreferToRelocate(String preferToRelocate) {
        this.preferToRelocate = preferToRelocate;
    }

    public String getUserVerified() {
        return UserVerified;
    }

    public void setUserVerified(String userVerified) {
        UserVerified = userVerified;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
