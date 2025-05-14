import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://www.googleapis.com/books/v1/"
    const val apiKey = "AIzaSyBHFXynBhdRhe1G6u0RgwVC98wuGE9vXpo"

    val interceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor
        .Level.BODY }

    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

    val googleBooksApi: GoogleBooksApi by lazy {
        Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GoogleBooksApi::class.java)
    }
}