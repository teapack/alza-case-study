package cz.vratislavjindra.alzacasestudy.dependency_injection

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import cz.vratislavjindra.alzacasestudy.feature_products.data.local.ProductDatabase
import cz.vratislavjindra.alzacasestudy.feature_products.data.remote.CategoryApi
import cz.vratislavjindra.alzacasestudy.feature_products.data.remote.ProductApi
import cz.vratislavjindra.alzacasestudy.feature_products.data.repository.CategoryRepositoryImpl
import cz.vratislavjindra.alzacasestudy.feature_products.data.repository.ProductRepositoryImpl
import cz.vratislavjindra.alzacasestudy.feature_products.domain.repository.CategoryRepository
import cz.vratislavjindra.alzacasestudy.feature_products.domain.repository.ProductRepository
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
        @ApplicationContext context: Context,
        api: CategoryApi,
        db: ProductDatabase
    ): CategoryRepository {
        return CategoryRepositoryImpl(context = context, api = api, dao = db.categoryDao)
    }

    @Provides
    @Singleton
    fun provideProductRepository(
        @ApplicationContext context: Context,
        api: ProductApi,
        db: ProductDatabase
    ): ProductRepository {
        return ProductRepositoryImpl(context = context, api = api, dao = db.productDao)
    }

    @Provides
    @Singleton
    fun provideCategoryApi(retrofit: Retrofit): CategoryApi {
        return retrofit.create(CategoryApi::class.java)
    }

    @Provides
    @Singleton
    fun provideProductApi(retrofit: Retrofit): ProductApi {
        return retrofit.create(ProductApi::class.java)
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
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
    fun provideProductDatabase(@ApplicationContext context: Context) : ProductDatabase {
        return Room
            .databaseBuilder(
                context,
                ProductDatabase::class.java,
                ProductDatabase.DATABASE_NAME
            )
            .build()
    }
}