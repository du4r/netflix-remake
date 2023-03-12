package com.example.netflixremake.utils

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.netflixremake.models.Category
import com.example.netflixremake.models.Movie
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection

class CategoryTask(val callback: Callback){
    private val handler = Handler(Looper.getMainLooper())
    private val executor = Executors.newSingleThreadExecutor()


    interface Callback{
        fun onResult(categories: List<Category>)
        fun onFailure(message: String)
        fun onPreExecute()
    }

    fun execute(url: String){
        callback.onPreExecute()

        executor.execute{
            var urlConnection: HttpsURLConnection? = null
            var stream: InputStream? = null
            var buffer: BufferedInputStream? = null

            try {

                val requestUrl = URL(url)
                urlConnection = requestUrl.openConnection() as HttpsURLConnection
                urlConnection.readTimeout = 2000
                urlConnection.connectTimeout = 2000

                val statusCode = urlConnection.responseCode

                if (statusCode > 400){
                    throw IOException("Erro na comunicacao com o servidor")
                }

                stream = urlConnection.inputStream
                buffer = BufferedInputStream(stream)
                val jsonAsString = toString(buffer)


                val categories = toCategories(jsonAsString)


                handler.post {
                    // retorna para a UI-thread
                    callback.onResult(categories)
                }


            }   catch (e: IOException){
                val message = e.message ?: "erro desconhecido"
                Log.e("teste", message, e)

                handler.post{
                    callback.onFailure(message)
                }
            }finally {

                    urlConnection?.disconnect()

                    stream?.close()

                    buffer?.close()

            }
        }
    }

    private fun toCategories(jsonAsString: String): List<Category> {

        val categories = mutableListOf<Category>()

        val jsonRoot = JSONObject(jsonAsString)
        val jsonCategories = jsonRoot.getJSONArray("category")

        for (i in 0 until jsonCategories.length()){
            val jsonCategory = jsonCategories.getJSONObject(i)
            val title = jsonCategory.getString("title")
            val jsonMovies = jsonCategory.getJSONArray("movie")

            val movies = mutableListOf<Movie>()
            for (j in 0 until jsonMovies.length()){
                val jsonMovie = jsonMovies.getJSONObject(j)
                val id = jsonMovie.getInt("id")
                val coverUrl  = jsonMovie.getString("cover_url")
                movies.add(Movie(id, coverUrl))
            }
            categories.add(Category(title, movies))
        }
        return categories
    }

    private fun toString(stream: InputStream): String{
        val bytes = ByteArray(1024)
        val baos = ByteArrayOutputStream()
        var read: Int
        while (true){
            read = stream.read(bytes)
            if (read <= 0){
                break
            }
            baos.write(bytes, 0, read)
        }
        return String(baos.toByteArray())
    }

}