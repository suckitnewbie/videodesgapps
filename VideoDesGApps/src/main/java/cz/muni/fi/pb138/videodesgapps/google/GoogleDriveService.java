/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pb138.videodesgapps.google;

import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Children;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.ChildList;
import com.google.api.services.drive.model.ChildReference;
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
 * Represents Google Drive services.
 *
 * @author xnevrela
 */
public class GoogleDriveService {

    public static final String MIME_TYPE_SPREATSHEET_OO = "application/vnd.oasis.opendocument.spreadsheet";
    public static final String MIME_TYPE_FOLDER = "application/vnd.google-apps.folder";
    // Google Drive service object
    private Drive service;
    // Associated Google connection
    private GoogleConnection connection;

    public GoogleDriveService(GoogleConnection connection, Drive service) {
        this.connection = connection;
        this.service = service;
    }

    /**
     * Returns list of all files that authenticated user has access to.
     * 
     * @return list of all files
     * @throws IOException thrown if problem in communication with Google Drive appears
     */
    public List<File> listFiles() throws IOException {
        List<File> result = new ArrayList<File>();
        Files.List request = service.files().list();
        request.setQ("trashed = false");

        do {
            try {
                FileList files = request.execute();

                result.addAll(files.getItems());
                // Paging
                request.setPageToken(files.getNextPageToken());
            } catch (HttpResponseException ex) {
                // Authorization token expired
                if (ex.getStatusCode() == 401 || ex.getStatusCode() == 403) {
                    if (connection.refresh()) {
                        result = listFiles();
                    } else {
                        return null;
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(GoogleConnection.class.getName()).log(Level.SEVERE, null, ex);
                request.setPageToken(null);
            }
        } while (request.getPageToken() != null
                && request.getPageToken().length() > 0);

        return result;
    }

    /**
     * Returns list of all files with given mime type that authenticated user has access to.
     * 
     * @param mimeType mime type of files to be returned
     * @return all files with given mime type
     * @throws IOException thrown if problem in communication with Google Drive appears
     */
    public List<File> listFilesByMimeType(String mimeType) throws IOException {
        List<File> result = new ArrayList<File>();
        Files.List request = service.files().list();
        request.setQ("mimeType = '" + mimeType + "' and trashed = false");

        do {
            try {
                FileList files = request.execute();

                result.addAll(files.getItems());
                // Paging
                request.setPageToken(files.getNextPageToken());
            } catch (HttpResponseException ex) {
                // Authorization token expired
                if (ex.getStatusCode() == 401 || ex.getStatusCode() == 403) {
                    if (connection.refresh()) {
                        result = listFilesByMimeType(mimeType);
                    } else {
                        return null;
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(GoogleConnection.class.getName()).log(Level.SEVERE, null, ex);
                request.setPageToken(null);
            }
        } while (request.getPageToken() != null
                && request.getPageToken().length() > 0);

        return result;
    }

    /**
     * Returns list of all files in given folder that authenticated user has access to.
     * 
     * @param folder folder
     * @return all files in the folder
     * @throws IOException thrown if problem in communication with Google Drive appears
     */
    public List<File> listFiles(File folder) throws IOException {
        return listFilesAndFolders(folder, null);
    }

    /**
     * Returns list of all files in given folder with given mime type that authenticated user has access to.
     * 
     * @param folder folder
     * @param mimeType mime type
     * @return list of all files in the folder with given mime type
     * @throws IOException thrown if problem in communication with Google Drive appears
     */
    public List<File> listFilesAndFolders(File folder, String mimeType) throws IOException {
        List<File> result = new ArrayList<File>();
        Children.List request = service.children().list(folder.getId());

        String q;
        if (mimeType == null) {
            q = "trashed = false";
        } else {
            q = "(mimeType = '" + mimeType + "' or mimeType = '" + MIME_TYPE_FOLDER + "') and trashed = false";
        }
        request.setQ(q);

        do {
            try {
                ChildList childern = request.execute();

                for (ChildReference child : childern.getItems()) {
                    result.add(getFile(child.getId()));
                }
                request.setPageToken(childern.getNextPageToken());
            } catch (HttpResponseException ex) {
                if (ex.getStatusCode() == 401 || ex.getStatusCode() == 403) {
                    if (connection.refresh()) {
                        result = listFilesAndFolders(folder, mimeType);
                    } else {
                        return null;
                    }
                }
            } catch (IOException e) {
                System.out.println("An error occurred: " + e);
                request.setPageToken(null);
            }
        } while (request.getPageToken() != null
                && request.getPageToken().length() > 0);

        return result;
    }

    /**
     * Creates a local copy of file saved at Google Drive.
     * 
     * @param file file at Google Drive to be downloaded
     * @return local copy of the file
     */
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
            } catch (HttpResponseException ex) {
                if (ex.getStatusCode() == 401 || ex.getStatusCode() == 403) {
                    if (connection.refresh()) {
                        result = downloadFile(file);
                    } else {
                        return null;
                    }
                }
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

    /**
     * Saves local file to Google Drive. <em>Creates a new file.</em>
     * 
     * @param file Google metadata of the file
     * @param content actual file to be saved
     * @return updated file reference to file saved at Google Drive
     */
    public File saveFile(File file, java.io.File content) {
        // Content
        FileContent fileContent = new FileContent(file.getMimeType(), content);
        File savedFile = null;

        try {
            savedFile = service.files().insert(file, fileContent).execute();
        } catch (HttpResponseException ex) {
            if (ex.getStatusCode() == 401 || ex.getStatusCode() == 403) {
                if (connection.refresh()) {
                    savedFile = saveFile(file, content);
                } else {
                    return null;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(GoogleConnection.class.getName()).log(Level.SEVERE, null, ex);
            savedFile = null;
        }

        return savedFile;
    }

    /**
     * Saves local file to Google Drive. <em>Creates a new file.</em>
     * 
     * @param name name of the file with extension
     * @param description description of the file
     * @param mimeType mime type of the file
     * @param content actual file to be saved
     * @return updated file reference to file saved at Google Drive
     */
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
        } catch (HttpResponseException ex) {
            if (ex.getStatusCode() == 401 || ex.getStatusCode() == 403) {
                if (connection.refresh()) {
                    savedFile = saveFile(name, description, mimeType, content);
                } else {
                    return null;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(GoogleConnection.class.getName()).log(Level.SEVERE, null, ex);
            savedFile = null;
        }

        return savedFile;
    }

    /**
     * Saves local file to Google Drive. <em>Updates existing file.</em>
     * 
     * @param file Google metadata of the file
     * @param content actual file to be saved
     * @return updated file reference to file saved at Google Drive
     */
    public File updateFile(File file, java.io.File content) {
        File updatedFile = null;

        // Content
        FileContent fileContent = new FileContent(file.getMimeType(), content);

        try {
            updatedFile = service.files().update(file.getId(), file, fileContent).execute();
        } catch (HttpResponseException ex) {
            if (ex.getStatusCode() == 401 || ex.getStatusCode() == 403) {
                if (connection.refresh()) {
                    updatedFile = updateFile(file, content);
                } else {
                    return null;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(GoogleConnection.class.getName()).log(Level.SEVERE, null, ex);
            updatedFile = null;
        }

        return updatedFile;
    }

    /**
     * Returns true if the file reference points to a folder.
     * 
     * @param file Google file metadata
     * @return true if the file reference points to a folder.
     */
    public boolean isFolder(File file) {
        return file.getMimeType() != null && file.getMimeType().equals(MIME_TYPE_FOLDER);
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

    private File getFile(String id) throws IOException {
        return service.files().get(id).execute();
    }
}
