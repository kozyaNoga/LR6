package com.example.lr6;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "lr6_database_v2.db";
    private static final String ASSET_DB_NAME = "lr6_db.db";
    private static final int DB_VERSION = 1;

    private final Context myContext;
    private final String dbPath;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.myContext = context;
        this.dbPath = context.getDatabasePath(DB_NAME).getPath();
    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();

        if (!dbExist) {
            this.getReadableDatabase();
            this.close();

            try {
                copyDataBase();
                Log.d("DBHelper", "База данных успешно скопирована");
            } catch (IOException e) {
                throw new Error("Ошибка копирования базы данных");
            }
        }
    }

    private boolean checkDataBase() {
        File dbFile = new File(dbPath);
        // Если файл меньше 20КБ, считаем его пустой системной заглушкой
        if (dbFile.exists() && dbFile.length() < 20000) {
            dbFile.delete();
            return false;
        }
        return dbFile.exists();
    }

    private void copyDataBase() throws IOException {
        InputStream myInput = myContext.getAssets().open(ASSET_DB_NAME);

        // Создаем выходной файл и папку, если нужно
        File outFile = new File(dbPath);
        if (!outFile.getParentFile().exists()) {
            outFile.getParentFile().mkdirs();
        }

        OutputStream myOutput = new FileOutputStream(dbPath);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    // Теперь передаем только имя таблицы, колонки будем писать внутри
    public ArrayList<String> getAllItems(String tableName) {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            // Запрашиваем НЕСКОЛЬКО колонок (например, name и price)
            // Замените 'name' и 'Price' на ваши реальные названия колонок!
            String query = "SELECT name, discription, price FROM " + tableName;

            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    // Берем данные по номеру колонки (0 - name, 1 - Price)
                    String name = cursor.getString(0);
                    String disc = cursor.getString(1);
                    String price = cursor.getString(2);

                    // Склеиваем их в одну красивую строку
                    String result = name + " — " + disc + " - " + price + " руб.";

                    list.add(result);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("DBHelper", "Ошибка: " + e.getMessage());
        }
        return list;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
