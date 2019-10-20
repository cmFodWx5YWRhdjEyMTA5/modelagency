package com.talentnew.activities.common;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.talentnew.R;
import com.talentnew.utilities.Utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseImageActivity extends NetworkBaseActivity {

    private final int DEST_WIDTH = 400;

    private int PICK_IMAGE_REQUEST=1;
    private int REQUEST_CAMERA = 0;
    private int REQUEST_TAKE_GALLERY_VIDEO = 2;
    private Uri mHighQualityImageUri;
    protected String imagePath="",videoPath;
    protected String fileName="";
    private Uri filePath;
    private Bitmap bitmap;
    private String userChoosenTask,flag;
    private boolean successfullyUpdated,isNewRider;
    protected String imageBase64;
    protected AlertDialog alertDialog;
    protected RequestOptions requestOptions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        // requestOptions.override(Utility.dpToPx(150, context), Utility.dpToPx(150, context));
        requestOptions.centerCrop();
        requestOptions.skipMemoryCache(false);

    }

    protected void selectImage(){
        int view=R.layout.select_image_dialog_layout;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setCancelable(false)
                .setView(view);

        // create alert dialog
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();


        // final TextView textHeader=(TextView) alertDialog.findViewById(R.id.text_header);
        final ImageView imageCancel=(ImageView) alertDialog.findViewById(R.id.image_close);
        final Button btnGallery=(Button) alertDialog.findViewById(R.id.btn_gallery);
        final Button btnCamera=(Button) alertDialog.findViewById(R.id.btn_camera);

        Utility.setColorFilter(btnGallery.getBackground(),getResources().getColor(R.color.colorAccent));
        Utility.setColorFilter(btnCamera.getBackground(),getResources().getColor(R.color.colorAccentLight));

        imageCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userChoosenTask = "Camera";
                boolean result = Utility.verifyCameraPermissions(BaseImageActivity.this);
                if (result)
                    cameraIntent();
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userChoosenTask = "Gallery";
                boolean result = Utility.verifyStorageOnlyPermissions(BaseImageActivity.this);
                if (result)
                    galleryIntent();
            }
        });

        alertDialog.show();
    }

    protected void selectVideo(){
        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        startActivityForResult(Intent.createChooser(intent,"Select Video"),REQUEST_TAKE_GALLERY_VIDEO);
    }

    private void cameraIntent(){
        mHighQualityImageUri = FileProvider.getUriForFile(this,
                getApplicationContext().getPackageName() + ".provider", getFile());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mHighQualityImageUri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void onSelectFromGalleryResult(Intent data){
        filePath = data.getData();
        // imagePath = filePath.getPath();
        // textViewImageStatus.setVisibility(View.VISIBLE);
        //  textViewImageStatus.setText("Pan card image added.");
        //  imagePath=getPath(filePath);
        try {
            //   bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            bitmap=getBitmapFromUri(filePath);
            saveBitmap(bitmap);
            //convertToBase64(new File(imagePath));
            imageAdded();
            //galleryAddPic();
            //  imageByte=AppHelper.getFileDataFromDrawable(this,bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onCaptureImageResult(){
        //convertToBase64(new File(imagePath));
        try {
            File file = new File(imagePath);

            Bitmap b = BitmapFactory.decodeFile(file.getAbsolutePath());
            Bitmap compressedBitmap = compressImage(b);
            if(file.exists()){
                file.delete();
            }
            saveBitmap(compressedBitmap);
            imageAdded();

        }catch (Exception e){
            e.printStackTrace();
        }


        //galleryAddPic();
    }

    public void onCaptureVideoResult(Intent data) {
        Uri selectedVideoUri = data.getData();

        // OI FILE Manager
        String filemanagerstring = selectedVideoUri.toString();
        videoPath = filemanagerstring;

        // MEDIA GALLERY
        String selectedVideoPath = getVideoPath(selectedVideoUri);
        if (selectedVideoPath != null) {
            videoPath = selectedVideoPath;
            Log.i(TAG,"video captured through path "+videoPath);
        }
        videoAdded();
    }

    public void saveBitmap(Bitmap bitmap){

        Bitmap b = compressImage(bitmap);

        FileOutputStream out = null;
        File filename = getFile();

        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            b.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public File getFile() {
        String date=new SimpleDateFormat("yyyy_MM_dd").format(new Date());
        String time=new SimpleDateFormat("HH:mm:ss").format(new Date());
        date = date.replaceAll("_","");
        time = time.replaceAll(":","");
        String root="";
        if(isExternalStorageAvailable()){
            root = Environment.getExternalStoragePublicDirectory("").toString();
        }
        File myDir = new File(root+"/ModelAgency/ModelAgency photos");
        myDir.mkdirs();
        fileName = date+time+".jpg";
        imagePath=root+"/ModelAgency/ModelAgency photos/"+fileName;
        File file = new File (myDir, fileName);
        return file;

    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    private String getVideoPath(Uri uri) {
        String[] projection = { MediaStore.Video.Media.DATA };
        getContentResolver();
        Cursor cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    public String convertToBase64(File file){
        String base64 = "";
        try {
            FileInputStream inputStream = new FileInputStream(file);
            byte[] bytes;
            byte[] buffer = new byte[8192];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            try {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            bytes = output.toByteArray();
            base64 = Base64.encodeToString(bytes, Base64.NO_WRAP);
            // imageBase64 = imageBase64.replaceAll("\n","");
            Log.i("PanCard","Image converted into base64");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            base64 = "";
            Log.i("PanCard","Image conversion not successful");
        }

        return base64;
    }

    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.i(TAG,"Request Code "+requestCode+" "+userChoosenTask);
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Camera"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Gallery"))
                        galleryIntent();

                } else {
                    //code for deny
                }
                break;
            case Utility.MY_PERMISSIONS_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Camera"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Gallery"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(alertDialog != null)
            alertDialog.dismiss();

        //  Log.i(TAG,"requestCode code "+requestCode+" "+resultCode+" "+RESULT_OK);

        if (requestCode == PICK_IMAGE_REQUEST){
            if(data != null){
                onSelectFromGalleryResult(data);
            }
        }else if (requestCode == REQUEST_CAMERA){
            Log.i(TAG,"data "+data);
            Log.i(TAG,"image captured "+imagePath);
            onCaptureImageResult();
        }else if (requestCode == REQUEST_TAKE_GALLERY_VIDEO){
            if(data != null){
                Log.i(TAG,"data "+data);
                onCaptureVideoResult(data);
            }

        }
    }

    protected void imageAdded(){

    }

    protected void videoAdded(){

    }


    private Bitmap compressImage(Bitmap bitmap){
        int origWidth = bitmap.getWidth();
        int origHeight = bitmap.getHeight();
        Log.i(TAG,"width "+origWidth);
        Bitmap returnBitmap = null;
        if(origWidth > DEST_WIDTH){
            // picture is wider than we want it, we calculate its target height
            int destHeight = origHeight/( origWidth / DEST_WIDTH ) ;
            // we create an scaled bitmap so it reduces the image, not just trim it
            returnBitmap = Bitmap.createScaledBitmap(bitmap, DEST_WIDTH, destHeight, false);
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            // compress to the format you want, JPEG, PNG...
            // 70 is the 0-100 quality percentage
            returnBitmap.compress(Bitmap.CompressFormat.JPEG,70 , outStream);
            Log.i(TAG,"image compressed");

        }else{
            returnBitmap = bitmap;
        }

        return returnBitmap;
    }

}
