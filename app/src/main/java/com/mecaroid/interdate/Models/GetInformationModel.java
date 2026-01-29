package com.mecaroid.interdate.Models;

public class GetInformationModel {

    String personalImageUri,personalNames,personalGender,personalRace,personalReligion,personalLanguages,personalOccupations,personalHobbies,personalAbout,personalKids;

    String educationAt,educationQualifications;

    String locationCountry,locationProvince,locationCity,locationTown,locationRelocate = "Yes";
    String preferenceGender,preferenceRace,preferenceReligion,preferenceRelocate,preferenceBasics;

    int personalAge =0,preferenceAgeFrom = 0,preferenceAgeTo = 0;

    public GetInformationModel() {
    }

    public GetInformationModel(String personalImageUri, String personalNames, String personalGender, String personalRace, String personalReligion, String personalLanguages, String personalOccupations, String personalHobbies, String personalAbout, String personalKids, String educationAt, String educationQualifications, String locationCountry, String locationProvince, String locationCity, String locationTown, String locationRelocate, String preferenceGender, String preferenceRace, String preferenceReligion, String preferenceRelocate, String preferenceBasics, int personalAge, int preferenceAgeFrom, int preferenceAgeTo) {
        this.personalImageUri = personalImageUri;
        this.personalNames = personalNames;
        this.personalGender = personalGender;
        this.personalRace = personalRace;
        this.personalReligion = personalReligion;
        this.personalLanguages = personalLanguages;
        this.personalOccupations = personalOccupations;
        this.personalHobbies = personalHobbies;
        this.personalAbout = personalAbout;
        this.personalKids = personalKids;
        this.educationAt = educationAt;
        this.educationQualifications = educationQualifications;
        this.locationCountry = locationCountry;
        this.locationProvince = locationProvince;
        this.locationCity = locationCity;
        this.locationTown = locationTown;
        this.locationRelocate = locationRelocate;
        this.preferenceGender = preferenceGender;
        this.preferenceRace = preferenceRace;
        this.preferenceReligion = preferenceReligion;
        this.preferenceRelocate = preferenceRelocate;
        this.preferenceBasics = preferenceBasics;
        this.personalAge = personalAge;
        this.preferenceAgeFrom = preferenceAgeFrom;
        this.preferenceAgeTo = preferenceAgeTo;
    }

    public String getPreferenceGender() {
        return preferenceGender;
    }

    public void setPreferenceGender(String preferenceGender) {
        this.preferenceGender = preferenceGender;
    }

    public String getPreferenceRace() {
        return preferenceRace;
    }

    public void setPreferenceRace(String preferenceRace) {
        this.preferenceRace = preferenceRace;
    }

    public String getPreferenceReligion() {
        return preferenceReligion;
    }

    public void setPreferenceReligion(String preferenceReligion) {
        this.preferenceReligion = preferenceReligion;
    }

    public String getPreferenceRelocate() {
        return preferenceRelocate;
    }

    public void setPreferenceRelocate(String preferenceRelocate) {
        this.preferenceRelocate = preferenceRelocate;
    }

    public String getPreferenceBasics() {
        return preferenceBasics;
    }

    public void setPreferenceBasics(String preferenceBasics) {
        this.preferenceBasics = preferenceBasics;
    }

    public int getPreferenceAgeFrom() {
        return preferenceAgeFrom;
    }

    public void setPreferenceAgeFrom(int preferenceAgeFrom) {
        this.preferenceAgeFrom = preferenceAgeFrom;
    }

    public int getPreferenceAgeTo() {
        return preferenceAgeTo;
    }

    public void setPreferenceAgeTo(int preferenceAgeTo) {
        this.preferenceAgeTo = preferenceAgeTo;
    }

    public String getLocationRelocate() {
        return locationRelocate;
    }

    public void setLocationRelocate(String locationRelocate) {
        this.locationRelocate = locationRelocate;
    }

    public String getPersonalReligion() {
        return personalReligion;
    }

    public void setPersonalReligion(String personalReligion) {
        this.personalReligion = personalReligion;
    }

    public String getPersonalImageUri() {
        return personalImageUri;
    }

    public void setPersonalImageUri(String personalImageUri) {
        this.personalImageUri = personalImageUri;
    }

    public String getPersonalNames() {
        return personalNames;
    }

    public void setPersonalNames(String personalNames) {
        this.personalNames = personalNames;
    }

    public String getPersonalGender() {
        return personalGender;
    }

    public void setPersonalGender(String personalGender) {
        this.personalGender = personalGender;
    }

    public String getPersonalRace() {
        return personalRace;
    }

    public void setPersonalRace(String personalRace) {
        this.personalRace = personalRace;
    }

    public String getPersonalLanguages() {
        return personalLanguages;
    }

    public void setPersonalLanguages(String personalLanguages) {
        this.personalLanguages = personalLanguages;
    }

    public String getPersonalOccupations() {
        return personalOccupations;
    }

    public void setPersonalOccupations(String personalOccupations) {
        this.personalOccupations = personalOccupations;
    }

    public String getPersonalHobbies() {
        return personalHobbies;
    }

    public void setPersonalHobbies(String personalHobbies) {
        this.personalHobbies = personalHobbies;
    }

    public String getPersonalAbout() {
        return personalAbout;
    }

    public void setPersonalAbout(String personalAbout) {
        this.personalAbout = personalAbout;
    }

    public String getPersonalKids() {
        return personalKids;
    }

    public void setPersonalKids(String personalKids) {
        this.personalKids = personalKids;
    }

    public String getEducationAt() {
        return educationAt;
    }

    public void setEducationAt(String educationAt) {
        this.educationAt = educationAt;
    }

    public String getEducationQualifications() {
        return educationQualifications;
    }

    public void setEducationQualifications(String educationQualifications) {
        this.educationQualifications = educationQualifications;
    }

    public String getLocationCountry() {
        return locationCountry;
    }

    public void setLocationCountry(String locationCountry) {
        this.locationCountry = locationCountry;
    }

    public String getLocationProvince() {
        return locationProvince;
    }

    public void setLocationProvince(String locationProvince) {
        this.locationProvince = locationProvince;
    }

    public String getLocationCity() {
        return locationCity;
    }

    public void setLocationCity(String locationCity) {
        this.locationCity = locationCity;
    }

    public String getLocationTown() {
        return locationTown;
    }

    public void setLocationTown(String locationTown) {
        this.locationTown = locationTown;
    }

    public int getPersonalAge() {
        return personalAge;
    }

    public void setPersonalAge(int personalAge) {
        this.personalAge = personalAge;
    }
}
