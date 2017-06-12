package zz.merlin.budget.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.Calendar;

import zz.merlin.budget.R;
import zz.merlin.budget.data.Data;
import zz.merlin.budget.data.Shared;
import zz.merlin.budget.data.Transaction;

/**
 * Implementation of App Widget functionality.
 */
public class MonthSummaryWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.month_summary_widget);
        ArrayList<Transaction> transactions = new Data(context).getAccumulated(Shared.lastPayDay());
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < transactions.size(); i++) {
            if (i>0) sb.append("\n");
            sb.append(transactions.get(i).category.name).append(": ").append(Shared.currency.format(transactions.get(i).value));
        }

        views.setTextViewText(R.id.appwidget_text, sb.toString());


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

