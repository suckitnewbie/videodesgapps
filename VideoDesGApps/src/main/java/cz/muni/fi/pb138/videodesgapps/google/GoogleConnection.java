/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pb138.videodesgapps.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author xnevrela
 */
public class GoogleConnection {

    static {
        CLIENT_ID = "200863197931.apps.googleusercontent.com";
        CLIENT_SECRET = "aKKa4YGHM-YsJkZvzpxpE8wM";
        REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";
        
        connection = new GoogleConnection();
    }
    private static GoogleConnection connection;
    private static String CLIENT_ID;
    private static String CLIENT_SECRET;
    private static String REDIRECT_URI;
    private HttpTransport httpTransport;
    private JsonFactory jsonFactory;
    private GoogleAuthorizationCodeFlow flow;
    private String url;
    private GoogleTokenResponse response;
    private GoogleCredential credential;
    private Drive service;
    private boolean connected;

    public GoogleConnection() {
        httpTransport = new NetHttpTransport();
        jsonFactory = new JacksonFactory();

        flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, CLIENT_ID, CLIENT_SECRET, Arrays.asList(DriveScopes.DRIVE))
                .setAccessType("online")
                .setApprovalPrompt("auto").build();

        url = flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
        
        connected = false;
    }

    public static GoogleConnection getConnection() {
        return connection;
    }

    public String getAuthentizationUrl() {
        return this.url;
    }
    
    public boolean isConnected() {
        return this.connected;
    }

    public boolean connect(String code) {
        boolean result = true;

        try {
            response = flow.newTokenRequest(code).setRedirectUri(REDIRECT_URI).execute();
            credential = new GoogleCredential().setFromTokenResponse(response);
            this.connected = true;
        } catch (Exception ex) {
            Logger.getLogger(GoogleConnection.class.getName()).log(Level.SEVERE, null, ex);
            result = false;
        }

        return result;
    }

    public GoogleDriveService buildService() {
        if (credential == null) {
            return null;
        }
        if (service == null) {
            service = new Drive.Builder(httpTransport, jsonFactory, credential).build();;
        }

        return new GoogleDriveService(service);
    }
    
    public void  close() {
        
    }
}
