package es.i12capea.data.api


import android.util.Log
import es.i12capea.domain.exceptions.RequestException
import es.i12capea.domain.exceptions.ResponseException
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

fun <T> Call<T>.call(): T {

    val latch = CountDownLatch(1)
    var result: T? = null
    var throwable : Throwable? = null

    val request = this.request().body()
    val method = this.request().method()
    val url = this.request().url().toString()
    val headers = this.request().headers().toMultimap()
    Log.d("Headers ", "Headers + " + this.request().headers())

    RetrofitPrinter().print(url, method, bodyToString(request), headers)

    this.enqueue(object : Callback<T> {
        override fun onFailure(call: Call<T>, t: Throwable) {
            throwable = RequestException()
            latch.countDown()
        }

        override fun onResponse(call: Call<T>, response: Response<T>) {
            when(response.isSuccessful){
                true -> {
                    response.body()?.let { body ->
                        RetrofitPrinter().print(url, method, body.toString(), headers, false)
                        result = body
                    }
                    result = response.body()!!
                    latch.countDown()
                }
                false -> {
                    throwable = ResponseException(response.code(), response.message())
                    latch.countDown()
                }
            }
        }
    })

    latch.await()
    throwable?.let {
        throw it
    }

    return result as T
}

private class RetrofitPrinter {

    fun print(url: String, method: String, json: String?, headers: MutableMap<String, List<String>> , isRequest: Boolean = true){
        val tag = "Connection"
        logNewLine(tag)
        when(isRequest){
            true -> Log.i(tag, "================ R E Q U E S T ================")
            false -> Log.i(tag,"=============== R E S P O N S E ===============")
        }
        logNewLine(tag)
        logTab(tag, "URL: $url")
        logTab(tag, "METHOD: $method")
        logTab(tag, "BODY:")
        json?.let {
            logTab(tag, it)
        }
        logNewLine(tag)
        logTab(tag, "HEADERS:")
        for (element in headers){
            logTab(tag, element.component1() + " : " + element.component2())
        }
        Log.i(tag, "===============================================")
        logNewLine(tag)
    }
}

private fun bodyToString(request: RequestBody?): String {
    return request?.let {
        try {
            val buffer = okio.Buffer()
            request.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: IOException) {
            "did not work"
        }
    } ?: ""
}

fun logTab(tag: String, message: String) {
    var i = 0
    val length = message.length
    while (i < length) {
        var newline = message.indexOf('\n', i)
        newline = if (newline != -1) newline else length
        do {
            val end = Math.min(newline, i + 2000)
            Log.i(tag, "    " + message.substring(i, end))
            i = end
        } while (i < newline)
        i++
    }
}

fun logNewLine(tag: String){
    val newLine = System.getProperty("line.separator")
    newLine?.let {
        Log.i(tag, "$newLine ")
    }
}