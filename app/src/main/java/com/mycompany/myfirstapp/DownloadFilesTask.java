package com.mycompany.myfirstapp;

/**
 * Created by baralon on 18/12/2015.
 */
//import com.google.gdata.client.spreadsheet.*;
//import com.google.gdata.data.spreadsheet.*;
//import com.google.gdata.util.*;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.*;
import java.util.*;
//public class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {
////    protected Long doInBackground(URL... urls) {
////        int count = urls.length;
////        long totalSize = 0;
////        for (int i = 0; i < count; i++) {
////            totalSize += Downloader.downloadFile(urls[i]);
////            publishProgress((int) ((i / (float) count) * 100));
////            // Escape early if cancel() is called
////            if (isCancelled()) break;
////        }
////        return totalSize;
////    }
//
//    }