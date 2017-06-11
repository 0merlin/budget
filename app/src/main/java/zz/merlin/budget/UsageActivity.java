package zz.merlin.budget;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import zz.merlin.budget.data.Data;
import zz.merlin.budget.data.Shared;
import zz.merlin.budget.data.Transaction;

public class UsageActivity extends AppCompatActivity {
    ArrayList<Transaction> transactionsList;

    LinearLayout transactions;
    PieChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage);
        transactions = (LinearLayout) findViewById(R.id.transactions);
        chart = (PieChart) findViewById(R.id.chart);
//        chart.setUsePercentValues(true);
        chart.getLegend().setEnabled(false);
        chart.setDescription(null);
        chart.setEntryLabelColor(Color.BLACK);
        chart.setEntryLabelTextSize(18);


        new Thread(new Runnable() {
            @Override
            public void run() {
                transactionsList = new Data(getApplicationContext()).getAccumulated(Shared.monthStart());
                List<PieEntry> entries = new ArrayList<>();

                for (Transaction data : transactionsList) {

                    // turn your data into Entry objects
                    entries.add(new PieEntry((float) data.value, data.category.name));
                }
                PieDataSet set = new PieDataSet(entries, "Spending");
                set.setValueTextSize(14);
                set.setColors(ColorTemplate.JOYFUL_COLORS);
                final PieData data = new PieData(set);
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
                            CardView ll = (CardView) getLayoutInflater().inflate(R.layout.transaction, transactions, false);
                            ((TextView) ll.findViewById(R.id.value)).setText(transaction.category.name);
                            ((TextView) ll.findViewById(R.id.category)).setText(Shared.currency.format(transaction.value));
                            transactions.addView(ll);
                        }
                        chart.setData(data);
                        chart.invalidate();
                    }
                });
            }
        }).start();
    }
}
