package ucne.edu.ventas.local.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ucne.edu.ventas.local.data.dao.VentaDao
import ucne.edu.ventas.local.data.entities.VentaEntity

@Database(
    entities = [
        VentaEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class VentaDb : RoomDatabase(){
    abstract fun ventaDao(): VentaDao
}