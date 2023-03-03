package com.mbrainz.sample.data.manager

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mbrainz.sample.data.model.remote.ArtistEntity
import com.mbrainz.sample.data.model.remote.SearchArtistsResponse
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MusicBrainzRetrofitService {
    @GET("artist")
    suspend fun queryArtists(@Query("query") searchTerm: String): SearchArtistsResponse
    @GET("artist/{id}?inc=releases")
    suspend fun artistReleases(@Path("id") artistId: String): ArtistEntity
}

@OptIn(ExperimentalSerializationApi::class)
class MusicBrainzApiManagerImpl : MusicBrainzApiManager {
    private val json = Json { ignoreUnknownKeys = true }

    private val okHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(
            Interceptor { chain ->
                val builder = chain.request().newBuilder()
                builder.header("Accept", contentType)
                builder.header("User-Agent", userAgent)
                return@Interceptor chain.proceed(builder.build())
            }
        )
    }.build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory(MediaType.get(contentType)))
        .build()

    private val apiService = retrofit.create(MusicBrainzRetrofitService::class.java)

    override suspend fun searchArtist(searchTerm: String) = apiService.queryArtists("artist:$searchTerm")
    override suspend fun getArtistReleases(artistId: String) = apiService.artistReleases(artistId)

    companion object {
        private const val BASE_URL = "https://musicbrainz.org/ws/2/"
        private const val contentType = "application/json"
        private const val userAgent = "antdesca-sample/1.0 (antoine.descamps@paris.msg.org)"
    }
}