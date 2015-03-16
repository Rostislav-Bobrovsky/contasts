package com.itechart.contacts.model;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * Created by Rostislav on 18-Feb-15.)
 */
public class People {
    private int id;
    private String firstName;
    private String lastName;
    private String secondName;
    private Date birthday;
    private Sex sex;
    private String nationality;
    private RelationshipStatus relationshipStatus;
    private String webSite;
    private String email;
    private String job;
    private Address address;
    private List<Phone> phones;
    private List<Attachment> attachments;
    private InputStream photo;

    public People() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
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

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public InputStream getPhoto() {
        return photo;
    }

    public void setPhoto(InputStream photo) {
        this.photo = photo;
    }

    public String toString() {
        return "id: " + id +
                "; firstName: " + firstName +
                "; lastName: " + lastName +
                "; secondName: " + secondName +
                "; birthday: " + birthday +
                "; sex: " + sex +
                "; nationality: " + nationality +
                "; relationshipStatus: " + relationshipStatus +
                "; webSite: " + webSite +
                "; email: " + email +
                "; job: " + job +
                "; address: " + address.toString() +
                "; phones: " + phones.toString();
    }
}
