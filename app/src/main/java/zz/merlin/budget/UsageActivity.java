package zz.merlin.budget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import zz.merlin.budget.data.Data;
import zz.merlin.budget.data.Shared;
import zz.merlin.budget.data.Transaction;

public class UsageActivity extends AppCompatActivity {
    ArrayList<Transaction> transactionsList;

    LinearLayout transactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        transactions = (LinearLayout) findViewById(R.id.transactions);


        new Thread(new Runnable() {
            @Override
            public void run() {
                transactionsList = new Data(getApplicationContext()).getSummaryTransactions(Shared.monthStart(UsageActivity.this));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        transactions.removeAllViews();
                        if (transactionsList.isEmpty()) {
                            View v = getLayoutInflater().inflate(R.layout.date_seperator, transactions, false);
                            ((TextView) v.findViewById(R.id.text)).setText(R.string.no_transactions);
                            transactions.addView(v);
                        }
                        for (Transaction transaction : transactionsList) {
                            LinearLayout ll = (LinearLayout) getLayoutInflater().inflate(R.layout.summary, transactions, false);
                            ((ImageView) ll.findViewById(R.id.image)).setImageDrawable(getDrawable(Shared.icons[transaction.category.icon]));
                            ((TextView) ll.findViewById(R.id.category)).setText(transaction.category.name);
                            ((TextView) ll.findViewById(R.id.value)).setText(Shared.currencyFormat(UsageActivity.this, transaction.value));
                            transactions.addView(ll);
                        }
                    }
                });
            }
        }).start();
    }
}
