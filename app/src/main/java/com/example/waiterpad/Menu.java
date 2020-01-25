package com.example.waiterpad;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Menu extends AppCompatActivity {

    private SQLiteOpenHelper dbHelper;
    final String LOG_TAG = "myLogs";

    TableLayout tableMenu;


    Button createOrder;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        dbHelper = new DBHelper(this);

        tableMenu = findViewById(R.id.menuTable);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        final Cursor c = db.query("menu_items", null, null, null, null, null, null);

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

                TextView title;
                final TextView amount;
                //Создаем элементы tableRow
                title = new TextView(this);
                amount = new TextView(this);
                Button decrementBtn = new Button(this);
                final Button incrementBtn = new Button(this);

                //Присваеваем значения из request
                title.setText(c.getString(nameColIndex));
                amount.setText("0");
                decrementBtn.setText("-");
                incrementBtn.setText("+");

                //задаем LayoutParams
                title.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                title.setId(1);
                amount.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                amount.setId(2);
                decrementBtn.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                decrementBtn.setId(3);
                incrementBtn.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                incrementBtn.setId(4);

                //добавляем элементы в tableRow
                tableRow.addView(title);
                tableRow.addView(amount);
                tableRow.addView(incrementBtn);
                tableRow.addView(decrementBtn);
                tableMenu.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

                incrementBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final View colTableRow = v.findViewById(2);
                        int count = (Integer.parseInt(amount.getText().toString()));
                        count++;
                        amount.setText(String.valueOf(count));
                        //tableOrders.removeView((View) colTableRow.getParent());
                    }
                });

                decrementBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final View colTableRow = v.findViewById(3);
                        int count = (Integer.parseInt(amount.getText().toString()));

                        if(count>0) {
                            count--;
                            amount.setText(String.valueOf(count));
                        }
                    }
                });

            } while (c.moveToNext());

        } else
            Log.d(LOG_TAG, "0 rows");
        c.close();

        createOrder = findViewById(R.id.createOrder);

        createOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteDatabase db = dbHelper.getWritableDatabase();
                // создаем объект для данных
                ContentValues cv = new ContentValues();
               // Log.d(LOG_TAG, "--- Select distinct(order_number) from orders: ---");
                // подготовим данные для вставки в виде пар: наименование столбца - значение

                for(int i = 0; i<tableMenu.getChildCount();i++){


                    Log.d(LOG_TAG, "--- Insert in orders: ---");
                    // подготовим данные для вставки в виде пар: наименование столбца - значение

                    TextView bufTitle = tableMenu.getChildAt(i).findViewById(1);
                    TextView bufAmount = tableMenu.getChildAt(i).findViewById(2);

                    cv.put("order_number", "1");


                    cv.put("item_name", bufTitle.getText().toString());
                    cv.put("amount", bufAmount.getText().toString());
                    // вставляем запись и получаем ее ID
                    long rowID = db.insert("orders", null, cv);
                    Log.d(LOG_TAG, "row inserted, ID = " + rowID);

                }

            }
        });

//        tableOrders.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                .setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        final View colTableRow = v.findViewById(1);
//                        tablePoints.removeView((View) colTableRow.getParent());
//                    }
//                });
//            }
//        });



    }
}
