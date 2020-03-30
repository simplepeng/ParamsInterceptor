# ParamsInterceptor
给OkHttp添加公共请求参数的Interceptor

## 依赖

```groovy

```

## 使用

```kotlin
    private val params = mutableMapOf<String, String>(
        "params1" to "simple",
        "params2" to "peng",
        "params3" to "hahaha"
    )

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(ParamsInterceptor(params, inPath = true, onPreRequest = { params ->
                params["params3"] = "hei hei hei"
                params["params4"] = "tu tu tu"
            }))
```

### 构造参数详解

```kotlin
class ParamsInterceptor
@JvmOverloads constructor(
    //公共参数们
    private val params: MutableMap<String, String>,
    //过滤掉不添加公共参数的url
    private val excludeUrls: List<String> = listOf(),
    //公共参数是添加到url上还是请求体中
    private val inPath: Boolean = false,
    //请求执行前的回调
    private val onPreRequest: (params: MutableMap<String, String>) -> Unit = {}
)
```

## 加入群聊：1078185041

<img src="https://raw.githubusercontent.com/simplepeng/ImageRepo/master/q_group.jpg" width="270px" height="370px">

## 版本迭代

* v1.0.0：首次上传