package android.ncdev.girl_photo.di

import android.ncdev.core_db.repository.GirlDbRepository
import android.ncdev.girl_photo.data.GirlDataSource
import android.ncdev.girl_photo.data.GirlDataSourceImpl
import android.ncdev.girl_photo.network.repository.GirlPhotoRepository
import android.ncdev.girl_photo.network.services.GirlService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class GirlModule {
    @Provides
    @Singleton
    fun provideGirlService() = GirlService()

    @Provides
    @Singleton
    fun provideGirlRepository(
        girlService: GirlService,
        girlDataSource: GirlDataSource
    ) =
        GirlPhotoRepository(
            girlService,
            girlDataSource
        )

    @Provides
    @Singleton
    fun provideGirlDataSource(girlDbRepository: GirlDbRepository): GirlDataSource =
        GirlDataSourceImpl(girlDbRepository)

}