package com.nodhan.exp6;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TableLayout tableLayout;
    DBHandler dbHandler;
    TextView textView;
    String[][] data;
    Calendar calendar;

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
        calendar = Calendar.getInstance();
        tableLayout = (TableLayout) findViewById(R.id.scrollTable); //Finding ScrollView
        tableLayout.removeAllViews();

        dbHandler = new DBHandler(this, "birthday", null, 1);

        Cursor cursor = dbHandler.display();
        int rowCount = cursor.getCount();
        int columnCount = cursor.getColumnCount() - 2;

        data = new String[rowCount][columnCount];
        if (cursor.moveToFirst()) {
            for (int i = 0; i < rowCount; i++) {
                data[i][0] = cursor.getString(0);
                data[i][1] = cursor.getString(1);
                data[i][2] = cursor.getString(2) + "/" + cursor.getString(3) + "/" + cursor.getString(4);
                data[i][3] = cursor.getString(5);
                cursor.moveToNext();
            }
        }

        if (data.length > 0) {
            TableRow row[] = new TableRow[rowCount]; //Row in TableLayout
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);

            String arr[] = {"Name : \t", "DOB : \t   "};
            for (int i = 0; i < data.length; i++) {

                row[i] = new TableRow(this);
                row[i].setLayoutParams(layoutParams);

                row[i].setId(Integer.parseInt(data[i][0]));
                String date = calendar.get(Calendar.DATE) + "";
                String month = calendar.get(Calendar.MONTH) + 1 + "";
                String[] dateSplit = data[i][2].split("/");
                boolean colorFlag = dateSplit[0].equals(date) && dateSplit[1].equals(month);

                textView = new TextView(this);
                StringBuilder stringBuilder = new StringBuilder().append(arr[0]).append(data[i][1]).append("\n")
                        .append(arr[1]).append(data[i][2]).append("\n");
                if (colorFlag) {
                    row[i].setBackgroundColor(Color.BLACK);
                    textView.setTextColor(Color.WHITE);
                } else {
                    stringBuilder.append("\n");
                }
                textView.setText(stringBuilder);
                textView.setTextSize(20);
                row[i].addView(textView);

                row[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        generateSMS(v.getId());
                    }
                });
                tableLayout.addView(row[i]);
            }
        } else {
            ((TextView) findViewById(R.id.message)).setText("No data found!\nStart adding details by clicking the button below!");
        }

    }

    /**
     * Generates SMS to be sent
     *
     * @param id - the row id
     */
    private void generateSMS(int id) {

        Cursor dataId = dbHandler.getData(id);
        dataId.moveToFirst();
        String name = dataId.getString(1).toUpperCase();
        int date = calendar.get(Calendar.DATE) - dataId.getInt(2);
        int month = calendar.get(Calendar.MONTH) - dataId.getInt(3) + 1;
        if (date == 0 && month == 0) {
            int years = calendar.get(Calendar.YEAR) - dataId.getInt(4);
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

            if (years == 0) {
                Toast.makeText(this, "Really? Took birth today only!", Toast.LENGTH_SHORT).show();
            } else {
                String message = "Congratulations on surviving another year in your life! Happy " + yearth + " Birthday, " + name + "!";
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage("+91" + dataId.getString(5), null, message, null, null);
                Toast.makeText(this, "SMS sent", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, name + "\'s birthday is not today!!", Toast.LENGTH_SHORT).show();
        }
    }

}
