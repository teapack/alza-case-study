package cz.vratislavjindra.alzacasestudy.dependency_injection

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import cz.vratislavjindra.alzacasestudy.feature_categories.data.local.CategoryDatabase
import cz.vratislavjindra.alzacasestudy.feature_categories.data.remote.CategoryApi
import cz.vratislavjindra.alzacasestudy.feature_categories.data.repository.CategoryRepositoryImpl
import cz.vratislavjindra.alzacasestudy.feature_categories.domain.repository.CategoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCategoryRepository(
        api: CategoryApi,
        db: CategoryDatabase
    ): CategoryRepository {
        return CategoryRepositoryImpl(api = api, dao = db.categoryDao)
    }

    @Provides
    @Singleton
    fun provideCategoryApi(retrofit: Retrofit): CategoryApi {
        return retrofit.create(CategoryApi::class.java)
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor())
            .build()
        val json = Json {
            ignoreUnknownKeys = true
        }
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory(contentType))
            .baseUrl("https://www.alza.cz/Services/RestService.svc/")
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideCategoryDatabase(@ApplicationContext context: Context) : CategoryDatabase {
        return Room
            .databaseBuilder(
                context,
                CategoryDatabase::class.java,
                CategoryDatabase.DATABASE_NAME
            )
            .build()
    }
}