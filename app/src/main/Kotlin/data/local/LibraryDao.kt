package data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface LibraryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(items: UniversalItemClass_BD)

    @Update
    suspend fun update(items: UniversalItemClass_BD)

    @Delete
    suspend fun delete(items: UniversalItemClass_BD)

    @Query("DELETE FROM library_items")
    suspend fun deleteAll()

    @Query("DELETE FROM library_items WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM library_items")
    suspend fun getAll(): List<UniversalItemClass_BD>

    @Query("SELECT * FROM library_items ORDER BY name ASC LIMIT :limit OFFSET :offset")
    suspend fun getAllSortedByName(limit: Int, offset: Int): List<UniversalItemClass_BD>

    @Query("SELECT * FROM library_items ORDER BY id ASC LIMIT :limit OFFSET :offset")
    suspend fun getAllSortedById(limit: Int, offset: Int): List<UniversalItemClass_BD>


}