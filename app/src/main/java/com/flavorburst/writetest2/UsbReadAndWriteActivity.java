package com.flavorburst.writetest2;

import android.content.Context;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class UsbReadAndWriteActivity extends AppCompatActivity implements View.OnClickListener{

    Context mContext;
    Button USB_READ,USB_WRITE;
    public Runtime mRuntime;
    public Process mProcess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usb_read_and_write);
        mContext = this;
        USB_READ = (Button)this.findViewById(R.id.bt_usd_read);
        USB_WRITE = (Button)this.findViewById(R.id.bt_usb_write);
        USB_READ.setOnClickListener(this);
        USB_WRITE.setOnClickListener(this);
        //当前应用的代码执行目录
        Log.i("TAG", "onCreate: ========="+getPackageCodePath());
        //upgradeRootPermission(getPackageCodePath());
    }

    /* 将文件数据写入sdcard中，保存数据 */
    public void writeDataToUsb(String path) {
        try {
            /* 获取File对象，确定数据文件的信息 */
            //File file  = new File(Environment.getExternalStorageDirectory()+"/f.txt");
            File file = new File(path/*Environment.getExternalStorageDirectory()*/, "鑫中宏.txt");
            Log.i("TAG", "writeDataToUsb: file=========="+file);
            /* 判断USB的外部设置状态是否可以读写 */
            //if (path.equals(Environment.MEDIA_MOUNTED)) {
            Log.i("TAG", "writeDataToUsb: path2=========="+path);
            /* 流的对象 *//*  */
            FileOutputStream fos = new FileOutputStream(file);
            Log.i("TAG", "writeDataToUsb: path3=========="+fos);

            /* 需要写入的数据 */
            String message = "深圳欢迎您！！！";

            /* 将字符串转换成字节数组 */
            byte[] buffer = message.getBytes();

            /* 开始写入数据 */
            fos.write(buffer);

            /* 关闭流的使用 */
            fos.close();
            Toast.makeText(UsbReadAndWriteActivity.this, "success", Toast.LENGTH_LONG).show();
            //}

        } catch (Exception ex) {
            Toast.makeText(UsbReadAndWriteActivity.this, "failed", Toast.LENGTH_LONG).show();
        }
    }


    //读取SD卡中的文件：
    /* 读取数据 */
    public void readDataFromUsb (String path) {
        try {

            /* 创建File对象，确定需要读取文件的信息 */
            File file = new File(path/*Environment.getExternalStorageDirectory()*/, "鑫中宏.txt");
            Log.i("TAG", "readDataFromUsb: ======"+file);
            /* FileInputSteam 输入流的对象， */
            FileInputStream fis = new FileInputStream(file);

            /* 准备一个字节数组用户装即将读取的数据 */
            byte[] buffer = new byte[fis.available()];

            /* 开始进行文件的读取 */
            fis.read(buffer);

            /* 关闭流  */
            fis.close();

            /* 将字节数组转换成字符串， 并转换编码的格式 */
            String res = new String(buffer,"UTF-8");
            // String res = EncodingUtils.getString(buffer, "UTF-8");

            Toast.makeText(UsbReadAndWriteActivity.this, "success" + res,Toast.LENGTH_LONG).show();

        } catch (Exception ex) {
            Toast.makeText(UsbReadAndWriteActivity.this, "failed！", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 通过反射调用获取内置存储和外置sd卡根路径(通用)
     *
     * @param mContext    上下文
     * @param is_removale 是否可移除，false返回内部存储，true返回外置sd卡
     * @return
     */
    private static String getStoragePath(Context mContext, boolean is_removale) {

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
                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                if (is_removale == removable) {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_usd_read:
                String mUsbPath = getStoragePath(mContext,true);
                //Log.i("TAG", "onClick: mUsbPath====="+mUsbPath);
                readDataFromUsb(mUsbPath);
                break;
            case R.id.bt_usb_write:
                String mUsbPath2 = getStoragePath(mContext,true);
                //Log.i("TAG", "onClick: mUsbPath====="+mUsbPath2);
                writeDataToUsb(mUsbPath2);
                break;
        }
    }
}

