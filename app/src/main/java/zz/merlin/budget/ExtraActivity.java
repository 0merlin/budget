package zz.merlin.budget;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import zz.merlin.budget.data.Category;
import zz.merlin.budget.data.Data;
import zz.merlin.budget.data.Shared;
import zz.merlin.budget.widgets.Helper;

public class ExtraActivity extends AppCompatActivity {

    EditText extra;
    double spent;
    int category;
    Calendar now;
    Date date;
    Button changeDate, changeTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            spent = extras.getDouble(Shared.SPENT, 0.0);
            category = extras.getInt(Shared.CATEGORY, 0);
        } else {
            Toast.makeText(this, "Dumb-Ass", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        now = Calendar.getInstance();
        date = new Date(now.getTimeInMillis());

        extra = (EditText) findViewById(R.id.extra_content);

        ArrayList<Category> categories = new Data(this).getCategories();
        for (Category category1: categories) {
            if (category1.id == category) {
                ((TextView)findViewById(R.id.spend_summary)).setText(String.format(Locale.ENGLISH, "Spending %s in %s", Shared.currencyFormat(this, spent), category1.name));
                break;
            }
        }

        changeDate = (Button) findViewById(R.id.change_date);
        changeTime = (Button) findViewById(R.id.change_time);
        changeDate.setText("Click to change date from: " + Shared.date.format(date));
        changeTime.setText("Click to change time from: " + Shared.time.format(date));

        changeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ExtraActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        now.set(year, month, dayOfMonth);
                        date = new Date(now.getTimeInMillis());
                        changeDate.setText("Click to change date from: " + Shared.date.format(date));
                    }
                }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        changeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(ExtraActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        now.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        now.set(Calendar.MINUTE, minute);
                        date = new Date(now.getTimeInMillis());
                        changeTime.setText("Click to change time from: " + Shared.time.format(date));
                    }
                }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE),
                        DateFormat.is24HourFormat(ExtraActivity.this)).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.last_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.complete_entry:
                new Data(getApplicationContext()).createTransaction(spent, category, extra.getText().toString(), date.getTime());
                Helper.updateMyWidgets(getApplicationContext());
                startActivity(new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY));
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
