package com.kamalan.backend.phonebook.utility;

/**
 * Created by Hesam on 04/26/15
 * <p/>
 * Just a wrapper for Long.
 * We need this wrapped Boolean because endpoints functions must return
 * an object instance, they can't return a Type class such as
 * String or Integer or Boolean
 */
public class WrappedString extends Throwable
{

    private final String result;

    public WrappedString(String result)
    {
        this.result = result;
    }

    public String getResult()
    {
        return result;
    }
}
