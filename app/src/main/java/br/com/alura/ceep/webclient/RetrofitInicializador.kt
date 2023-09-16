package br.com.alura.ceep.webclient

import br.com.alura.ceep.webclient.services.NotaService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class RetrofitInicializador {
    var logging: HttpLoggingInterceptor = HttpLoggingInterceptor()
    var client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging.setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()


    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.100.40:8080/")
        .addConverterFactory(MoshiConverterFactory.create())
        .client(client)
        .build()

    val notaService = retrofit.create(NotaService::class.java)


}