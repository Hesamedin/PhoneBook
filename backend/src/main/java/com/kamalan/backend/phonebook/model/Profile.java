package com.kamalan.backend.phonebook.model;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by Hesam Kamalan on 5/2/15
 */
@Entity
public class Profile
{
    @Id
    private String email;
    private User user;

    private Profile()
    {
    }

    public Profile(User user)
    {
        this.user = user;
        this.email = user.getEmail();
    }

    public User getUser()
    {
        return user;
    }

    public Key<Profile> getKey()
    {
        return Key.create(Profile.class, email);
    }
}
