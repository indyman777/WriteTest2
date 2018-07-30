package com.flavorburst.writetest2;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "Write Test";
    private Button btWriteTimestamp;
    protected final int REQUEST_PERMISSIONS_CODE = 225;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btWriteTimestamp = findViewById(R.id.btRecordTimestamp);
        btWriteTimestamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeToErrorLog();
            }
        });

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            Log.i("DBINIT", "checking permission");

            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if(!hasPermissions(this, permissions)) {

                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS_CODE);
            }
        }
    }

    private boolean hasPermissions(Context context, String[] permissions) {

        boolean permissionsGranted = true;
        for(String permission : permissions) {

            if (ContextCompat.checkSelfPermission(getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED) permissionsGranted = false;

        }
        return permissionsGranted;
    }
    protected void writeToErrorLog() {

        String filename = "LogFile.txt";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
        Calendar calendar = new GregorianCalendar();
        String currentDateandTime = sdf.format(calendar.getTime());

        String compiledError = currentDateandTime + "\n\n";

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            log("external storage available");
            File file = getDownloadStorageDir(filename);

            try {
                // Used for recording only one entry
                if (file.exists()) file.delete();
                FileOutputStream outputStream = new FileOutputStream(file);
                outputStream.write(compiledError.getBytes());
                outputStream.close();

                log("Wrote " + compiledError + " to " + filename);
            } catch (IOException e) {
                log("Unable to write to LogFile");
                e.printStackTrace();
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    public File getDownloadStorageDir(String errorLog) {

        // Get the directory for the user's public errorLog directory.
//        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), errorLog);
        log("is removable? " + String.valueOf(Environment.isExternalStorageRemovable(new File("/storage/42E5-1DEE"))));
//        File file = new File("/storage/42E5-1DEE/logfile.txt");

        java.io.File file = new java.io.File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + "/Filename.xml");

        if (!file.exists()) try {
            file.createNewFile();
        } catch (IOException e1) {
            log("Unable to create LogFile");
            e1.printStackTrace();
        }

        return file;
    }

    protected void log(String s) {

        Log.i(TAG, s);
    }
}