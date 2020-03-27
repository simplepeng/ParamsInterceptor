package demo.simple.paramsinterceptor;

import android.content.Context;

import com.readystatesoftware.chuck.ChuckInterceptor;

import java.util.List;
import java.util.Map;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import me.simple.interceptor.ParamsInterceptor;
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
                .addInterceptor(new ParamsInterceptor(params, excludeUrls, false, map -> {
                    map.put("params3", "ta ta ta");
                    map.put("params4", "tt tt tt");
                    return null;
                }))
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
