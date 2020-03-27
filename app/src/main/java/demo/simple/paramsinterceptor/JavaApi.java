package demo.simple.paramsinterceptor;

import android.content.Context;

import com.readystatesoftware.chuck.ChuckInterceptor;

import java.util.List;
import java.util.Map;

import me.simple.interceptor.ParamsInterceptor;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JavaApi {
    public static Api getApi(
            String baseUrl,
            Context context,
            Map<String, String> params,
            List<String> excludeUrls) {
        OkHttpClient httpClient = new OkHttpClient.Builder()
//                .addInterceptor(new ParamsInterceptor(params,excludeUrls,true,))
                .addInterceptor(new ChuckInterceptor(context))
                .build();
        return new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Api.class);
    }
}
