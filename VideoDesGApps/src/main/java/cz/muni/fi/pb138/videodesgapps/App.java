package cz.muni.fi.pb138.videodesgapps;

import com.google.api.services.drive.model.File;
import cz.muni.fi.pb138.videodesgapps.google.GoogleConnection;
import cz.muni.fi.pb138.videodesgapps.google.GoogleDriveService;
import cz.muni.fi.pb138.videodesgapps.gui.MainView;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) throws IOException {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InstantiationException ex) {
                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                JFrame mainView = new MainView();

                //Display the window.
                mainView.pack();
                mainView.setVisible(true);
            }
        });

/*                
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
         System.out.println("FILE: " + file.getTitle());
         System.out.println("    Id: " + file.getId());
         }

         for (File file : gds.listFiles()) {
             if (file.getMimeType().equals(GoogleDriveService.MIME_TYPE_FOLDER)) {
                 System.out.println("FOLDER: " + file.getTitle());
             }
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
*/        
         
    }
}