package com.kamalan.backend.phonebook.utility;

/**
 * Created by Hesam on 04/26/15
 * <p/>
 * Just a wrapper for Long.
 * We need this wrapped Boolean because endpoints functions must return
 * an object instance, they can't return a Type class such as
 * String or Integer or Boolean
 */
public class WrappedLong extends Throwable
{

    private final long result;

    public WrappedLong(long result)
    {
        this.result = result;
    }

    public long getResult()
    {
        return result;
    }
}
