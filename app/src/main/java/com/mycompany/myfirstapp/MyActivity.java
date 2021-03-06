package com.mycompany.myfirstapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;






import android.app.Activity;
import android.hardware.SensorManager;
import android.os.Bundle;

import android.speech.tts.TextToSpeech;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.List;
import java.util.Locale;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;


public class MyActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";

    private EditText userInputToSpeech;
    private TextToSpeech convertToSpeech;
    String fileName = "userName";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Context context = getApplicationContext();
        CharSequence text = "Please enter username";
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        String fileName = "userName";
        File file = new File(context.getFilesDir(), fileName);

        Reader reader;
        if (!file.exists()) {
            toast.show();
        }
        else {
            try {
                StringBuilder textFile = new StringBuilder();
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    textFile.append(line);
                }
                br.close();
                FetchSpreadSheet.userName = textFile.toString();
                Log.i("file content: ", textFile.toString());
                int timeToWaitForScheduleInSechonds = 30;
                Context[] arr = new Context[1];
                arr[0] = getApplicationContext();
                FetchSpreadSheet fetcher = new FetchSpreadSheet();
                long startTime = System.currentTimeMillis();
                fetcher.taskToReturn = "";
                fetcher.execute(arr);
                while (FetchSpreadSheet.taskToReturn.equals("") && (System.currentTimeMillis() - startTime) < timeToWaitForScheduleInSechonds * 1000) {
                    Thread.sleep(2);
                }
                convertToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onInit(int status) {
                        if (status != TextToSpeech.ERROR) {
                            convertToSpeech.setLanguage(Locale.US);
                            int NUMBER_OF_READING_TIME = 1;
                            for (int i = 0; i < NUMBER_OF_READING_TIME; i ++ ) {
                                convertToSpeech.speak(FetchSpreadSheet.taskToReturn, TextToSpeech.QUEUE_FLUSH, null, null);
                            }
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when the user clicks the Send button
     */
    public void sendMessage(View view) {

        Intent intent = new Intent(this, DisplayMessageActivity.class);

        EditText editText = (EditText) findViewById(R.id.edit_message);
        String userName = editText.getText().toString();
        Log.i("the user name is: ", userName);
        Context context = getApplicationContext();
        File file = new File(context.getFilesDir(), fileName);
        FileOutputStream outputStream;
        Writer writer = null;

        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(userName.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast toast = Toast.makeText(context, "New user was entered", Toast.LENGTH_LONG);
        toast.show();
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public void checkUser(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        String fileName = "userName";
        Context context = getApplicationContext();
        File file = new File(context.getFilesDir(), fileName);
        FileOutputStream outputStream;
        if (!file.exists()) {
            intent.putExtra(EXTRA_MESSAGE, "not exist");
        } else {
            intent.putExtra(EXTRA_MESSAGE, "exist");
        }
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "My Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.mycompany.myfirstapp/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "My Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.mycompany.myfirstapp/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}




