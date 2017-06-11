package zz.merlin.budget;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.JsonArray;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import faranjit.currency.edittext.CurrencyEditText;
import zz.merlin.budget.data.Data;
import zz.merlin.budget.data.Shared;
import zz.merlin.budget.data.Transaction;

public class MainActivity extends AppCompatActivity {

    boolean enableBackup = true;

    CurrencyEditText spent;
    ImageButton clear;
    Button done;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spent = (CurrencyEditText) findViewById(R.id.edit_currency);
        clear = (ImageButton) findViewById(R.id.btn_clear);
        done = (Button) findViewById(R.id.key_done);
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        Log.e("blah", ""+grantResults[0]);
        Log.e("blah", ""+PackageManager.PERMISSION_GRANTED);
        if (requestCode == 1 && (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED)) {
            Toast.makeText(this, "Backup will be disabled", Toast.LENGTH_LONG).show();
            enableBackup = false;
        }
    }

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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

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

    public void doExport() {

        ArrayList<Transaction> transactions = new Data(this).getTransactionsAfter(0);
        File path = Environment.getExternalStorageDirectory();

        final File file = new File(path, "budget-" + Shared.date_full.format(new Date()) + ".json");
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(file, false);
            JsonArray array = new JsonArray();
            for (Transaction transaction : transactions) array.add(transaction.json());
            stream.write(array.toString().getBytes());
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
