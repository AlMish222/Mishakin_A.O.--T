package data.local

import androidx.room.TypeConverter

class Convertor_BD {

    @TypeConverter
    fun fromItemType(type: TypeOfElement): String = type.name

    @TypeConverter
    fun toItemType(value: String): TypeOfElement = TypeOfElement.valueOf(value)
}