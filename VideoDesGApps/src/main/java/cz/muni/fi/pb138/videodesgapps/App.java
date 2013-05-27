package cz.muni.fi.pb138.videodesgapps;

import com.google.api.services.drive.model.File;
import cz.muni.fi.pb138.videodesgapps.google.GoogleConnection;
import cz.muni.fi.pb138.videodesgapps.google.GoogleDriveService;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) throws IOException {
        // CONNECTION TO GOOGLE DRIVE
        // Step 1: prepare instance
        GoogleConnection gc = GoogleConnection.getConnection();
        String url = gc.getAuthentizationUrl();

        // Step 2: user authentazation
        System.out.println("Please open the following URL in your browser then type the authorization code:");
        System.out.println("  " + url);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String code = br.readLine();

        // Step 3: Insert generated user code and connect
        boolean connected = gc.connect(code);
        if (connected) {

            // Step 4: Building Google Drive Service instance
            GoogleDriveService gds = gc.buildService();

            // Step 5: Enjoy :)
            // Listing files:
            for (File file : gds.listFilesByMimeType(GoogleDriveService.MIME_TYPE_SPREATSHEET_OO)) {
                System.out.println("FILE: " + file.getTitle() + "." + file.getFileExtension());
                System.out.println("    MimeType: " + file.getMimeType());
            }

            // Saving NEW file
            java.io.File content = java.io.File.createTempFile("Temp", "tmp");
            PrintWriter pw = new PrintWriter(content);
            pw.print("Hello world!\nThis is VideoDescGApps application.");
            pw.close();
            File savedFile = gds.saveFile("Video.txt", "Pokusny soubor VideoDescGApps", "text/plain", content);


            System.out.println("Video.txt file has been created on your Google Drive (id: " + savedFile.getId() + ". Check it.");
            System.out.println("Press enter to continue.");
            br.read();


            // Saving UPDATED file
            pw = new PrintWriter(content);
            pw.print("Hello for the second time!\nThis is again VideoDescGApps application.");
            pw.close();
            gds.updateFile(savedFile, content);
        } else {
            System.out.println("Incorrect code inserted.");
        }
        
        
    }
}