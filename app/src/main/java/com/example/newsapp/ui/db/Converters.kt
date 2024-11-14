package com.example.newsap.ui.db


import androidx.room.TypeConverter
import com.example.newsapp.ui.models.Source
import com.example.newsapp.ui.util.Constants

class Converters {

    @TypeConverter
    fun fromSource(source:Source):String{
        return source.name
    }

    @TypeConverter
    fun toSource(name : String):Source{
        return Source(name, name)
    }



}