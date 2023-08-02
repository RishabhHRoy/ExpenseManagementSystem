package Database.SQLLite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {

    private val TABLE_CATEGORY = "CATEGORIES"
    private val TABLE_BUDGET = "BUDGETS"
    private val TABLE_EXPENSE = "EXPENSES"

    // Creating table query
    private val CREATE_CATEGORY = ("CREATE TABLE " + TABLE_CATEGORY
            + "(_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "TITLE TEXT NOT NULL, "
            + "DESCRIPTION TEXT);")

    private val CREATE_BUDGET = ("CREATE TABLE " + TABLE_BUDGET
            + "(_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "TITLE TEXT NOT NULL, "
            + "DESCRIPTION TEXT,"
            + "BUDGET DECIMAL(10, 2),"
            + "CATEGORY_ID INTEGER NOT NULL,"
            + "FOREIGN KEY(CATEGORY_ID) REFERENCES " + TABLE_CATEGORY + "(_ID)"
            + "ON UPDATE ACTION"
            + "ON DELETE ACTION"
            + ");")

    private val CREATE_EXPENSE = ("CREATE TABLE " + TABLE_EXPENSE
            + "(_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "TITLE TEXT NOT NULL, "
            + "COST DECIMAL(10,2),"
            + "CATEGORY_ID INTEGER NOT NULL,"
            + "FOREIGN KEY(CATEGORY_ID) REFERENCES " + TABLE_CATEGORY + "(_ID)"
            + "ON UPDATE ACTION"
            + "ON DELETE ACTION"
            + ");")

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_CATEGORY)
        db.execSQL(CREATE_BUDGET)
        db.execSQL(CREATE_EXPENSE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORY")

        db.execSQL("DROP TABLE IF EXISTS $TABLE_BUDGET")

        db.execSQL("DROP TABLE IF EXISTS $TABLE_EXPENSE")

        onCreate(db)
    }
}