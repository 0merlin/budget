package zz.merlin.budget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import zz.merlin.budget.data.Data;
import zz.merlin.budget.data.Shared;
import zz.merlin.budget.data.Transaction;

public class TransactionsActivity extends AppCompatActivity {
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
                transactionsList = new Data(getApplicationContext()).getTransactionsAfter(Shared.lastPayDay());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        transactions.removeAllViews();
                        String last = "";
                        if (transactionsList.isEmpty()) {
                            View v = getLayoutInflater().inflate(R.layout.date_seperator, transactions, false);
                            ((TextView) v.findViewById(R.id.text)).setText(R.string.no_transactions);
                            transactions.addView(v);
                        }
                        for (Transaction transaction : transactionsList) {
                            String date = Shared.date.format(transaction.date);
                            if (!last.equals(date)) {
                                View v = getLayoutInflater().inflate(R.layout.date_seperator, transactions, false);
                                ((TextView) v.findViewById(R.id.text)).setText(date);
                                transactions.addView(v);
                                last = date;
                            }
                            CardView ll = (CardView) getLayoutInflater().inflate(R.layout.transaction, transactions, false);
                            ((ImageView) ll.findViewById(R.id.image)).setImageDrawable(getDrawable(Shared.icons[transaction.category.icon]));
                            ((TextView) ll.findViewById(R.id.date)).setText(Shared.time.format(transaction.date));
                            ((TextView) ll.findViewById(R.id.value)).setText(Shared.currency.format(transaction.value));
                            ((TextView) ll.findViewById(R.id.category)).setText(transaction.category.name);
                            TextView c = (TextView) ll.findViewById(R.id.comment);
                            if (!transaction.comment.isEmpty()) {
                                c.setVisibility(View.VISIBLE);
                                c.setText(transaction.comment);
                            }
                            transactions.addView(ll);
                        }
                    }
                });
            }
        }).start();
    }
}
