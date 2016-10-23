package com.nodhan.exp6;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

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
        Log.d("rowCount", rowCount + "");
        int columnCount = cursor.getColumnCount() - 2;
        Log.d("columnCount", columnCount + "");

        data = new String[rowCount][columnCount];
        if (cursor.moveToFirst()) {
            for (int i = 0; i < rowCount; i++) {
                data[i][0] = cursor.getString(0);
                data[i][1] = cursor.getString(1);
                data[i][2] = cursor.getString(2) + "/" + cursor.getString(3) + "/" + cursor.getString(4);
                data[i][3] = cursor.getString(5);
                Log.d("DATA", data[i][0] + " " + data[i][1] + " " + data[i][2] + " " + data[i][3]);
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
                        textViews[j].setText(new StringBuilder().append(arr[j]).append(data[i][j]));
                    } else {
                        textViews[j - 1].setText(new StringBuilder().append(arr[j - 1]).append(data[i][j]));
                        textViews[j].setText("");
                    }
                    textViews[j].setTextSize(20);
                    row[j].addView(textViews[j]);
                    row[j].setId(Integer.parseInt(data[i][0]));
                    row[j].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            generateSMS(v.getId());
                        }
                    });
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

    private void generateSMS(int id) {

        Cursor dataId = dbHandler.getData(id);
        dataId.moveToFirst();
        int years = Calendar.getInstance().get(Calendar.YEAR) - dataId.getInt(4);
        String yearth;
        switch (years) {
            case 1:
                yearth = "1st";
                break;
            case 2:
                yearth = "2nd";
                break;
            case 3:
                yearth = "3rd";
                break;
            default:
                yearth = years + "th";
        }
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);

        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", new String("+91" + dataId.getString(5)));
        smsIntent.putExtra("sms_body", "Congratulations on surviving an year! Happy " + yearth + " Birthday, " + dataId.getString(1) + "!");

        try {
            startActivity(smsIntent);
            finish();
            Log.i("Finished sending SMS...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(),
                    "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

}
