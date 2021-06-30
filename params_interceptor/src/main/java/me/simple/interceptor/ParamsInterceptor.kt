package me.simple.interceptor

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer
import org.json.JSONObject
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class ParamsInterceptor
@JvmOverloads constructor(
    //公共参数们
    private val params: ConcurrentHashMap<String, String>,
    //过滤掉不添加公共参数的url
    private val excludeUrls: List<String> = listOf(),
    //公共参数是添加到url上还是请求体中
    private val inPath: Boolean = false,
    //请求执行前的回调
    private val onPreRequest: (params: MutableMap<String, String>) -> Unit = {}
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.toString()

        //请求的ur如果l在过滤Url组里面的直接就执行请求，不添加公共参数
        for (excludeUrl in excludeUrls) {
            if (excludeUrl.isNotEmpty() && url.contains(excludeUrl)) {
                return chain.proceed(request)
            }
        }

        //执行添加公共参数之前的回调
        onPreRequest.invoke(params)

        val newRequest = parseRequest(request)

        return chain.proceed(newRequest)
    }

    /**
     * 解析请求
     */
    private fun parseRequest(request: Request): Request {
        return when (request.method.toLowerCase(Locale.getDefault())) {
            "get" -> {
                addGetParams(request)
            }
            "delete" -> {
                addGetParams(request)
            }
            "head" -> {
                addGetParams(request)
            }
            "options" -> {
                addGetParams(request)
            }

            "post" -> {
                addPostParams(request)
            }
            "put" -> {
                addPostParams(request)
            }
            "patch" -> {
                addPostParams(request)
            }
            else -> {
                addGetParams(request)
            }
        }
    }

    /**
     * 添加GET方式的参数
     */
    private fun addGetParams(request: Request): Request {
        val url = urlAppendParams(request)
        return request.newBuilder()
            .url(url)
            .build()
    }

    /**
     *公共参数添加在Url上
     */
    private fun urlAppendParams(request: Request): HttpUrl {
        val builder = request.url.newBuilder()
        for ((key, value) in params.entries) {
            builder.addQueryParameter(key, value)
        }
        return builder.build()
    }

    /**
     * 添加POST方式的参数
     */
    private fun addPostParams(request: Request): Request {
        return if (inPath) {
            addPostParamsInPath(request)
        } else {
            addPostParamsInBody(request)
        }
    }

    /**
     * POST方式-公共参数添加在Url上
     */
    private fun addPostParamsInPath(request: Request): Request {
        val body = request.body ?: return request
        val url = urlAppendParams(request)
        return request.newBuilder()
            .method(request.method, body)
            .url(url)
            .build()
    }

    /**
     * POST方式-公共参数添加在Body里面
     */
    private fun addPostParamsInBody(request: Request): Request {
        val body = request.body ?: return request
        return when (body) {
            //表单提交
            is FormBody -> {
                val builder = FormBody.Builder()
                //添加原来已有的参数
                for (i in 0 until body.size) {
                    builder.add(body.name(i), body.value(i))
                }
                //添加额外的参数
                for ((key, value) in params.entries) {
                    builder.add(key, value)
                }
                request.newBuilder()
                    .method(request.method, builder.build())
                    .build()
            }
            is MultipartBody -> {
                request
            }
            //Body提交
            else -> {
                val buffer = Buffer()
                body.writeTo(buffer)
                val json = buffer.readUtf8()
                if (json.isEmpty()) return request

                val jsonObject = JSONObject(json)
                for ((key, value) in params.entries) {
                    jsonObject.put(key, value)
                }
                val content: String = jsonObject.toString()
                val contentBody =
                    content.toRequestBody("application/json; charset=UTF-8".toMediaType())
                request.newBuilder()
                    .method(request.method, contentBody)
                    .build()
            }
        }
    }
}