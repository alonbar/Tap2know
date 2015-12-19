package com.mycompany.myfirstapp;

/**
 * Created by baralon on 19/12/2015.
 */
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.util.ServiceException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
public class OAuthIntegration{
    public static void test(Context myContext) throws MalformedURLException, GeneralSecurityException, IOException, ServiceException {
        URL SPREADSHEET_FEED_URL;
        SPREADSHEET_FEED_URL = new URL("https://spreadsheets.google.com/feeds/spreadsheets/private/full");

        InputStream ins = myContext.getResources().openRawResource(R.raw.key);
        File p12 = createFileFromInputStream(ins);

//        File p12 = new File("./key.p12");
        String a = p12.getAbsolutePath();
        boolean flag = p12.exists();
        HttpTransport httpTransport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();
        String[] SCOPESArray = {"https://spreadsheets.google.com/feeds", "https://spreadsheets.google.com/feeds/spreadsheets/private/full", "https://docs.google.com/feeds"};
        final List SCOPES = Arrays.asList(SCOPESArray);
        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setServiceAccountId("alonlpc@gmail.com")
                .setServiceAccountScopes(SCOPES)
                .setServiceAccountPrivateKeyFromP12File(p12)
                .build();

        SpreadsheetService service = new SpreadsheetService("Test");
        service.setOAuth2Credentials(credential);
        SpreadsheetFeed feed = service.getFeed(SPREADSHEET_FEED_URL, SpreadsheetFeed.class);
        List<SpreadsheetEntry> spreadsheets = feed.getEntries();
//
//        if (spreadsheets.size() == 0) {
//            System.out.println("No spreadsheets found.");
//        }
//
//        SpreadsheetEntry spreadsheet = null;
//        for (int i = 0; i < spreadsheets.size(); i++) {
//            if (spreadsheets.get(i).getTitle().getPlainText().startsWith("ListOfSandboxes")) {
//                spreadsheet = spreadsheets.get(i);
//                System.out.println("Name of editing spreadsheet: " + spreadsheets.get(i).getTitle().getPlainText());
//                System.out.println("ID of SpreadSheet: " + i);
//            }
//        }

    }

    public static File createFileFromInputStream(InputStream inputStream) {

        String path = "";

        File file = new File(Environment.getExternalStorageDirectory(),
                "KeyHolder/KeyFile/");
        if (!file.exists()) {
            if (!file.mkdirs())
                Log.d("KeyHolder", "Folder not created");
            else
                Log.d("KeyHolder", "Folder created");
        } else
            Log.d("KeyHolder", "Folder present");

        path = file.getAbsolutePath();

        try {
            File f = new File(path+"/MyKey");
            OutputStream outputStream = new FileOutputStream(f);
            byte buffer[] = new byte[1024];
            int length = 0;

            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            return f;
        } catch (IOException e) {
            // Logging exception
            e.printStackTrace();
        }

        return null;
    }

}