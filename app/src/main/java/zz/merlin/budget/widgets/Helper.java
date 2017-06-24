package zz.merlin.budget.widgets;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;

public class Helper {
    /**
     * Helper method to update all widgets that are created from this app.
     *
     * @param context The context in which to run this update.
     */
    public static void updateMyWidgets(Context context) {
        int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, MonthSummaryWidget.class));
        MonthSummaryWidget myWidget = new MonthSummaryWidget();
        myWidget.onUpdate(context, AppWidgetManager.getInstance(context), ids);

        int[] idsSpent = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, SpentMonthWidget.class));
        SpentMonthWidget spentMonthWidget = new SpentMonthWidget();
        spentMonthWidget.onUpdate(context, AppWidgetManager.getInstance(context), idsSpent);
    }
}
