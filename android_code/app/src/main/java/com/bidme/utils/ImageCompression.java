package com.bidme.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Environment;


import com.bidme.interfaces.Const;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by VARUN on 01/01/19.
 */
public class ImageCompression extends AsyncTask<String, Void, String> {

    private Context context;
    private static final float maxHeight = 1280.0f;
    private static final float maxWidth = 1280.0f;



    public ImageCompression(Context context){
        this.context=context;
    }
    public interface AsyncResponse {
        void processFinish(String imagePath);
    }
    public AsyncResponse delegate = null;

    public void setOnTaskFinishedEvent(AsyncResponse delegate) {
        if (delegate != null) {
            this.delegate = delegate;
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        if(strings.length == 0 || strings[0] == null)
            return null;

        return compressImage(strings[0]);
    }

    protected void onPostExecute(String imagePath){
        // imagePath is path of new compressed image.
        if (delegate != null)
            this.delegate.processFinish(imagePath);
    }


    public String compressImage(String imagePath) {
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(imagePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        float imgRatio = (float) actualWidth / (float) actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFile(imagePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.RGB_565);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        if(bmp!=null)
        {
            bmp.recycle();
        }

        ExifInterface exif;
        try {
            exif = new ExifInterface(imagePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        String filepath = getFilename();
        try {
            out = new FileOutputStream(filepath);

           //write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filepath;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public String getFilename() {

//        File file = new File(Environment.getExternalStorageDirectory()
//                .getPath(), Consts.POOCH_PLAY + File.separator + Consts.DATABASE);
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//        String dbPath = (file.getPath() + File.separator + DATABASE_NAME);
//        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
//                + "/Android/data/"
//                + context.getApplicationContext().getPackageName()
//                + "/Files/Compressed");
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()+ File.separator+ Const.APP_NAME);

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            mediaStorageDir.mkdirs();
        }

        String mImageName="IMG_"+ String.valueOf(System.currentTimeMillis()) +".jpg";
        String uriString = (mediaStorageDir.getAbsolutePath() + "/"+ mImageName);;
        return uriString;

    }

}