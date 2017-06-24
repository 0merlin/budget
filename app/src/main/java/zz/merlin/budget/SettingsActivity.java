package zz.merlin.budget;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.Locale;

import faranjit.currency.edittext.CurrencyEditText;
import zz.merlin.budget.data.Shared;

public class SettingsActivity extends AppCompatActivity {
    Spinner month_start, localeSetting;
    CurrencyEditText month_spend;
    ArrayList<Locale> locales = new ArrayList<>();
    String monthDays[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        month_start = (Spinner) findViewById(R.id.choose_month_start);
        localeSetting = (Spinner) findViewById(R.id.choose_locale);
        month_spend = (CurrencyEditText) findViewById(R.id.monthly_spend);
        monthDays = getResources().getStringArray(R.array.month_start);


        for (String valid : Shared.supportedLocales) {
            try {
                locales.add(Locale.forLanguageTag(valid));
            } catch (Exception ignored) {
            }
        }

        Collections.sort(locales, new Comparator<Locale>() {
            @Override
            public int compare(Locale o1, Locale o2) {
                if (o1.getDisplayCountry().isEmpty() && !o2.getDisplayCountry().isEmpty()) return 1;
                if (!o1.getDisplayCountry().isEmpty() && o2.getDisplayCountry().isEmpty())
                    return -1;
                return localeString(o1).compareTo(localeString(o2));
            }
        });

        String[] localeNames = new String[locales.size()];
        for (int i = 0; i < locales.size(); i++) {
            localeNames[i] = localeString(locales.get(i));
        }

        ArrayAdapter<String> adp = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, localeNames);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        localeSetting.setAdapter(adp);


        localeSetting.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                month_spend.setLocale(locales.get(position));
                Shared.set(SettingsActivity.this, Shared.SAVED_LOCALE, locales.get(position).toLanguageTag());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                month_spend.setLocale(Shared.getSavedLocale(SettingsActivity.this));
            }
        });

        Locale d = Shared.getSavedLocale(this);
        int i = 0;
        for (Locale locale : locales) {
            if (locale.toLanguageTag().equals(d.toLanguageTag())) {
                localeSetting.setSelection(i);
                break;
            }
            i++;
        }
        month_spend.setLocale(d);
        month_spend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    Shared.set(SettingsActivity.this, Shared.SAVED_SPEND, (float) month_spend.getCurrencyDouble());
                } catch (ParseException ignored) {
                }
            }
        });
        month_spend.setText(String.format(Shared.getSavedLocale(this), "%.02f", Shared.get(this, Shared.SAVED_SPEND, 0.0f)));

        month_start.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Shared.set(SettingsActivity.this, Shared.SAVED_MONTH_START, monthDays[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Shared.set(SettingsActivity.this, Shared.SAVED_MONTH_START, monthDays[0]);
            }
        });
        String s = "" + Shared.getStartDay(this);
        for (int j = 0; j < monthDays.length; j++) {
            if (s.equals(monthDays[j])) {
                month_start.setSelection(j);
                break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String localeString(Locale locale) {
        return locale.getDisplayCountry() + ": " + Currency.getInstance(locale).getDisplayName();
    }
}
