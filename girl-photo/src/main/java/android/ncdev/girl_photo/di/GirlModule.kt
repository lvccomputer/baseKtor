package android.ncdev.girl_photo.di

import android.ncdev.girl_photo.network.services.GirlService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import javax.inject.Singleton

@InstallIn
@Module
class GirlModule {
    @Provides
    @Singleton
    fun provideGirlService() = GirlService()


}