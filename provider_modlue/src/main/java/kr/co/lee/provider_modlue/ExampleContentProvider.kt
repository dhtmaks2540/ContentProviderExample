package kr.co.lee.provider_modlue

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri

class ExampleContentProvider : ContentProvider() {
    lateinit var sqliteDB: SQLiteDatabase

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val id = sqliteDB.delete("tb_data", selection, selectionArgs)
        return id
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        sqliteDB.insert("tb_data", null, values)
        return null
    }

    override fun onCreate(): Boolean {
        val helper = DBHelper(context!!)
        sqliteDB = helper.writableDatabase
        return false
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val cursor = sqliteDB.query("tb_data", projection, selection, selectionArgs, null, null, sortOrder)
        return cursor
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val id = sqliteDB.update("tb_data", values, selection, selectionArgs)
        return id
    }
}