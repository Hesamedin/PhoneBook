package com.kamalan.backend.phonebook.utility;

/**
 * Created by Hesam Kamalan on 5/3/15
 *
 * Just a wrapper for Boolean.
 * We need this wrapped Boolean because endpoints functions must return
 * an object instance, they can't return a Type class such as
 * String or Integer or Boolean
 */
public class WrappedBoolean
{
    private final Boolean result;
    private final String reason;

    public WrappedBoolean(Boolean result) {
        this.result = result;
        this.reason = "";
    }

    public WrappedBoolean(Boolean result, String reason) {
        this.result = result;
        this.reason = reason;
    }

    public Boolean getResult() {
        return result;
    }

    public String getReason() {
        return reason;
    }
}
