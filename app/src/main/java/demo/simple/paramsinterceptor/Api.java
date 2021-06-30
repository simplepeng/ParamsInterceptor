package demo.simple.paramsinterceptor;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface Api {

    @GET("get")
    Call<String> get();

    @FormUrlEncoded
    @POST("postForm")
    Call<String> postForm(@Field("name") String name);

    @POST("postBody")
    Call<String> postBody(@Body Map<String, String> map);

    @FormUrlEncoded
    @PUT("put")
    Call<String> put(@Field("name") String name);

    @PUT("putBody")
    Call<String> putBody(@Body Map<String, String> map);
}
