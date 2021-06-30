package demo.simple.paramsinterceptor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.readystatesoftware.chuck.ChuckInterceptor
import kotlinx.android.synthetic.main.activity_main.*
import me.simple.interceptor.ParamsInterceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.ConcurrentHashMap

class MainActivity : AppCompatActivity() {

//    private val params = ConcurrentHashMap<String, String>(
//        "params1" to "simple",
//        "params2" to "peng",
//        "params3" to "hahaha"
//    )

    private val params = ConcurrentHashMap<String, String>().apply {
        "params1" to "simple"
        "params2" to "peng"
        "params3" to "hahaha"
    }

    private val baseUrl = "http://127.0.0.1/"

    private var excludeUrls = listOf<String>("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_get.setOnClickListener {
            getApi().get().enqueue(listener)
        }

        btn_post.setOnClickListener {
            getApi().postForm("simple").enqueue(listener)
        }

        btn_postBody.setOnClickListener {
            getApi().postBody(mapOf()).enqueue(listener)
        }

        btn_put.setOnClickListener {
            getApi().put("simple").enqueue(listener)
        }

        btn_putBody.setOnClickListener {
            getApi().putBody(mapOf()).enqueue(listener)
        }
    }

    private val listener = object : retrofit2.Callback<String> {
        override fun onFailure(call: Call<String>, t: Throwable) {

        }

        override fun onResponse(call: Call<String>, response: Response<String>) {

        }
    }

    private fun getApi(): Api {
//        return JavaApi.getApi(baseUrl, this, params, excludeUrls)
        return getKtApi()
    }

    private fun getKtApi(): Api {
        val httpClient = OkHttpClient.Builder()
            .addInterceptor(ParamsInterceptor(params, inPath = false, onPreRequest = { params ->
                params["params3"] = "hei hei hei"
                params["params4"] = "tu tu tu"
            }))
            .addInterceptor(ChuckInterceptor(this))
            .build()
        return Retrofit.Builder()
            .client(httpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)
    }
}
