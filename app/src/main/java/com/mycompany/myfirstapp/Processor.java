package com.mycompany.myfirstapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;

import java.net.URI;
import java.net.URL;
import java.util.List;

/**
 * Created by baralon on 19/12/2015.
 */
public class Processor  extends AsyncTask<SpreadsheetEntry, Void, String> {
    static String userName = "alon";
    protected String doInBackground (SpreadsheetEntry [] schedules) {
        try {
            SpreadsheetEntry schedule = schedules[0];
            List<WorksheetEntry> worksheets = schedule.getWorksheets();
            for (WorksheetEntry worksheet : worksheets) {
                // Get the worksheet's title, row count, and column count.
                String title = worksheet.getTitle().getPlainText();
                if (userName.equals(title)) {
                    Log.i("working on user: ", title);
                    URL cellFeedUrl = new URI(worksheet.getCellFeedUrl().toString()
                            + "?min-row=2&max-row=2&min-col=2&max-col=2").toURL();
//                    CellFeed cellFeed = service.getFeed(cellFeedUrl, CellFeed.class);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Sf";
    }
}
