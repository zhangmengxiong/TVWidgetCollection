package com.mx.widget.utils

import android.util.Log
import okhttp3.*
import java.io.File
import java.net.URLEncoder
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * 创建人： zhangmengxiong
 * 创建时间： 2017/4/25.
 * 联系方式: zmx_final@163.com
 */

object HttpDataHelp {
    // 网络连接部分
    private val CHARSET_NAME = "UTF-8"
    private val TIME_OUT = 10
    private var okCache: Cache? = null

    fun getContent(url: String, fromCache: Boolean = false): String? {
        return getContent(url, null, fromCache)
    }

    /**
     * 读取数据

     * @param url
     * *
     * @param params
     * *
     * @return
     */
    @JvmOverloads
    fun getContent(url: String?, params: HashMap<String, String>? = null, fromCache: Boolean = false): String? {
        if (url.isNullOrEmpty()) return null
        var url: String = url ?: return null

        url = DecodeToUrl(url, params)
//        println("" + url)
        var result: String? = null

        val request = Request.Builder()
                .url(url)
                .cacheControl(getCacheControl(fromCache))
                .build()
        var response: Response? = null
        try {
            response = getClient().newCall(request).execute()
            result = response?.body()?.string()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            response?.close()
        }
        return result
    }

    /**
     * 用Post方法发送数据并获取结果

     * @param url
     * *
     * @param params
     * *
     * @return
     */
    fun postContent(url: String?, params: HashMap<String, String>? = null): String? {
        if (url.isNullOrBlank()) return null
        Log.v(TAG, "" + url)

        var response: Response? = null
        var result: String? = null

        val formBody = FormBody.Builder()
        if (params != null && params.size > 0) {
            for ((key, value) in params) {
                formBody.add(key, "" + value)
            }
        }
        val request = Request.Builder()
                .url(url)
                .cacheControl(getCacheControl(false))
                .post(formBody.build())
                .build()

        try {
            response = getClient().newCall(request).execute()
            result = response?.body()?.string()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            response?.close()
        }
        return result
    }

    /**
     * 将请求地址和请求参数进行拼接

     * @param url    请求地址
     * *
     * @param params 参数值
     * *
     * @return
     */
    fun DecodeToUrl(url: String, params: HashMap<String, String>?): String {
        if (params == null || params.size <= 0) return url

        val builder = StringBuilder()
        //处理参数
        var pos = 0
        for (key in params.keys) {
            if (pos > 0) {
                builder.append("&")
            }
            //对参数进行URLEncoder
            builder.append(String.format("%s=%s", key, getEncode(params[key])))
            pos++
        }
        //补全请求地址
        if (url.contains("?")) {
            return String.format("%s&%s", url, builder.toString())
        } else {
            return String.format("%s?%s", url, builder.toString())
        }
    }

    /**
     * 请求参数的编码转换

     * @param name 参数值
     * *
     * @return
     */
    private fun getEncode(name: String?): String? {
        try {
            return URLEncoder.encode(name, CHARSET_NAME)
        } catch (ignored: Exception) {
        }
        return name
    }

    private fun getClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
                .connectTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
        if (okCache == null) {
            // okCache = Cache(File(MyApp.appContext.cacheDir, "okhttp_cache"), (10 * 1024 * 1024).toLong())
        }
        builder.cache(okCache)
        return builder.build()
    }

    private fun getCacheControl(needCache: Boolean): CacheControl {
        val builder = CacheControl.Builder()
        if (needCache) {
            builder.maxAge(24, TimeUnit.HOURS) // 指示客户机可以接收生存期不大于指定时间的响应。
        } else {
            builder.noCache()//不使用缓存，全部走网络
        }
        return builder.build()//cacheControl
    }

    /**
     * Post方法上传文件和参数

     * @param url
     * *
     * @param paramsMap
     * *
     * @return
     */
    fun postFile(url: String?, paramsMap: HashMap<String, Any>): String? {
        var response: Response? = null
        var result: String? = null
        try {
            val builder = MultipartBody.Builder()
            //设置类型
            builder.setType(MultipartBody.FORM)
            //追加参数
            for (key in paramsMap.keys) {
                val obj = paramsMap[key]
                if (obj !is File) {
                    builder.addFormDataPart(key, obj.toString())
                } else {
                    builder.addFormDataPart(key, obj.name, RequestBody.create(null, obj))
                }
            }
            //创建RequestBody
            val body = builder.build()
            //创建Request
            val request = Request.Builder()
                    .url(url)
                    .cacheControl(getCacheControl(false))
                    .post(body)
                    .build()
            //单独设置参数 比如读取超时时间
            val call = getClient().newCall(request)
            response = call.execute()
            result = response?.body()?.string()
        } catch (e1: Exception) {
            e1.printStackTrace()
        } finally {
            if (response != null) {
                response.close()
            }
        }
        return result
    }
}