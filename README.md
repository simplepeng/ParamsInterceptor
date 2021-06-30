# ParamsInterceptor
![MIT](https://img.shields.io/badge/License-MIT-orange?style=flat-square)  [![](https://jitpack.io/v/simplepeng/ParamsInterceptor.svg)](https://jitpack.io/#simplepeng/ParamsInterceptor)  ![Androidx](https://img.shields.io/badge/Androidx-Yes-blue?style=flat-square)  ![Api](https://img.shields.io/badge/Api-14+-blueviolet?style=flat-square)  ![Kotlin](https://img.shields.io/badge/Kotlin-Yes-ff6984?style=flat-square) ![OkHttp](https://img.shields.io/badge/OkHttp-4.2.0-yellow?style=flat-square)

给OkHttp添加公共请求参数的Interceptor

## 依赖

```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

```groovy
implementation 'com.github.simplepeng:ParamsInterceptor:v1.0.2'
```

## 使用

```kotlin
private val params = ConcurrentHashMap<String, String>().apply {
    "params1" to "simple"
    "params2" to "peng"
    "params3" to "hahaha"
}

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
    private val params: ConcurrentHashMap<String, String>,
    //过滤掉不添加公共参数的url
    private val excludeUrls: List<String> = listOf(),
    //公共参数是添加到url上还是请求体中
    private val inPath: Boolean = false,
    //请求执行前的回调
    private val onPreRequest: (params: MutableMap<String, String>) -> Unit = {}
)
```

## 问题反馈Q群：1078185041

<img src="https://raw.githubusercontent.com/simplepeng/ImageRepo/master/q_group.jpg" width="270px" height="370px">

## 版本迭代

* v1.0.2：增加其他请求方式的适配

* v1.0.1：解决线程并发执行发生`ConcurrentModificationException`的bug


* v1.0.0：首次上传