package com.kamalan.backend.phonebook.model;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.PhoneNumber;
import com.google.common.base.Preconditions;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

@Entity
@Cache
public class Contact
{
    @Id
    private long id;
    @Index
    private String cName;
    private Email cEmail;
    private PhoneNumber cPhoneNumber;

    // private key, to connect this Entity to Profile Entity
    @Parent
    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    private Key<Profile> profileKey;
    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    private String profileId;

    // default constructor is private
    private Contact()
    {

    }

    public Contact(final long id, final String profileId, final ContactForm contactForm)
    {
        Preconditions.checkNotNull(contactForm.getUserName(), "The name is required");

        this.id = id;
        this.profileKey = Key.create(Profile.class, profileId);
        this.profileId = profileId;

        updateWithContactForm(contactForm);
    }

    /**
     * Updates the Contact with ContactForm.
     * This method is used upon object creation as well as updating existing Contact.
     *
     * @param contactForm contains form data sent from the client.
     */
    public void updateWithContactForm(final ContactForm contactForm)
    {
        this.cName = contactForm.getUserName();
        this.cEmail = contactForm.getUserEmailAddress();
        this.cPhoneNumber = contactForm.getUserPhoneNumber();
    }



    public long getId() {
        return id;
    }

    public String getcName() {
        return cName;
    }

    public Email getcEmail() {
        return cEmail;
    }

    public PhoneNumber getcPhoneNumber() {
        return cPhoneNumber;
    }

    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    public Key<Profile> getProfileKey() {
        return profileKey;
    }

    // Get a String version of the key
    public String getWebSafeKey()
    {
        return Key.create(profileKey, Contact.class, id).getString();
    }

    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    public String getProfileId() {
        return profileId;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", cName='" + cName + '\'' +
                ", cEmail=" + cEmail +
                ", profileId='" + profileId + '\'' +
                ", cPhoneNumber=" + cPhoneNumber +
                '}';
    }
}