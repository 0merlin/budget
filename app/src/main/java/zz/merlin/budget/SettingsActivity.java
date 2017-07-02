package zz.merlin.budget;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import faranjit.currency.edittext.CurrencyEditText;
import zz.merlin.budget.data.Category;
import zz.merlin.budget.data.Data;
import zz.merlin.budget.data.Shared;
import zz.merlin.budget.data.Transaction;

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


        Locale[] locales1 = Locale.getAvailableLocales();
        for (Locale locale : locales1) {
            try {
                Log.e("Locales", locale.toLanguageTag() + " - " + Currency.getInstance(locale).getDisplayName() + " - " + Currency.getInstance(locale).getCurrencyCode());
            } catch (Exception ignored) {
            }
        }


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


        findViewById(R.id.run_backup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.enableBackup)
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            doBackup();
                        }
                    }).start();
                else
                    Toast.makeText(SettingsActivity.this, "You disabled backup, try restarting the app to try again", Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.run_export).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.enableBackup)
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            doExport();
                        }
                    }).start();
                else
                    Toast.makeText(SettingsActivity.this, "You disabled backup, try restarting the app to try again", Toast.LENGTH_LONG).show();
            }
        });
        findViewById(R.id.run_restore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.enableBackup) {
                    new AlertDialog.Builder(SettingsActivity.this)
                            .setTitle("Are you sure you want to restore")
                            .setMessage("Are you very sure you want to restore the latest backup, this will clear the internal database, and restore the latest backup found?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Toast.makeText(SettingsActivity.this, "Running import", Toast.LENGTH_LONG).show();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            doImport();
                                        }
                                    }).start();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                } else
                    Toast.makeText(SettingsActivity.this, "You disabled backup, try restarting the app to try again", Toast.LENGTH_LONG).show();
            }
        });
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


    /**
     * Create a JSON backup file on the SD card. Currently only stores the transactions,
     * and not the application settings.
     */
    public void doBackup() {

        ArrayList<Transaction> transactions = new Data(this).getTransactionsAfter(0);
        ArrayList<Category> categories = new Data(this).getCategories();
        File path = Environment.getExternalStorageDirectory();

        final File file = new File(path, "budget-" + Shared.date_full.format(new Date()) + ".json");
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(file, false);
            JsonObject backup = new JsonObject();


            JsonArray jsonTransactions = new JsonArray();
            for (Transaction transaction : transactions) jsonTransactions.add(transaction.json());

            JsonArray jsonCategories = new JsonArray();
            for (Category category : categories) jsonCategories.add(category.json());

            backup.add("transactions", jsonTransactions);
            backup.add("categories", jsonCategories);
            backup.addProperty("month_start", Shared.getStartDay(this));
            backup.addProperty("locale", Shared.getSavedLocale(this).toLanguageTag());
            backup.addProperty("spendable", Shared.getSavedSpend(this));

            stream.write(backup.toString().getBytes());
            stream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stream != null) stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "File written: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Find an export that can be imported.
     */
    public void doImport() {
        String path = Environment.getExternalStorageDirectory().toString();
        File directory = new File(path);
        File[] files = directory.listFiles();
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        File chosen = null;
        for (File file : files) {
            String name = file.getName();
            if (name.startsWith("budget-") && name.endsWith(".json"))
                chosen = file;
        }

        if (chosen == null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "No backup file found", Toast.LENGTH_LONG).show();
                }
            });
            return;
        }

        try {
            FileReader fr = new FileReader(chosen);
            JsonObject jsonObject = new JsonParser().parse(fr).getAsJsonObject();

            JsonArray categories = jsonObject.getAsJsonArray("categories");
            JsonArray transactions = jsonObject.getAsJsonArray("transactions");

            Shared.set(this, Shared.SAVED_MONTH_START, String.valueOf(jsonObject.get("month_start").getAsInt()));
            Shared.set(this, Shared.SAVED_SPEND, jsonObject.get("spendable").getAsFloat());
            Shared.set(this, Shared.SAVED_LOCALE, jsonObject.get("locale").getAsString());

            Data data = new Data(this);
            data.resetDatabase();

            for (int i = 0; i < categories.size(); i++) {
                Category category = new Category(categories.get(i).getAsJsonObject());
                data.insertCategory(category);
            }
            for (int i = 0; i < transactions.size(); i++) {
                Transaction transaction = new Transaction(transactions.get(i).getAsJsonObject());
                data.insertTransaction(transaction);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        final File finalChosen = chosen;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Backup read from: " + finalChosen.getAbsolutePath(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Run the default export functionality.
     */
    public void doExport() {
        ArrayList<Transaction> transactions = new Data(this).getTransactionsAfter(0);
        File path = Environment.getExternalStorageDirectory();

        final File file = new File(path, "budget-" + Shared.date_full.format(new Date()) + ".csv");
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(file, false);
            for (Transaction transaction : transactions) {
                String sb = String.format(Locale.ENGLISH,
                        "%s,%f,%s,\"%s\"\n",
                        Shared.date_full.format(transaction.date),
                        transaction.value,
                        transaction.category.name,
                        transaction.comment.replaceAll("\"", "").replaceAll("\n", ""));
                stream.write(sb.getBytes());
            }
            stream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stream != null) stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "File written: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
