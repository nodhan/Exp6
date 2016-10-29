package com.nodhan.exp6;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    static EditText date;
    EditText name, mobileNumber;
    Button add;
    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        add = (Button) findViewById(R.id.add);

        name = (EditText) findViewById(R.id.name);
        mobileNumber = (EditText) findViewById(R.id.mobile_number);
        date = (EditText) findViewById(R.id.date);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkForNull(name);
                checkForNull(mobileNumber);
                checkForNull(date);
                if (flag) {
                    DBHandler dbHandler = new DBHandler(getApplicationContext(), "birthday", null, 1);
                    String[] splitDates = date.getText().toString().split("/");
                    int[] dates = new int[splitDates.length];
                    for (int i = 0; i < splitDates.length; i++)
                        dates[i] = Integer.parseInt(splitDates[i]);
                    dbHandler.add(name.getText().toString(), dates, mobileNumber.getText().toString());
                    finish();
                } else {
                    flag = true;
                }
            }
        });
    }

    /**
     * Checks for empty or null text in EditText
     *
     * @param editText the field to be checked
     */
    void checkForNull(EditText editText) {
        String check = editText.getText().toString();
        if (check.equals("") || check.equals(null)) {
            editText.setError("Field Required");
            flag = false;
        }
    }

}
