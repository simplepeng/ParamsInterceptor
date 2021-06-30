package demo.simple.paramsinterceptor;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.OPTIONS;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

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

    @DELETE("delete")
    Call<String> delete();

    @HEAD("head")
    Call<Void> head();

    @OPTIONS("options")
    Call<String> options();

    @FormUrlEncoded
    @PATCH("patch")
    Call<String> patch(@Field("name") String name);

    @PATCH("patchBody")
    Call<String> patchBody(@Body Map<String, String> map);
}
