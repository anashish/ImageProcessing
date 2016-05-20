package picazzy.picazzy.com.picazzy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import android.os.Bundle;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import picazzy.picazzy.com.picazzy.model.ImageParam;
import picazzy.picazzy.com.picazzy.network.ImageApi;
import picazzy.picazzy.com.picazzy.utils.BitmapUtility;
import picazzy.picazzy.com.picazzy.utils.Constant;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Query;
import retrofit.mime.TypedByteArray;

/**
 * Created by SHARMA on 5/14/16.
 */
public class EditorActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<EffectsModel> effectsModels1;
    private List<EffectsModel> effectsModels2;
    private List<EffectsModel> effectsModels3;
    private List<EffectsModel> effectsModels4;

    private EffectsAdapter effectsAdapter;
    private Button btn_paintify;
    private Button btn_splash;
    private Button btn_electic;
    private Button btn_cocoon;

    /**
     * Static List of All the subcategory resources
     */
    private int[] list_paintify = {R.drawable.p_pt11, R.drawable.p_pt7, R.drawable.pt20, R.drawable.p_pt32, R.drawable.p_pt36, R.drawable.p_ps03, R.drawable.p_pt37, R.drawable.p_pt30, R.drawable.p_pt4, R.drawable.p_pt1, R.drawable.p_pt19};
    private int[] list_splash = {R.drawable.p_pt34, R.drawable.p_pt3, R.drawable.p_pt2_1, R.drawable.p_pt22, R.drawable.p_pt2_3, R.drawable.p_pt29, R.drawable.p_pt18, R.drawable.p_pt2_4, R.drawable.p_pt2_2, R.drawable.p_pt15, R.drawable.p_pt2_5, R.drawable.p_pt9};
    private int[] list_electic = {R.drawable.p_pt28, R.drawable.p_pt31, R.drawable.p_pt27, R.drawable.p_pt35, R.drawable.p_pt21, R.drawable.p_pt13, R.drawable.p_pt26, R.drawable.p_pt17, R.drawable.ps02, R.drawable.p_ps04, R.drawable.p_ps05};
    private int[] list_cocoon = {R.drawable.p_pt33, R.drawable.p_pt6, R.drawable.p_pt23_2, R.drawable.p_ps07, R.drawable.p_ps08, R.drawable.p_pt5_1, R.drawable.p_ps06, R.drawable.p_pt25_1, R.drawable.p_pt5_4, R.drawable.p_pt25_2, R.drawable.p_pt23_1};

    private String[] title_paintify;
    private String[] title_splash;
    private String[] title_electic;
    private String[] title_cocoon;

    private ImageView imageView;
    private ImageView imv_back_button;
    private ImageView imv_share_button;
    private Bitmap myBitmap;
    private String path = null;
    private String mImageOriention;
    private String mEffectName;
    private String mBase64Image;

    private ImageParam mImageParam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        init();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mImageParam=new ImageParam();

        imv_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imv_share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                share.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
                startActivity(Intent.createChooser(share, "Share Image"));
            }
        });
        path = getIntent().getStringExtra("uri");
        myBitmap= BitmapUtility.getBitmap(path);
        Drawable bitmapDrawable = new BitmapDrawable(myBitmap);
        imageView.setBackground(bitmapDrawable);
        mImageOriention=""+BitmapUtility.getOrientation(path);






        prepareData();

        /**
         * on click listners for 4 category buttons
         */
        btn_paintify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_paintify.setBackgroundColor(Color.parseColor("#000000"));
                btn_paintify.setTextColor(Color.parseColor("#ffffff"));

                splashOff();
                electicOff();
                cocoonOff();

                effectsAdapter = new EffectsAdapter(effectsModels1, new EffectsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(EffectsModel item) {
                        hitService(item);
                        Toast.makeText(EditorActivity.this, item.getTitle() + "", Toast.LENGTH_SHORT).show();
                    }
                });
                recyclerView.setAdapter(effectsAdapter);
            }
        });
        btn_splash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_splash.setBackgroundColor(Color.parseColor("#000000"));
                btn_splash.setTextColor(Color.parseColor("#ffffff"));

                paintifyOff();
                electicOff();
                cocoonOff();

                effectsAdapter = new EffectsAdapter(effectsModels2, new EffectsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(EffectsModel item) {

                        hitService(item);
                        Toast.makeText(EditorActivity.this, item.getTitle() + "", Toast.LENGTH_SHORT).show();
                    }
                });
                recyclerView.setAdapter(effectsAdapter);
            }
        });
        btn_electic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_electic.setBackgroundColor(Color.parseColor("#000000"));
                btn_electic.setTextColor(Color.parseColor("#ffffff"));

                paintifyOff();
                splashOff();
                cocoonOff();

                effectsAdapter = new EffectsAdapter(effectsModels3, new EffectsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(EffectsModel item) {
                        hitService(item);
                        Toast.makeText(EditorActivity.this, item.getTitle() + "", Toast.LENGTH_SHORT).show();
                    }
                });
                recyclerView.setAdapter(effectsAdapter);
            }
        });
        btn_cocoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_cocoon.setBackgroundColor(Color.parseColor("#000000"));
                btn_cocoon.setTextColor(Color.parseColor("#ffffff"));

                paintifyOff();
                electicOff();
                splashOff();

                effectsAdapter = new EffectsAdapter(effectsModels4, new EffectsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(EffectsModel item) {
                        hitService(item);
                        Toast.makeText(EditorActivity.this, item.getTitle() + "", Toast.LENGTH_SHORT).show();
                    }
                });
                recyclerView.setAdapter(effectsAdapter);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.filter_list);
        effectsAdapter = new EffectsAdapter(effectsModels1, new EffectsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(EffectsModel item) {
                hitService(item);
                Toast.makeText(EditorActivity.this, item.getTitle() + "", Toast.LENGTH_SHORT).show();
            }
        });
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(effectsAdapter);

    }


    /**
     * method to hit the web service
     * uses item name as parameter
     * item name is the selected effect's name
     * fetch bitmap from variable "myBitmap" or the image uri from String variable "path"
     *
     * @param item
     */
    void hitService(EffectsModel  item) {

        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Please wait ...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(true);
        progress.show();
        mEffectName=item.getTitle();
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constant.BASE_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();



        String eplaceCode=item.getCode().replace("p_","");

        ImageApi imageApi=adapter.create(ImageApi.class);
        String baseImage=Constant.getBase64ImageString(myBitmap);
        imageApi.submitPhotoDetail(eplaceCode,baseImage,"portrait",mEffectName,"mobile",new Callback<Response>() {


            @Override
            public void success(Response s, Response response) {


                myBitmap=Constant.getBitmapFromBase64(new String(((TypedByteArray) response.getBody()).getBytes()));

                Drawable bitmapDrawable = new BitmapDrawable(myBitmap);
                imageView.setBackground(bitmapDrawable);
                progress.dismiss();


            }

            @Override
            public void failure(RetrofitError error) {

                progress.dismiss();
                Toast.makeText(EditorActivity.this,"Something went wrong .Please try later.", Toast.LENGTH_SHORT).show();


            }



        });




    }


    /**
     * Changing active state of the buttons to inactive
     */
    void paintifyOff() {
        btn_paintify.setBackgroundColor(Color.parseColor("#ffffff"));
        btn_paintify.setTextColor(Color.parseColor("#000000"));
    }

    void electicOff() {
        btn_electic.setBackgroundColor(Color.parseColor("#ffffff"));
        btn_electic.setTextColor(Color.parseColor("#000000"));
    }

    void splashOff() {
        btn_splash.setBackgroundColor(Color.parseColor("#ffffff"));
        btn_splash.setTextColor(Color.parseColor("#000000"));
    }

    void cocoonOff() {
        btn_cocoon.setBackgroundColor(Color.parseColor("#ffffff"));
        btn_cocoon.setTextColor(Color.parseColor("#000000"));
    }
    //

    /**
     * Activity related variable and resource initialization
     */
    void init() {
        effectsModels1 = new ArrayList<EffectsModel>();
        effectsModels2 = new ArrayList<>();
        effectsModels3 = new ArrayList<>();
        effectsModels4 = new ArrayList<>();
        imv_share_button = (ImageView) findViewById(R.id.share_button);
        imv_back_button = (ImageView) findViewById(R.id.back_button);
        imageView = (ImageView) findViewById(R.id.edit_image);
        btn_paintify = (Button) findViewById(R.id.b1);
        btn_splash = (Button) findViewById(R.id.b2);
        btn_electic = (Button) findViewById(R.id.b3);
        btn_cocoon = (Button) findViewById(R.id.b4);
        title_paintify = getApplicationContext().getResources().getStringArray(R.array.effect_names_paintify);
        title_splash = getApplicationContext().getResources().getStringArray(R.array.effect_names_splash);
        title_electic = getApplicationContext().getResources().getStringArray(R.array.effect_names_electic);
        title_cocoon = getApplicationContext().getResources().getStringArray(R.array.effect_name_cocoon);
    }

    /**
     * preparing subcategory data for the adapters
     */
    private void prepareData() {
        EffectsModel effectsModel;


        int i;
        for (i = 0; i < list_paintify.length; i++) {

            String name = getResources().getResourceEntryName(list_paintify[i]);

            effectsModel = new EffectsModel(title_paintify[i], list_paintify[i],name);

            effectsModels1.add(effectsModel);
        }
        for (i = 0; i < list_splash.length; i++) {
            String name = getResources().getResourceEntryName(list_splash[i]);

            effectsModel = new EffectsModel(title_splash[i], list_splash[i],name);
            effectsModels2.add(effectsModel);
        }
        for (i = 0; i < list_electic.length; i++) {
            String name = getResources().getResourceEntryName(list_electic[i]);
            effectsModel = new EffectsModel(title_electic[i], list_electic[i],name);
            effectsModels3.add(effectsModel);
        }
        for (i = 0; i < list_cocoon.length; i++) {
            String name = getResources().getResourceEntryName(list_cocoon[i]);
            effectsModel = new EffectsModel(title_cocoon[i], list_cocoon[i],name);
            effectsModels4.add(effectsModel);
        }
    }

}
