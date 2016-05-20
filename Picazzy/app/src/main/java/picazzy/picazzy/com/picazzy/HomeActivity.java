package picazzy.picazzy.com.picazzy;

import android.app.ActionBar;
import android.graphics.Point;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.MediaStore;
import android.content.Intent;
import android.net.Uri;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Environment;
import android.content.Context;
import android.database.Cursor;
import android.app.Activity;
import android.util.Log;
import android.support.v4.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.content.ActivityNotFoundException;
import android.graphics.Bitmap;
import android.widget.ImageView.ScaleType;
import android.graphics.BitmapFactory;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.ig.crop.Crop;

import picazzy.picazzy.com.picazzy.utils.BaseAlbumDirFactory;
import picazzy.picazzy.com.picazzy.utils.BitmapUtility;
import picazzy.picazzy.com.picazzy.utils.PermissionUtils;


public class HomeActivity extends AppCompatActivity {
    private final static int ACTIVITY_PICK_IMAGE = 0;
    private final static int ACTIVITY_TAKE_PHOTO = 1;

    private final static int RESULT_SELECT_IMAGE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 107;

    private static final String TAG = "GalleryUtil";
    private final int GALLERY_ACTIVITY_CODE = 200;
    private final int RESULT_CROP = 400;

    private String picturePath = null;


    private static final int REQUEST_CAMERA = 100;
    private static final int SELECT_FILE = 200;
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private static final int REQUEST_SAVE_IMAGE_CAMERA = 4;
    private static final int REQUEST_SAVE_IMAGE_GALLERY = 5;
    private static final int REQUEST_SELECT_FILE = 1;
    private static final int REQUEST_SELECT_CAMERA = 2;
    private String mCurrentPhotoPath;
    private String mSecondImagePath;
    private String mFirstImagePath;
    private String mImagePath;



    private ImageButton btn_photoAlbum, btn_Camera;
    private ImageView imgVw_logo;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private BaseAlbumDirFactory mAlbumStorageDirFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btn_photoAlbum = (ImageButton) findViewById(R.id.btn_PhotoAlbum);
        btn_Camera = (ImageButton) findViewById(R.id.btn_Camera);
        btn_Camera.setOnClickListener(onClickListener);
        btn_photoAlbum.setOnClickListener(onClickListener);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        mAlbumStorageDirFactory = new BaseAlbumDirFactory();
    }


    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {

                case R.id.btn_Camera:
                    dispatchTakePictureIntent();
                    break;
                case R.id.btn_PhotoAlbum:
                    openGalleryPermission();
                    break;

            }

        }
    };

    private void dispatchTakePictureIntent() {

        if (!PermissionUtils.getInstance().hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            PermissionUtils.getInstance().needPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_SAVE_IMAGE_CAMERA, "Save Cropped Image");
            // Now you will get callback in onRequestPermissionsResult() method for further code
        } else {
            if (!PermissionUtils.getInstance().hasPermission(this, Manifest.permission.CAMERA)) {
                PermissionUtils.getInstance().needPermission(this, Manifest.permission.CAMERA, REQUEST_SELECT_CAMERA, "Open Camera!");
                // Now you will get callback in onRequestPermissionsResult() method for further code
            } else {
                openCamera();
                //Your code related to permission
            }
        }

    }
    public void openGalleryPermission() {
        System.out.println("............."+PermissionUtils.getInstance().hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE));
        if (!PermissionUtils.getInstance().hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            PermissionUtils.getInstance().needPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_SAVE_IMAGE_GALLERY, "Save Cropped Image");
            // Now you will get callback in onRequestPermissionsResult() method for further code
        } else {
            if (!PermissionUtils.getInstance().hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                PermissionUtils.getInstance().needPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_SELECT_FILE, "Select File!");
                // Now you will get callback in onRequestPermissionsResult() method for further code
            } else {
                openGallery();
                //Your code related to permission
            }
        }
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            File f = setUpPhotoFile();
            mCurrentPhotoPath = f.getAbsolutePath();
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        } catch (IOException e) {
            e.printStackTrace();
            mCurrentPhotoPath = null;
        }
        startActivityForResult(takePictureIntent, REQUEST_CAMERA);
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);

    }
    private File setUpPhotoFile() throws IOException {
        File f = createImageFile();
        mCurrentPhotoPath = f.getAbsolutePath();
        return f;
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }
    private File getAlbumDir() {
        File storageDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());
            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        return null;
                    }
                }
            }
        } else {
            // TODO : Add handling
        }
        return storageDir;
    }
    private String getAlbumName() {
        return getString(R.string.app_name);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAMERA:
                    handleCameraPic();
                    break;
                case SELECT_FILE:
                    if (data != null) {
                        Uri picUri = data.getData();
                        handleGalleryPic(picUri);
                    }
                    break;
                case Crop.REQUEST_CROP:
                    handleCrop(resultCode, data);
                    break;

            }
        }
    }

    private void handleCameraPic() {
        if (mCurrentPhotoPath != null) {
            galleryAddPic();
            sendImage(mCurrentPhotoPath);
        }
    }
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

    private void handleGalleryPic(Uri picUri) {
        if (picUri != null) {
            mCurrentPhotoPath = getPath(picUri);
            if (mCurrentPhotoPath != null) {
                sendImage(mCurrentPhotoPath);
            } else {
            }
        }
    }

    private void sendImage(String path) {
        beginCrop(Uri.fromFile(new File(path)));
    }
    private void beginCrop(Uri source) {
        BitmapUtility bitmapUtility = new BitmapUtility(this);
        Uri outputUri = null;
        try {
            outputUri = Uri.fromFile(bitmapUtility.createImageFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Point size = BitmapUtility.getDisplayPoint(this);
        int width = size.x;
        int height = width;
        new Crop(source).output(outputUri).withAspect(width, height).start(this);

    }


    public String getPath(Uri uri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri,
                filePathColumn, null, null, null);
        String picturePath = null;
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
        }
        return picturePath;
    }


    public void handleCrop(int resultCode, Intent result) {

        if (resultCode == Activity.RESULT_OK) {
            Uri croppedUri = Crop.getOutput(result);
            File outPutFile = new File(croppedUri.getPath());
            mImagePath = outPutFile.getAbsolutePath();
            mCurrentPhotoPath = mImagePath;
            Intent intent = new Intent(HomeActivity.this, EditorActivity.class);
            intent.putExtra("uri", mCurrentPhotoPath);
            startActivity(intent);

        }

    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Home Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://picazzy.picazzy.com.picazzy/http/host/path")
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
                "Home Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://picazzy.picazzy.com.picazzy/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}


