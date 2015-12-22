package com.mycompany.myfirstapp;

/**
 * Created by baralon on 18/12/2015.
 */

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class FetchSpreadSheet extends AsyncTask<Context, Void, Long> {
    public SpreadsheetEntry currentSchedule ;
    public static String userName;
    final String SUNDAY = "B";
    final String MONDAY = "C";
    final String TUESDAY = "D";
    final String WEDENSDAY = "E";

    protected Long doInBackground(Context... contexts) {
        String applicationName = "AppName";
        String user = "alonlpc";
        String pass = "rfvtgB!11";
        Context context = contexts[0].getApplicationContext();
        URL SPREADSHEET_FEED_URL;
        try {
            SPREADSHEET_FEED_URL = new URL("https://spreadsheets.google.com/feeds/spreadsheets/private/full");
            InputStream ins = context.getResources().openRawResource(R.raw.key);
            File p12 = createFileFromInputStream(ins);
            HttpTransport httpTransport = new NetHttpTransport();
            JacksonFactory jsonFactory = new JacksonFactory();
            String[] SCOPESArray = {"https://spreadsheets.google.com/feeds", "https://spreadsheets.google.com/feeds/spreadsheets/private/full", "https://docs.google.com/feeds"};
            final List SCOPES = Arrays.asList(SCOPESArray);

            GoogleCredential credential = new GoogleCredential.Builder()
                    .setTransport(httpTransport)
                    .setJsonFactory(jsonFactory)
                    .setServiceAccountId("alon-140@timeme-1164.iam.gserviceaccount.com")
                    .setServiceAccountScopes(SCOPES)
                    .setServiceAccountPrivateKeyFromP12File(p12)
                    .build();

            SpreadsheetService service = new SpreadsheetService("test03");

            service.setOAuth2Credentials(credential);
            SpreadsheetFeed feed = service.getFeed(SPREADSHEET_FEED_URL, SpreadsheetFeed.class);
            List<SpreadsheetEntry> spreadsheets = feed.getEntries();

            if (spreadsheets.size() == 0) {
                Log.i("No spreadsheets", " found.");
            }

            SpreadsheetEntry spreadsheet = null;
            for (int i = 0; i < spreadsheets.size(); i++) {
                if (spreadsheets.get(i).getTitle().getPlainText().startsWith("TimeTable")) {
                    spreadsheet = spreadsheets.get(i);
                    Log.i("Name : ", spreadsheets.get(i).getTitle().getPlainText());
                    currentSchedule = spreadsheet;
                    break;
                }
            }
            if (spreadsheet == null)
                return null;
            userName="alon";
            List<WorksheetEntry> worksheets = currentSchedule.getWorksheets();
            WorksheetEntry userSchedule = null;
            for (WorksheetEntry worksheet : worksheets) {
                //find worksheet for user.
                String title = worksheet.getTitle().getPlainText();

                if (userName.equals(title)) {
                    Log.i("working on user: ", title);
                    userSchedule = worksheet;
                    break;
                }
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_WEEK);
                int f = Calendar.HOUR_OF_DAY;

                URL cellFeedUrl = new URI(userSchedule.getCellFeedUrl().toString()
                        + "?min-row=2&max-row=2&min-col=2&max-col=2").toURL();
                CellFeed cellFeed = service.getFeed(cellFeedUrl, CellFeed.class);
                for (CellEntry cell : cellFeed.getEntries()) {
                    //  get cell value
                   Log.i("cell value: ", cell.getCell().getInputValue());
                }

            }

        } catch (Exception e)
        {
            e.printStackTrace();;
        }


        return 0L;
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