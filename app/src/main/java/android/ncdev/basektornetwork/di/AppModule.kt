package android.ncdev.basektornetwork.di

import android.content.Context
import android.content.SharedPreferences
import coil.ImageLoader
import coil.decode.SvgDecoder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideSharePreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    }
    @Provides
    @Singleton
    fun imageLoader(@ApplicationContext context: Context): ImageLoader =
        ImageLoader.Builder(context)
            .componentRegistry {
                add(SvgDecoder(context))
            }
            .build()
}