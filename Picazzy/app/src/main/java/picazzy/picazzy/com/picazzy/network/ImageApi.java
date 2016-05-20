package picazzy.picazzy.com.picazzy.network;

import com.google.gson.annotations.SerializedName;

import picazzy.picazzy.com.picazzy.model.ImageParam;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.Query;


/**
 * Created by Ashish on 19/5/16.
 */
public interface ImageApi {


  /*  @POST("/index.php")
     void  submitPhotoDetail(@Body  ImageParam request, Callback<String> callback);*/


    @POST("/index.php")
    void  submitPhotoDetail(@Query("code") String code,@Query("image") String base64,
            @Query("dimension") String dimension,@Query("effectname") String effectName,
                            @Query("height") String height
                            ,Callback<Response> callback);



}
