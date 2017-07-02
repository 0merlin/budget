package zz.merlin.budget;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;

import faranjit.currency.edittext.CurrencyEditText;
import zz.merlin.budget.data.Data;
import zz.merlin.budget.data.Shared;

public class MainActivity extends AppCompatActivity {

    public static boolean enableBackup = true;

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
