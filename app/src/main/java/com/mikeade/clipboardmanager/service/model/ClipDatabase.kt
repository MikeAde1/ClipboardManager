package com.mikeade.clipboardmanager.service.model

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.*
import android.content.Context
import kotlinx.coroutines.CoroutineScope

@Database (entities = [ClipboardEntity::class], version = 5, exportSchema = false)
@TypeConverters(Converter::class)

abstract class ClipDatabase : RoomDatabase() {
    abstract fun clipBoardDao() :ClipBoardDao

    companion object{

        private var instance: ClipDatabase? = null

        fun getInstance(context: Context): ClipDatabase? {
            if (instance == null) {
                synchronized(ClipDatabase::class) {
                    // 2 threads can be running at the same time
                    //for instantiating room database when it is empty, this makes it always
                    //and always non null
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ClipDatabase::class.java,
                        "notes_database"
                    ).fallbackToDestructiveMigration().
                        //addCallback(roomCallback).
                    build()

                }
            }
            return instance;

        }
    }

    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        /**
         * Override the onOpen method to populate the database.
         * For this sample, we clear the database every time it is created or opened.
         */
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            // If you want to keep the data through app restarts,
            // comment out the following line.
            /*instance?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.wordDao())
                }*/
        }

    }

}



/**
 * Populate the database in a new coroutine.
 * If you want to start with more words, just add them.
 */
fun populateDatabase(wordDao: ClipBoardDao) {
    // Start the app with a clean database every time.
    // Not needed if you only populate on creation.
/*        wordDao.delete(ClipboardEntity(7,"klkj"))

        var word = ClipboardEntity(1,"Hello")
        wordDao.insert(word)
        word = Word("World!")
        wordDao.insert(word)*/
}



