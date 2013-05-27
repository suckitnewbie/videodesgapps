/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pb138.videodesgapps.google;

import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author xnevrela
 */
public class GoogleDriveService {

    public static String MIME_TYPE_SPREATSHEET_OO = "application/vnd.oasis.opendocument.spreadsheet";
    Drive service;

    public GoogleDriveService(Drive service) {
        this.service = service;
    }

    public List<File> listFiles() throws IOException {
        List<File> result = new ArrayList<File>();
        Files.List request = service.files().list();
        request.setQ("trashed = false");

        do {
            try {
                FileList files = request.execute();

                result.addAll(files.getItems());
                request.setPageToken(files.getNextPageToken());
            } catch (IOException ex) {
                Logger.getLogger(GoogleConnection.class.getName()).log(Level.SEVERE, null, ex);
                request.setPageToken(null);
            }
        } while (request.getPageToken() != null
                && request.getPageToken().length() > 0);

        return result;
    }

    public List<File> listFilesByMimeType(String mimeType) throws IOException {
        List<File> result = new ArrayList<File>();
        Files.List request = service.files().list();
        request.setQ("mimeType = '" + mimeType + "' and trashed = false");

        do {
            try {
                FileList files = request.execute();

                result.addAll(files.getItems());
                request.setPageToken(files.getNextPageToken());
            } catch (IOException ex) {
                Logger.getLogger(GoogleConnection.class.getName()).log(Level.SEVERE, null, ex);
                request.setPageToken(null);
            }
        } while (request.getPageToken() != null
                && request.getPageToken().length() > 0);

        return result;
    }

    public java.io.File downloadFile(File file) {
        java.io.File result = null;
        InputStream is = null;

        String downloadUrl = file.getDownloadUrl();

        if (downloadUrl != null && downloadUrl.length() > 0) {
            try {
                HttpResponse resp =
                        service.getRequestFactory().buildGetRequest(new GenericUrl(downloadUrl))
                        .execute();
                is = resp.getContent();
                result = this.createTemporaryFile("GDriveFile", "tmp", is);
            } catch (IOException ex) {
                // An error occurred.
                Logger.getLogger(GoogleConnection.class.getName()).log(Level.SEVERE, null, ex);
                result = null;
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException ex) {
                        Logger.getLogger(GoogleDriveService.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        return result;
    }

    public File saveFile(String name, String description, String mimeType, java.io.File content) {
        // Metadata
        File file = new File();
        file.setTitle(name);
        file.setDescription(description);
        file.setMimeType(mimeType);

        // Content
        FileContent fileContent = new FileContent(mimeType, content);
        File savedFile = null;
                
        try {
            savedFile = service.files().insert(file, fileContent).execute();
        } catch (IOException ex) {
            Logger.getLogger(GoogleConnection.class.getName()).log(Level.SEVERE, null, ex);
            savedFile = null;
        }
        
        return savedFile;
    }
    
    public File updateFile(File file, java.io.File content) {
        File updatedFile = null;
        
        // Content
        FileContent fileContent = new FileContent(file.getMimeType(), content);
        
        try {
            updatedFile = service.files().update(file.getId(), file, fileContent).execute();
        } catch(IOException ex) {
            Logger.getLogger(GoogleConnection.class.getName()).log(Level.SEVERE, null, ex);
            updatedFile = null;
        }
        
        return updatedFile;
    }

    private java.io.File createTemporaryFile(String name, String extension, InputStream is) throws IOException {
        java.io.File result = java.io.File.createTempFile(name, extension);
        OutputStream os;

        int read = 0;
        byte[] bytes = new byte[1024];

        try {
            os = new FileOutputStream(result);

            while ((read = is.read(bytes)) != -1) {
                os.write(bytes, 0, read);
            }
        } catch (IOException ex) {
            Logger.getLogger(GoogleConnection.class.getName()).log(Level.SEVERE, null, ex);
            result = null;
        }

        return result;
    }
}
