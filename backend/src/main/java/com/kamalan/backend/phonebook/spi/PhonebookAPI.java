package com.kamalan.backend.phonebook.spi;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.Objectify;
import com.kamalan.backend.phonebook.Constants;
import com.kamalan.backend.phonebook.model.MyUser;
import com.kamalan.backend.phonebook.utility.WrappedString;

import java.util.logging.Logger;

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

    @ApiMethod(name = "createPhonebook", path = "phonebook", httpMethod = ApiMethod.HttpMethod.POST)
    public WrappedString createPhonebook(final User user) throws UnauthorizedException
    {
        // If the user is not logged in, throw an UnauthorizedException
        if (user == null)
        {
            throw new UnauthorizedException("Authorization required");
        }

        // Return userId
        String userId = getUserId(user);
        return new WrappedString(userId);
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
            Logger.getLogger("userId is null, so trying to obtain it from the datastore.");

            MyUser myUser = new MyUser(user);
            ofy().save().entity(myUser).now();

            // Begin new session for not using session cache.
            Objectify objectify = ofy().factory().begin();
            MyUser savedUser = objectify.load().key(myUser.getKey()).now();
            userId = savedUser.getUser().getUserId();

            Logger.getLogger("Obtained the userId: " + userId);
        }

        return userId;
    }
}