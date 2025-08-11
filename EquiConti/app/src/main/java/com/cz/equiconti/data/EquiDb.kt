@Database(
    entities = [Owner::class, Horse::class, Txn::class],
    version = 4, // <-- incrementa
    exportSchema = false
)
abstract class EquiDb : RoomDatabase() {
    abstract fun ownerDao(): OwnerDao
    abstract fun horseDao(): HorseDao
    abstract fun txnDao(): TxnDao

    companion object {
        @Volatile private var INSTANCE: EquiDb? = null

        fun get(context: Context): EquiDb =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    EquiDb::class.java,
                    "equiconti.db"
                )
                    .fallbackToDestructiveMigration() // ricrea il DB se lo schema cambia
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
