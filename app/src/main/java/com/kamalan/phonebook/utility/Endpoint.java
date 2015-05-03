package com.kamalan.phonebook.utility;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;

/**
 * Created by Hesam Kamalan on 5/3/15
 */
public class Endpoint
{
    /**
     * Application client Id
     */
    public static final String CLIENT_ID = "my-phonebook-123";

    /**
     * Your WEB CLIENT ID from the API Access screen of the Developer Console page.
     */
    public static final String WEB_CLIENT_ID = "415020457117-u69hrqtfmu8bvvnf2a5jsidpn2vmdm97.apps.googleusercontent.com";

    /**
     * The audience is defined by the web client id, not the Android client id.
     */
    public static final String AUDIENCE = "server:client_id:" + WEB_CLIENT_ID;

    /**
     * Class instance of the JSON factory.
     */
    public static final JsonFactory JSON_FACTORY = new AndroidJsonFactory();

    /**
     * Class instance of the HTTP transport.
     */
    public static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();
}
