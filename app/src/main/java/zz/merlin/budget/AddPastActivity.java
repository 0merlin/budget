package zz.merlin.budget;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import faranjit.currency.edittext.CurrencyEditText;
import zz.merlin.budget.data.ArrayAdapterWithIcon;
import zz.merlin.budget.data.Category;
import zz.merlin.budget.data.Data;
import zz.merlin.budget.data.Shared;

public class AddPastActivity extends AppCompatActivity {
    CurrencyEditText spend;
    EditText extra;
    Spinner spinner;
    DatePicker date;
    TimePicker time;
    ArrayList<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_past);

        spend = (CurrencyEditText) findViewById(R.id.edit_currency);
        extra = (EditText) findViewById(R.id.extra_content);
        spinner = (Spinner) findViewById(R.id.category_choice);
        date = (DatePicker) findViewById(R.id.select_date);
        time = (TimePicker) findViewById(R.id.select_time);

        categories = new Data(this).getCategories();

        String[] items = new String[categories.size()];
        int[] icons = new int[categories.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = categories.get(i).name;
            icons[i] = Shared.icons[categories.get(i).icon];
        }
        spend.setLocale(Shared.getSavedLocale(this));

        spinner.setAdapter(new ArrayAdapterWithIcon(this, items, icons));

        findViewById(R.id.complete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runComplete();
            }
        });
    }

    private void runComplete() {
        double s;

        try {
            s = spend.getCurrencyDouble();
        } catch (ParseException | NumberFormatException e) {
            spend.setError("This is required");
            Toast.makeText(this, "Amount is required", Toast.LENGTH_LONG);
            return;
        }
        String text = extra.getText().toString();

        int category = categories.get(spinner.getSelectedItemPosition()).id;
        Calendar when = Calendar.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            when.set(date.getYear(), date.getMonth(), date.getDayOfMonth(), time.getHour(), time.getMinute());
        else
            //noinspection deprecation
            when.set(date.getYear(), date.getMonth(), date.getDayOfMonth(), time.getCurrentHour(), time.getCurrentMinute());

        if (when.getTimeInMillis() > System.currentTimeMillis())
            Toast.makeText(this, "Cannot set time in the future", Toast.LENGTH_LONG).show();
        else {
            new Data(this).insertMessage(s, category, text, when.getTimeInMillis());
            finish();
        }
    }
}
