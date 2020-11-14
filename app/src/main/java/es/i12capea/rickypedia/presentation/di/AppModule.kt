package es.i12capea.rickypedia.presentation.di

import android.app.Application
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import es.i12capea.rickypedia.R
import es.i12capea.rickypedia.common.Constants
import es.i12capea.rickypedia.data.api.CharacterApi
import es.i12capea.rickypedia.data.api.EpisodesApi
import es.i12capea.rickypedia.data.api.LocationApi
import es.i12capea.rickypedia.data.local.RymDatabase
import es.i12capea.rickypedia.data.local.dao.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
class AppModule {

    @Provides
    @Singleton
    @Named("fbAuth")
    fun getFirebaseAuth() : FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideCoroutineDispatcher() : CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Provides
    @Singleton
    fun provideGsonBuilder() : Gson {
        return GsonBuilder()
            .create()
    }

    @Provides
    @Singleton
    fun provideInterceptor(): Interceptor {
        return Interceptor {
            val newRequest = it.request().newBuilder().apply {
                addHeader("Content-type", "application/json")
                addHeader("Accept", "application/json")
            }.build()
            val response = it.proceed(newRequest)
            response
        }
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(interceptor: Interceptor) : OkHttpClient{
        return OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            //.sslSocketFactory(sslContext().socketFactory, x509TrustManager())
            //.sslSocketFactory(sslContext().socketFactory)
            .addInterceptor(interceptor)
            //.hostnameVerifier { hostname, _ ->
            //    hostname.equals(BuildConfig.HOST_NAME_VERIFIER, true)
            //}
            //.hostnameVerifier { hostname, _ ->
            //    true
            //}
            .build()
    }

    @Provides
    @Singleton
    fun providesRetrofitBuilder(okHttpClient: OkHttpClient , gson : Gson) : Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            //.callbackExecutor(Runnable::run)
    }

    @Provides
    @Singleton
    fun provideCharacterApi(retrofit: Retrofit.Builder) : CharacterApi{
        return retrofit.build()
            .create(CharacterApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLocationApi(retrofit: Retrofit.Builder) : LocationApi{
        return retrofit.build()
            .create(LocationApi::class.java)
    }

    @Singleton
    @Provides
    fun provideEpisodeApi(retrofit: Retrofit.Builder) : EpisodesApi{
        return retrofit.build()
            .create(EpisodesApi::class.java)
    }

    @Singleton
    @Provides
    fun provideRequestOptions(): RequestOptions {
        return RequestOptions
            .placeholderOf(R.drawable.common_full_open_on_phone)
            .error(R.drawable.common_full_open_on_phone)
    }

    @Singleton
    @Provides
    fun provideGlideInstance(application: Application, requestOptions: RequestOptions): RequestManager {
        return Glide.with(application)
            .setDefaultRequestOptions(requestOptions)
    }

    @Singleton
    @Provides
    fun provideAppDb(app: Application): RymDatabase {
        return Room
            .databaseBuilder(app, RymDatabase::class.java, Constants.DB_NAME)
            .fallbackToDestructiveMigration() // get correct db version if schema changed
            .build()
    }

    @Singleton
    @Provides
    fun provideLocalCharacterPageDao(db: RymDatabase): LocalCharacterPageDao {
        return db.getLocalCharacterPageDao()
    }

    @Singleton
    @Provides
    fun provideLocalCharacterDao(db: RymDatabase): LocalCharacterDao {
        return db.getLocalCharacterDao()
    }

    @Singleton
    @Provides
    fun provideLocalEpisodeDao(db: RymDatabase): LocalEpisodeDao {
        return db.getLocalEpisodeDao()
    }

    @Singleton
    @Provides
    fun provideLocalEpisodePageDao(db: RymDatabase): LocalEpisodePageDao {
        return db.getLocalEpisodePageDao()
    }

    @Singleton
    @Provides
    fun provideLocalLocationDao(db: RymDatabase): LocalLocationDao {
        return db.getLocalLocationDao()
    }

    @Singleton
    @Provides
    fun provideLocalLocationPageDao(db: RymDatabase): LocalLocationPageDao {
        return db.getLocalLocationPageDao()
    }

}