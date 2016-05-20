package picazzy.picazzy.com.picazzy.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.os.Environment;
import android.view.Display;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NavigableMap;
import java.util.TreeMap;

public class BitmapUtility {
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();
    private static final NavigableMap<Long, String> formatterSuffixes = new TreeMap<>();
    public static String TAG = "";

    private Context context;

    public BitmapUtility(Context context) {
        this.context = context;
    }

    // Handles image orientation w.r.t the camera angle
    public static Bitmap getBitmap(String path) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
        try {
            ExifInterface exif = new ExifInterface(path);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true); // rotating bitmap
        } catch (Exception e) {
        }
        return bitmap;
    }
    public static int getOrientation(String path) {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(path);
            return exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
      return 0;
    }


    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }

    public File createImageFile(String imageName) throws IOException {
        // Create an image file name
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + imageName;
        File albumF = getAlbumDir();
        File imageFile = new File(albumF + File.separator + imageFileName + JPEG_FILE_SUFFIX);
        if (imageFile.exists()) {
            imageFile.delete();
        }
        imageFile.createNewFile();
        return imageFile;
    }
    private File getAlbumDir() {
        File storageDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storageDir = new BaseAlbumDirFactory().getAlbumStorageDir(getAlbumName());
            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {

                        return null;
                    }
                }
            }
        }
        return storageDir;
    }


    private String getAlbumName() {
        return "blogmint";
    }


    public static Point getDisplayPoint(Context context) {
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

}
