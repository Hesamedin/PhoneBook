package com.kamalan.backend.phonebook.model;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class MyUser
{
    @Id
    private String email;
    private User user;

    private MyUser()
    {
    }

    public MyUser(User user)
    {
        this.user = user;
        this.email = user.getEmail();
    }

    public User getUser()
    {
        return user;
    }

    public Key<MyUser> getKey()
    {
        return Key.create(MyUser.class, email);
    }

}