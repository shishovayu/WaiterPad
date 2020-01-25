package com.example.waiterpad;//package ru.startandroid.develop.p0341simplesqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TestClass extends AppCompatActivity implements OnClickListener {

    TableLayout tableMenu;


    final String LOG_TAG = "myLogs";

    Button btnAdd, btnRead, btnClear;
    EditText etName;
    EditText etPrice;


    DBHelper dbHelper;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        tableMenu = findViewById(R.id.menuTable);


        etName = (EditText) findViewById(R.id.etName);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnRead = (Button) findViewById(R.id.btnRead);
        btnRead.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        etPrice = (EditText) findViewById(R.id.etPrice);

        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(this);
    }


    @SuppressLint("ResourceType")
    @Override
    public void onClick(View v) {

        // создаем объект для данных
        ContentValues cv = new ContentValues();

        // получаем данные из полей ввода
        String name = etName.getText().toString();
        String price = etPrice.getText().toString();

        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        switch (v.getId()) {
            case R.id.btnAdd:
                Log.d(LOG_TAG, "--- Insert in menu_items: ---");
                // подготовим данные для вставки в виде пар: наименование столбца - значение

                cv.put("name", name);
                cv.put("price", price);
                // вставляем запись и получаем ее ID
                long rowID = db.insert("menu_items", null, cv);
                Log.d(LOG_TAG, "row inserted, ID = " + rowID);
                break;
            case R.id.btnRead:


                Log.d(LOG_TAG, "--- Rows in menu_items: ---");
                // делаем запрос всех данных из таблицы mytable, получаем Cursor
                Cursor c = db.query("menu_items", null, null, null, null, null, null);

                // ставим позицию курсора на первую строку выборки
                // если в выборке нет строк, вернется false
                if (c.moveToFirst()) {

                    // определяем номера столбцов по имени в выборке
                    int idColIndex = c.getColumnIndex("id");
                    int nameColIndex = c.getColumnIndex("name");
                    int priceColIndex = c.getColumnIndex("price");

                    do {
                        // получаем значения по номерам столбцов и пишем все в лог
                        Log.d(LOG_TAG,
                                "ID = " + c.getInt(idColIndex) +
                                        ", name = " + c.getString(nameColIndex) +
                                        ", price = " + c.getString(priceColIndex));
                        // переход на следующую строку
                        // а если следующей нет (текущая - последняя), то false - выходим из цикла

                        // Добавляем tableRow в TableLayout на основном экране
                        TableRow tableRow = new TableRow(this);

                        tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tableMenu.setStretchAllColumns(true);

                        final TextView title;
                        TextView cost;
                        //Создаем элементы tableRow
                        title = new TextView(this);
                        cost = new TextView(this);
                        Button decrementBtn = new Button(this);

                        //Присваеваем значения из request
                        title.setText(c.getString(nameColIndex));
                        cost.setText(c.getString(priceColIndex));
                        decrementBtn.setText("remove");


                        //задаем LayoutParams
                        title.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        title.setId(1);
                        cost.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        cost.setId(2);
                        decrementBtn.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        decrementBtn.setId(3);
                        /*incrementBtn.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        incrementBtn.setId(4);*/

                        //добавляем элементы в tableRow
                        tableRow.addView(title);
                        tableRow.addView(cost);
                        tableRow.addView(decrementBtn);
                        tableMenu.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

                        decrementBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                               // final View colTableRow = v.findViewById(3);
                                Log.d(LOG_TAG, "--- Remove menu_item: ---");
                                //String name = title.getText().toString();
                                SQLiteDatabase db = dbHelper.getWritableDatabase();
                                int removeItem = db.delete("menu_items", "name" + "=" + "'" + title.getText().toString()+"'", null);
                                Log.d(LOG_TAG, "row deleted? = " + removeItem);
                            }
                        });
                    } while (c.moveToNext());
                } else
                    Log.d(LOG_TAG, "0 rows");
                c.close();
                break;
            case R.id.btnClear:
                Log.d(LOG_TAG, "--- Clear menu_items: ---");
                // удаляем все записи меню
                int clearCount = db.delete("menu_items", null, null);
                Log.d(LOG_TAG, "deleted rows count = " + clearCount);
                break;
        }
        // закрываем подключение к БД
        dbHelper.close();
    }



    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            // конструктор суперкласса
            super(context, "myDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "--- onCreate database ---");
            // создаем таблицу с полями
            db.execSQL("create table mytable ("
                    + "id integer primary key autoincrement,"
                    + "name text,"
                    + "price text" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

}
