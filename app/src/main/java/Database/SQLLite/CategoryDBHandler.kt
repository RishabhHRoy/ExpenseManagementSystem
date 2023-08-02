package Database.SQLLite

import CustomErrors.CustomException
import Models.Category
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class CategoryDBHandler(context: Context, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {
    private val TABLE_CATEGORY = "CATEGORIES"

    private val CREATE_CATEGORY = (
            "CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORY
            + "(_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "TITLE TEXT UNIQUE NOT NULL, "
            + "DESCRIPTION TEXT);")

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_CATEGORY)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if(newVersion > 1){
            db.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORY")
        }

        onCreate(db)
    }

    companion object {
        val COLUMN_ID = "_ID"
        val COLUMN_TITLE = "TITLE"
        val COLUMN_DESC = "DESCRIPTION"
    }

    fun insert(category: Category){
        val values = ContentValues().apply {
            put(COLUMN_TITLE, category.title)
            put(COLUMN_DESC, category.description)
        }

        val db = this.writableDatabase

        try{
            db.insert(TABLE_CATEGORY, null, values)

            db.close()
        }catch (e: SQLiteException){
            throw CustomException("Error on item creation: ${e.message}")
        }
    }

    @SuppressLint("Range")
    fun read(id: String): ArrayList<Category> {
        val categories = ArrayList<Category>()
        val db = this.writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT * FROM $TABLE_CATEGORY WHERE $COLUMN_ID='$id'", null)
        } catch (e: SQLiteException) {
            // if table not yet present, create it
            db.execSQL(CREATE_CATEGORY)
            return ArrayList()
        }

        var title: String
        var description: String
        //var id: Int
        var id: String

        try{
            if (cursor!!.moveToFirst()) {
                while (!cursor.isAfterLast) {
                    title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE))
                    description = cursor.getString(cursor.getColumnIndex(COLUMN_DESC))
                    //id = cursor.getString(cursor.getColumnIndex(COLUMN_ID)).toInt()
                    id = cursor.getString(cursor.getColumnIndex(COLUMN_ID))
                    categories.add(Category(title, description, id))
                    cursor.moveToNext()
                }
            }
        }catch (e: SQLiteException){
            throw CustomException("Error on item reading: ${e.message}")
        }

        return categories
    }

    @SuppressLint("Range")
    fun readAll(): ArrayList<Category> {
        val categories = ArrayList<Category>()
        val db = this.writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT * FROM $TABLE_CATEGORY", null)
        } catch (e: SQLiteException) {
            // if table not yet present, create it
            db.execSQL(CREATE_CATEGORY)
            return ArrayList()
        }

        var title: String
        var description: String
        //var id: Int
        var id: String

        try{
            if (cursor!!.moveToFirst()) {
                while (!cursor.isAfterLast) {
                    title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE))
                    description = cursor.getString(cursor.getColumnIndex(COLUMN_DESC))
                    //id = cursor.getString(cursor.getColumnIndex(COLUMN_ID)).toInt()
                    id = cursor.getString(cursor.getColumnIndex(COLUMN_ID))
                    categories.add(Category(title, description, id))
                    cursor.moveToNext()
                }
            }
        }catch (e: SQLiteException){
            throw CustomException("Error on item reading: ${e.message}")
        }

        return categories
    }

    fun update(id: String, category: Category){
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_TITLE, category.title)
            put(COLUMN_DESC, category.description)
        }

        val selection = COLUMN_ID + " LIKE ?"
        val selectionArgs = arrayOf(id)
        try{
            val count = db.update(
                TABLE_CATEGORY,
                values,
                selection,
                selectionArgs)
        }catch (e: SQLiteException){
            throw CustomException("Error on item update: ${e.message}")
        }
    }

    fun delete(id: String){
        val db = this.writableDatabase

        val selection = COLUMN_ID + " LIKE ?"

        val selectionArgs = arrayOf(id)

        try{
            db.delete(TABLE_CATEGORY, selection, selectionArgs)
        }catch (e: SQLiteException){
            throw CustomException("Error on item deletion: ${e.message}")
        }
    }
}
