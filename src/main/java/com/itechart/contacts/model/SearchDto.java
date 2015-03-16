package com.itechart.contacts.model;

import java.util.Date;

/**
 * Created by Rostislav on 09-Mar-15.)
 */
public class SearchDto {
    private String firstName;
    private String lastName;
    private String secondName;
    private Date dateFrom;
    private Date dateTo;
    private Sex sex;
    private String nationality;
    private RelationshipStatus relationshipStatus;
    private String webSite;
    private String email;
    private String job;
    private Address address;

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

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public RelationshipStatus getRelationshipStatus() {
        return relationshipStatus;
    }

    public void setRelationshipStatus(RelationshipStatus relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String toString() {
        return "firstName: " + firstName +
                "; lastName: " + lastName +
                "; secondName: " + secondName +
                "; dateFrom: " + dateFrom +
                "; dateTo: " + dateTo +
                "; sex: " + sex +
                "; nationality: " + nationality +
                "; relationshipStatus: " + relationshipStatus +
                "; webSite: " + webSite +
                "; email: " + email +
                "; job: " + job +
                "; address: " + address.toString();
    }
}
