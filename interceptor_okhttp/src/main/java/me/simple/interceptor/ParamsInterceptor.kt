package me.simple.interceptor

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer
import org.json.JSONObject
import java.util.*

class ParamsInterceptor
@JvmOverloads constructor(
    //
    private val params: MutableMap<String, String>,
    //
    private val excludeUrls: List<String> = listOf(),
    //
    private val inPath: Boolean = false,
    //
    private val onPreRequest: (params: MutableMap<String, String>) -> Unit = {}
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.toString()

        for (excludeUrl in excludeUrls) {
            if (excludeUrl.isNotEmpty() && url.contains(excludeUrl)) {
                return chain.proceed(request)
            }
        }

        onPreRequest.invoke(params)

        val newRequest = parseRequest(request)
        return chain.proceed(newRequest)
    }

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

    private fun addGetParams(request: Request): Request {
        val url = urlAppendParams(request)
        return request.newBuilder()
            .url(url)
            .build()
    }

    private fun addPostParams(request: Request): Request {
        return if (inPath) {
            addPostParamsInPath(request)
        } else {
            addPostParamsInBody(request)
        }
    }

    private fun addPostParamsInPath(request: Request): Request {
        val body = request.body ?: return request
        val url = urlAppendParams(request)
        return request.newBuilder()
            .post(body)
            .url(url)
            .build()
    }

    private fun urlAppendParams(request: Request): HttpUrl {
        val builder = request.url.newBuilder()
        for ((key, value) in params.entries) {
            builder.addQueryParameter(key, value)
        }
        return builder.build()
    }

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
                    .post(builder.build())
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
                request.newBuilder()
                    .post(content.toRequestBody("application/json; charset=UTF-8".toMediaType()))
                    .build()
            }
        }
    }
}