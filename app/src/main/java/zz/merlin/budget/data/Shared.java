package zz.merlin.budget.data;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Shared {
    public static final String SPENT = "spent";
    public static final String CATEGORY = "category";
    public static final SimpleDateFormat date_full = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss", Locale.ENGLISH);
    public static final DecimalFormat currency = new DecimalFormat("R #.##");
    public static final SimpleDateFormat date = new SimpleDateFormat("EEEE dd MMMM", Locale.ENGLISH);
    public static final SimpleDateFormat time = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

    public static long monthStart() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTimeInMillis();
    }

    public static long lastPayDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 25);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTimeInMillis();
    }
}
