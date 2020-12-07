package com.example.ta_1818007;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String database_name = "db_spesifikasi";
    public static final String table_name = "tabel_spesifikasi";

    public static final String row_id = "_id";
    public static final String row_nomor = "Nomor";
    public static final String row_nama = "Nama";
    public static final String row_mk = "MK";
    public static final String row_jaringan = "Jaringan";
    public static final String row_tglRilis = "Tanggal";
    public static final String row_warna = "Warna";
    public static final String row_ram = "Ram";
    public static final String row_rom = "Rom";
    public static final String row_battery = "Battery";
    public static final String row_harga = "Harga";
    public static final String row_foto = "Foto";

    private SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, database_name, null, 2);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + table_name + "(" + row_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + row_nomor + " TEXT, " + row_nama + " TEXT, " + row_mk + " TEXT, "
                + row_jaringan + " TEXT, " + row_tglRilis + " TEXT, " + row_warna + " TEXT, "
                + row_ram+ " TEXT, " + row_rom + " TEXT, " + row_battery + " TEXT, " + row_harga + " TEXT, " + row_foto + " TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int x) {
        db.execSQL("DROP TABLE IF EXISTS " + table_name);
    }

    //Get All SQLite Data
    public Cursor allData(){
        Cursor cur = db.rawQuery("SELECT * FROM " + table_name , null);
        return cur;
    }

    //Get 1 Data By ID
    public Cursor oneData(Long id){
        Cursor cur = db.rawQuery("SELECT * FROM " + table_name + " WHERE " + row_id + "=" + id, null);
        return cur;
    }

    //Insert Data to Database
    public void insertData(ContentValues values){
        db.insert(table_name, null, values);
    }

    //Update Data
    public void updateData(ContentValues values, long id){
        db.update(table_name, values, row_id + "=" + id, null);
    }

    //Delete Data
    public void deleteData(long id){
        db.delete(table_name, row_id + "=" + id, null);
    }
}
