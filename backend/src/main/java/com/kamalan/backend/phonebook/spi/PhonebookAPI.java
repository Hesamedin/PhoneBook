package com.kamalan.backend.phonebook.spi;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.kamalan.backend.phonebook.Constants;
import com.kamalan.backend.phonebook.model.ContactForm;
import com.kamalan.backend.phonebook.model.Profile;
import com.kamalan.backend.phonebook.model.Contact;
import com.kamalan.backend.phonebook.utility.WrappedString;

import java.util.List;
import java.util.logging.Logger;

import static com.kamalan.backend.phonebook.service.OfyService.factory;
import static com.kamalan.backend.phonebook.service.OfyService.ofy;

/**
 * Created by Hesam Kamalan on 4/26/15
 */
@Api(
        name = "phonebookAPI",
        version = "v1",
        scopes = {Constants.EMAIL_SCOPE},
        clientIds = {Constants.WEB_CLIENT_ID, Constants.ANDROID_CLIENT_ID, Constants.API_EXPLORER_CLIENT_ID},
        audiences = {Constants.ANDROID_AUDIENCE},
        description = "Phonebook API for the GAE Backend application.")
public class PhonebookAPI
{
    private static final String TAG = PhonebookAPI.class.getSimpleName();
    private static final String MSG_AUTH_ERROR = "Authorization required";

    @ApiMethod(name = "createContact", path = "contact", httpMethod = ApiMethod.HttpMethod.POST)
    public Contact createContact(final User user, final ContactForm contactForm) throws UnauthorizedException
    {
        // If the user is not logged in, throw an UnauthorizedException
        if (user == null)
        {
            throw new UnauthorizedException(PhonebookAPI.MSG_AUTH_ERROR);
        }

        // Get user id
        final String userId = getUserId(user);

        // Get the key for the user's profile
        Key<Profile> profileKey = Key.create(Profile.class, userId);

        // Allocate a key for the conference
        final Key<Contact> contactKey = factory().allocateId(profileKey, Contact.class);

        // Get the contact Id from the key
        final long contactId = contactKey.getId();

        // Get the existing Profile entity for the current user if there is one
        // Otherwise create a new Profile entity with default values
        Profile profile = getProfileFromUser(user);

        // Create a new Contact Entity, specifying the user's Profile entity
        // as the parent of the Contact
        Contact contact = new Contact(contactId, userId, contactForm);

        // Save Contact and Profile Entities
        ofy().save().entities(contact, profile).now();

        return contact;
    }

    @ApiMethod(name = "getContactsCreated", path = "contact", httpMethod = ApiMethod.HttpMethod.GET)
    public List<Contact> getContactsCreated(final User user) throws UnauthorizedException
    {
        // If the user is not logged in, throw an UnauthorizedException
        if (user == null)
        {
            throw new UnauthorizedException(PhonebookAPI.MSG_AUTH_ERROR);
        }

        String userId = getUserId(user);
        Key<Profile> userKey = Key.create(Profile.class, userId);

        return ofy().load().type(Contact.class).ancestor(userKey).order("cName").list();
    }

    /**
     * Gets the Profile entity for the current user or creates it if it doesn't exist
     *
     * @param user
     * @return user's Profile
     */
    private static Profile getProfileFromUser(User user)
    {
        // First fetch the user's Profile from the data-store.
        Profile profile = ofy().load().key(Key.create(Profile.class, getUserId(user))).now();
        if (profile == null)
        {
            // Create a new Profile if it doesn't exist.
            profile =  new Profile(user);
        }

        return profile;
    }

    /**
     * This is an ugly workaround for null userId for Android clients.
     *
     * @param user A User object injected by the cloud endpoints.
     * @return the App Engine userId for the user.
     */
    private static String getUserId(User user)
    {
        String userId = user.getUserId();
        if (userId == null)
        {
            Logger.getLogger("userId is null, so trying to obtain it from the data-store.");

            Profile profile = new Profile(user);
            ofy().save().entity(profile).now();

            // Begin new session for not using session cache.
            Objectify objectify = ofy().factory().begin();
            Profile savedUser = objectify.load().key(profile.getKey()).now();
            userId = savedUser.getUser().getUserId();

            Logger.getLogger("Obtained the userId: " + userId);
        }
        return userId;
    }
}