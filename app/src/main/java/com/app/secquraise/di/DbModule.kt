package com.app.secquraise.di

import android.content.Context
import androidx.room.Room
import com.app.secquraise.db.MyDao
import com.app.secquraise.db.MyDataBase
import com.app.secquraise.util.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DbModule {
    @Provides
    @Singleton
    fun provideDao(@ApplicationContext context:Context):MyDao{
        return Room.databaseBuilder(context,MyDataBase::class.java,Constant.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build().getDao()
    }
}