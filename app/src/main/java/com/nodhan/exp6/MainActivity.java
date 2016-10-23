package com.nodhan.exp6;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TableLayout tableLayout;
    DBHandler dbHandler;
    TextView[] textViews;
    String[][] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }

    public void setData() {
        tableLayout = (TableLayout) findViewById(R.id.scrollTable); //Finding ScrollView
        tableLayout.removeAllViews();

        dbHandler = new DBHandler(this, "birthday", null, 1);
        
        Cursor cursor = dbHandler.display();
        int rowCount = cursor.getCount();
        Log.d("rowCount", rowCount+"");
        int columnCount = cursor.getColumnCount();
        Log.d("columnCount", columnCount+"");
        
        data = new String[rowCount][columnCount];
        if (cursor.moveToFirst()) {
            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < columnCount; j++) {
                    data[i][j] = cursor.getString(j);
                    Log.d("DATA[" + i + "][" + j + "]", data[i][j]);
                }
                cursor.moveToNext();
            }
        }
        
        
        textViews = new TextView[rowCount + 1]; //TextView array
        
        if (data.length > 0) {
            for (int i = 0; i < data.length; i++) {

                TableRow row[] = new TableRow[rowCount + 1]; //Row in TableLayout
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);

                String arr[] = {"Name : \t\t\t\t\t\t\t\t\t", "DOB : \t\t\t\t\t\t", "Number : \t\t\t\t\t\t"};

                //Init row and TextView and setting the data
                for (int j = 0; j <= rowCount; j++) {
                    row[j] = new TableRow(this);
                    row[j].setLayoutParams(layoutParams);
                    textViews[j] = new TextView(this);
                    if (j != rowCount) {
                        textViews[j].setText(arr[j] + data[i][j]);
                    } else {
                        textViews[j].setText("");
                    }
                    textViews[j].setTextSize(20);
                    row[j].addView(textViews[j]);
                    tableLayout.addView(row[j]);
                }
            }
        } else {
            TableRow row = new TableRow(this); //Row in TableLayout
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            row.setLayoutParams(layoutParams);
            TextView view = new TextView(this);
            view.setText("No data found!\nStart adding details by clicking the button below!");
            view.setTextSize(20);
            row.addView(view);
            tableLayout.addView(row);
        }

    }

}
