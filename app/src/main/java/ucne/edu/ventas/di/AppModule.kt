package ucne.edu.ventas.di

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ucne.edu.ventas.local.data.database.VentaDb
import ucne.edu.ventas.local.remote.VentasApi
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    private const val BASE_URL = "https://ventasapiaplicada.azurewebsites.net/"

    @Provides
    @Singleton
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Provides
    @Singleton
    fun provideTicketingApi(moshi: Moshi): VentasApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(VentasApi::class.java)
    }


    //ROOM
    @Provides
    @Singleton
    fun provideVentaDb(@ApplicationContext appContext: Context): VentaDb {
        return Room.databaseBuilder(
            appContext,
            VentaDb::class.java,
            "Venta.db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideVentaDao(database: VentaDb) = database.ventaDao()
}