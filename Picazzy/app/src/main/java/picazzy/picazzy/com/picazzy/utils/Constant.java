package picazzy.picazzy.com.picazzy.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

/**
 * Created by Ashish on 19/5/16.
 */
public class Constant {

    public static final String BASE_URL="http://picazzy.com";


    public static String getBase64ImageString(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

       bitmap=decodeSampledBitmapFromResource(bitmap,100,100);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
    public static Bitmap decodeSampledBitmapFromResource(Bitmap bitmap,
                                                         int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return   Bitmap.createScaledBitmap(bitmap, reqWidth, reqHeight, false);
    }
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap  getBitmapFromBase64(String encodedImage) {

        String imageDataBytes = encodedImage.substring(encodedImage.indexOf(",")+1);
        byte[] decodedString = Base64.decode(imageDataBytes, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
    public static void printLog(String msg) {
        boolean isTrue = true;
        if (isTrue) {
            Log.d("TTNDCARPOOL ", msg);
        }
    }
}
