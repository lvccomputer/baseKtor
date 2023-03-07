package android.ncdev.core_db.di

import android.ncdev.core_db.repository.GirlDbRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RealmModule {
    @Provides
    @Singleton
    fun provideGirlDBRepository() = GirlDbRepository()

}