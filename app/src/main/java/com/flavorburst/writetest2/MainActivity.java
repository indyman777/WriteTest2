package com.flavorburst.writetest2;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.EnvironmentCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

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

            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission_group.STORAGE};
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

        // for USB testing
        String usbPath = getUSBStoragePath(this, true);

        if (usbPath != null) {
            log("external USB storage available : " + usbPath + File.separator +  filename);
            File file = new File(usbPath + File.separator + filename);

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


        // for microSD testing
//        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
//            log("external storage available");
//            File file = getExternalStorageDir(filename);
//
//            try {
//                // Used for recording only one entry
//                if (file.exists()) file.delete();
//                FileOutputStream outputStream = new FileOutputStream(file);
//                outputStream.write(compiledError.getBytes());
//                outputStream.close();
//
//                log("Wrote " + compiledError + " to " + filename);
//            } catch (IOException e) {
//                log("Unable to write to LogFile");
//                e.printStackTrace();
//            } catch (Exception e) {
//
//                e.printStackTrace();
//            }
//        }
    }

//    public File getExternalStorageDir(String errorLog) {
//
//        // Get the directory for the user's external errorLog directory.
//        String filePath = getExternalStorageDirectories()[0];
////        log("is removable? " + String.valueOf(Environment.isExternalStorageRemovable(new File(filePath))));
//        File file = new File(filePath + "/" + errorLog);
//
//        if (!file.exists()) try {
//            file.createNewFile();
//        } catch (IOException e1) {
//            log("Unable to create LogFile");
//            e1.printStackTrace();
//        }
//
//        return file;
//    }

    protected void log(String s) {

        Log.i(TAG, s);
    }

    /* returns external storage paths (directory of external memory card) as array of Strings */
//    public String[] getExternalStorageDirectories() {
//
//        List<String> results = new ArrayList<>();
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { //Method 1 for KitKat & above
//            File[] externalDirs = getExternalFilesDirs(null);
//
//            for (File file : externalDirs) {
//                String path = file.getPath().split("/Android")[0];
//
//                boolean addPath = false;
//
//                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    addPath = Environment.isExternalStorageRemovable(file);
//                }
//                else{
//                    addPath = Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(file));
//                }
//
//                if(addPath){
//                    results.add(path);
//                }
//            }
//        }
//
//        if(results.isEmpty()) { //Method 2 for all versions
//
//            // In case this is being run on older touch panel.
//            results.add("/mnt/extsd");
//
////            // better variation of: http://stackoverflow.com/a/40123073/5002496
////            String output = "";
////            try {
////                final Process process = new ProcessBuilder().command("mount | grep /dev/block/vold")
////                        .redirectErrorStream(true).start();
////                process.waitFor();
////                final InputStream is = process.getInputStream();
////                final byte[] buffer = new byte[1024];
////                while (is.read(buffer) != -1) {
////                    output = output + new String(buffer);
////                }
////                is.close();
////            } catch (final Exception e) {
////                e.printStackTrace();
////            }
////            if(!output.trim().isEmpty()) {
////                String devicePoints[] = output.split("\n");
////                for(String voldPoint: devicePoints) {
////                    results.add(voldPoint.split(" ")[2]);
////                }
////            }
//        }
//
//        //Below few lines is to remove paths which may not be external memory card, like OTG (feel free to comment them out)
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            for (int i = 0; i < results.size(); i++) {
//                if (!results.get(i).toLowerCase().matches(".*[0-9a-f]{4}[-][0-9a-f]{4}")) {
//                    log(results.get(i) + " might not be extSDcard");
//                    results.remove(i--);
//                }
//            }
//        } else {
//            for (int i = 0; i < results.size(); i++) {
//                if (!results.get(i).toLowerCase().contains("ext") && !results.get(i).toLowerCase().contains("sdcard")) {
//                    log(results.get(i)+" might not be extSDcard");
//                    results.remove(i--);
//                }
//            }
//        }
//
//        String[] storageDirectories = new String[results.size()];
//        for(int i=0; i<results.size(); ++i) storageDirectories[i] = results.get(i);
//
//        for(String path : storageDirectories) log("storage directory:" + path);
//
//        return storageDirectories;
//    }

    protected static String getUSBStoragePath(Context mContext, boolean is_removable) {

        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2) return "mnt/udisk";

        else {

            StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
            Class<?> storageVolumeClazz = null;
            try {
                storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
                Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
                Method getPath = storageVolumeClazz.getMethod("getPath");
                Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
                Object result = getVolumeList.invoke(mStorageManager);
                final int length = Array.getLength(result);
                for (int i = 0; i < length; i++) {
                    Object storageVolumeElement = Array.get(result, i);
                    String path = (String) getPath.invoke(storageVolumeElement);
                    Log.i("TAG", "getStoragePath: =====" + path + ", removable? " + String.valueOf(isRemovable.invoke(storageVolumeElement)));//path获取路径自行判断
                    boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                    if (is_removable == removable) {
                        Log.i("TAG", "Returning " + path);
                        return path;
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}