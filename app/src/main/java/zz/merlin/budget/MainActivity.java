package zz.merlin.budget;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
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
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import faranjit.currency.edittext.CurrencyEditText;
import zz.merlin.budget.data.Category;
import zz.merlin.budget.data.Data;
import zz.merlin.budget.data.Shared;
import zz.merlin.budget.data.Transaction;

public class MainActivity extends AppCompatActivity {

    boolean enableBackup = true;

    CurrencyEditText spent;
    ImageButton clear;
    Button done;
    TextView available;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spent = (CurrencyEditText) findViewById(R.id.edit_currency);
        clear = (ImageButton) findViewById(R.id.btn_clear);
        done = (Button) findViewById(R.id.key_done);
        available = (TextView) findViewById(R.id.available);
        findViewById(R.id.key_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spent.setText(spent.getText() + "1");
            }
        });
        findViewById(R.id.key_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spent.setText(spent.getText() + "2");
            }
        });
        findViewById(R.id.key_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spent.setText(spent.getText() + "3");
            }
        });
        findViewById(R.id.key_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spent.setText(spent.getText() + "4");
            }
        });
        findViewById(R.id.key_5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spent.setText(spent.getText() + "5");
            }
        });
        findViewById(R.id.key_6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spent.setText(spent.getText() + "6");
            }
        });
        findViewById(R.id.key_7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spent.setText(spent.getText() + "7");
            }
        });
        findViewById(R.id.key_8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spent.setText(spent.getText() + "8");
            }
        });
        findViewById(R.id.key_9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spent.setText(spent.getText() + "9");
            }
        });
        findViewById(R.id.key_0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spent.setText(spent.getText() + "0");
            }
        });
        findViewById(R.id.key_00).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spent.setText(spent.getText() + "00");
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseType();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spent.setText("");
                setNextVisible(false);
            }
        });
        spent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (spent.getText().length() == 0) return;

                try {
                    setNextVisible(spent.getCurrencyDouble() > 0);
                } catch (ParseException | NumberFormatException e) {
                    setNextVisible(false);
                }
            }
        });
        spent.setLocale(Shared.getSavedLocale(this));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        calculateSpendable();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == 1 && (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED)) {
            Toast.makeText(this, "Backup will be disabled", Toast.LENGTH_LONG).show();
            enableBackup = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        calculateSpendable();
    }

    /**
     * Whether the next button should be set visible or not.
     *
     * @param visible Visibility?
     */
    private void setNextVisible(boolean visible) {
        clear.setVisibility(visible ? View.VISIBLE : View.GONE);
        done.setEnabled(visible);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.goto_budget:
                startActivity(new Intent(this, UsageActivity.class));
                return true;
            case R.id.goto_transactions:
                startActivity(new Intent(this, TransactionsActivity.class));
                return true;
            case R.id.goto_categories:
                startActivity(new Intent(this, CategoryActivity.class));
                return true;
            case R.id.goto_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.backup:
                if (enableBackup)
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            doBackup();
                        }
                    }).start();
                else
                    Toast.makeText(this, "You disabled backup, try restarting the app to try again", Toast.LENGTH_LONG).show();
                return true;
            case R.id.export:
                if (enableBackup)
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            doExport();
                        }
                    }).start();
                else
                    Toast.makeText(this, "You disabled backup, try restarting the app to try again", Toast.LENGTH_LONG).show();
                return true;
            case R.id.restore:
                if (enableBackup) {
                    new AlertDialog.Builder(this)
                            .setTitle("Are you sure you want to restore")
                            .setMessage("Are you very sure you want to restore the latest backup, this will clear the internal database, and restore the latest backup found?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Toast.makeText(MainActivity.this, "Running import", Toast.LENGTH_LONG).show();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            doImport();
                                        }
                                    }).start();
                                }})
                            .setNegativeButton(android.R.string.no, null).show();
                }
                else
                    Toast.makeText(this, "You disabled backup, try restarting the app to try again", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Second step of creating a transaction.
     */
    private void chooseType() {
        double value;
        if (spent.getText().toString().isEmpty()) {
            spent.setError("This cannot be empty");
            return;
        }
        try {
            value = spent.getCurrencyDouble();
        } catch (ParseException | NumberFormatException e) {
            spent.setError("Error parsing what this contains");
            return;
        }

        startActivity(
                new Intent(this, SelectCategoryActivity.class)
                        .putExtra(Shared.SPENT, value)
        );
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
                calculateSpendable();
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

    private void calculateSpendable() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                double spent = new Data(MainActivity.this).spentFrom(Shared.monthStart(MainActivity.this));
                double todaySpent = new Data(MainActivity.this).spentFrom(Shared.todayStart());
                Calendar cal = Calendar.getInstance();
                int now = cal.get(Calendar.DAY_OF_YEAR);
                cal.setTimeInMillis(Shared.monthEnd(MainActivity.this));
                int monthAhead = cal.get(Calendar.DAY_OF_YEAR);

                int d = Shared.max(monthAhead - now + (now > monthAhead ? cal.getMaximum(Calendar.DAY_OF_YEAR) : 0), 1);


                double monthAvailable = Shared.getSavedSpend(MainActivity.this) - spent + todaySpent;
                double dayAvailable = monthAvailable / d;
                double allowed = dayAvailable - todaySpent;

                final String finalString;
                final boolean overspent = allowed < 0;

                if (overspent)
                    finalString = String.format(Locale.ENGLISH, "Over spent by %s [%s / %d day%s = %s]",
                            Shared.currencyFormat(MainActivity.this, -allowed),
                            Shared.currencyFormat(MainActivity.this, monthAvailable),
                            d,
                            d > 1 ? "s" : "",
                            Shared.currencyFormat(MainActivity.this, dayAvailable));
                else
                    finalString = String.format(Locale.ENGLISH, "%s = [%s / %d day%s - %s]",
                            Shared.currencyFormat(MainActivity.this, allowed),
                            Shared.currencyFormat(MainActivity.this, monthAvailable),
                            d,
                            d > 1 ? "s" : "",
                            Shared.currencyFormat(MainActivity.this, todaySpent));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        available.setText(finalString);
                        available.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), overspent ? R.color.overspent : R.color.white));
                    }
                });

            }
        }).start();
    }
}
