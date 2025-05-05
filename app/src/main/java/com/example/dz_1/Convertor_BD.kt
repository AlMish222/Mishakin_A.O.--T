package com.example.dz_1

import androidx.room.TypeConverter

class Convertor_BD {

    @TypeConverter
    fun fromItemType(type: EnumClass_BD): String = type.name

    @TypeConverter
    fun toItemType(value: String): EnumClass_BD = EnumClass_BD.valueOf(value)
}