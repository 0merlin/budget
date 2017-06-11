package zz.merlin.budget.widgets;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;

public class Helper {
    public static void updateMyWidgets(Context context) {
        int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, MonthSummaryWidget.class));
        MonthSummaryWidget myWidget = new MonthSummaryWidget();
        myWidget.onUpdate(context, AppWidgetManager.getInstance(context), ids);

        int[] idsSpent = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, SpentMonthWidget.class));
        SpentMonthWidget spentMonthWidget = new SpentMonthWidget();
        spentMonthWidget.onUpdate(context, AppWidgetManager.getInstance(context), idsSpent);
    }
}
