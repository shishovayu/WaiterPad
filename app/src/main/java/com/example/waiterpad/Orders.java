package com.example.waiterpad;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Orders extends AppCompatActivity {

    private SQLiteOpenHelper dbHelper;
    final String LOG_TAG = "myLogs";

    TableLayout tableOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        dbHelper = new DBHelper(this);

        tableOrders = findViewById(R.id.tableOrders);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        final Cursor c = db.query("orders", null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int NameColIndex = c.getColumnIndex("item_name");
            int amountColIndex = c.getColumnIndex("amount");

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d(LOG_TAG,
                        "ID = " + c.getInt(idColIndex) +
                                ", item_name = " + c.getString(NameColIndex) +
                                ", amount = " + c.getString(amountColIndex));
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла


                // Добавляем tableRow в TableLayout на основном экране
                TableRow tableRow = new TableRow(this);

                tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                tableOrders.setStretchAllColumns(true);

                TextView title;
                final TextView amount;
                //Создаем элементы tableRow
                title = new TextView(this);
                amount = new TextView(this);


                //Присваеваем значения из request
                title.setText(c.getString(NameColIndex));
                amount.setText("0");

//                //задаем LayoutParams
//                title.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
//                title.setId(1);
//                amount.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
//                amount.setId(2);


                //добавляем элементы в tableRow
                tableRow.addView(title);
                tableRow.addView(amount);
                tableOrders.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));


            } while (c.moveToNext());

        } else
            Log.d(LOG_TAG, "0 rows");
        c.close();
    }
}
