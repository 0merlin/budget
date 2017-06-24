package zz.merlin.budget.data;

import android.content.Context;
import android.util.DisplayMetrics;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import zz.merlin.budget.R;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Shared {
    public static final String SPENT = "spent";
    public static final String CATEGORY = "category";
    public static final String SAVED_LOCALE = "locale";
    public static final String SAVED_SPEND = "spend";
    public static final String SAVED_MONTH_START = "month_start";

    private static final String PREFERENCE_FILE = "budget_preferences";

    public static final SimpleDateFormat date_full = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss", Locale.ENGLISH);
    public static final SimpleDateFormat date = new SimpleDateFormat("EEEE dd MMMM", Locale.ENGLISH);
    public static final SimpleDateFormat time = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

    public static long todayStart() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTimeInMillis();
    }

    public static long monthStart(Context context) {
        int d = getStartDay(context);
        Calendar cal = Calendar.getInstance();
        if (cal.get(Calendar.DAY_OF_MONTH) >= d) {
            cal.set(Calendar.DAY_OF_MONTH, d);
        } else {
            int m = cal.get(Calendar.MONTH);
            if (m > 1)
                cal.set(Calendar.MONTH, m - 1);
            else {
                cal.set(Calendar.MONTH, 12);
                cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
            }
            cal.set(Calendar.DAY_OF_MONTH, d);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
        }
        return cal.getTimeInMillis();
    }

    public static long monthEnd(Context context) {
        int d = getStartDay(context);
        Calendar cal = Calendar.getInstance();
        if (cal.get(Calendar.DAY_OF_MONTH) < d) {
            cal.set(Calendar.DAY_OF_MONTH, d);
        } else {
            int m = cal.get(Calendar.MONTH);
            if (m < 12)
                cal.set(Calendar.MONTH, m + 1);
            else {
                cal.set(Calendar.MONTH, 1);
                cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1);
            }
            cal.set(Calendar.DAY_OF_MONTH, d);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
        }
        return cal.getTimeInMillis();
    }

    public static final int icons[] = new int[]{
            R.drawable.ic_alien,
            R.drawable.ic_baby,
            R.drawable.ic_bar_chart,
            R.drawable.ic_beer,
            R.drawable.ic_book,
            R.drawable.ic_chicken,
            R.drawable.ic_coffee,
            R.drawable.ic_cosmetics,
            R.drawable.ic_cutlery,
            R.drawable.ic_diamond,
            R.drawable.ic_diaper,
            R.drawable.ic_drinks,
            R.drawable.ic_feeding_bottle,
            R.drawable.ic_film_roll,
            R.drawable.ic_fork,
            R.drawable.ic_headphones,
            R.drawable.ic_heavy_metal,
            R.drawable.ic_holiday,
            R.drawable.ic_home,
            R.drawable.ic_idea,
            R.drawable.ic_juice,
            R.drawable.ic_man,
            R.drawable.ic_map_location,
            R.drawable.ic_medical,
            R.drawable.ic_money,
            R.drawable.ic_ninja,
            R.drawable.ic_photo_camera,
            R.drawable.ic_photo_camera,
            R.drawable.ic_piggy_bank,
            R.drawable.ic_placeholder,
            R.drawable.ic_rain,
            R.drawable.ic_scales,
            R.drawable.ic_settings,
            R.drawable.ic_settings_1,
            R.drawable.ic_settings_2,
            R.drawable.ic_shopping_bag,
            R.drawable.ic_spoon,
            R.drawable.ic_stopwatch,
            R.drawable.ic_sun,
            R.drawable.ic_target,
            R.drawable.ic_teeth_cleaning,
            R.drawable.ic_transport,
            R.drawable.ic_video_camera,
            R.drawable.ic_woman
    };
    public static final String[] items = new String[]{
            "Alien",
            "Baby",
            "Bar Chart",
            "Beer",
            "Book",
            "Chicken",
            "Coffee",
            "Cosmetics",
            "Cutlery",
            "Diamond",
            "Diaper",
            "Drinks",
            "Baby Bottle",
            "Film Roll",
            "Fork",
            "Headphones",
            "Heavy Metal",
            "Holiday",
            "Home",
            "Idea",
            "Juice",
            "Man",
            "Map Location",
            "Medical",
            "Money",
            "Ninja",
            "Photo camera",
            "Camera",
            "Piggy bank",
            "Placeholder",
            "Rain",
            "Scales",
            "Gears 1",
            "Gears 2",
            "Gears 3",
            "Shopping bag",
            "Spoon",
            "Clock",
            "Sun",
            "Target",
            "Toiletries",
            "Transport",
            "Video Camera",
            "Woman"
    };

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static String[] supportedLocales = new String[]{
            "en-GB",
            "en-US",
            "en-ZA"
    };

    public static void set(Context context, String key, float value) {
        context.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE).edit().putFloat(key, value).apply();
    }

    public static void set(Context context, String key, String value) {
        context.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE).edit().putString(key, value).apply();

    }

    public static float get(Context context, String key, float def) {
        return context.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE).getFloat(key, def);
    }

    public static String get(Context context, String key, String def) {
        return context.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE).getString(key, def);
    }

    public static Locale getSavedLocale(Context context) {
        return Locale.forLanguageTag(Shared.get(context, Shared.SAVED_LOCALE, Shared.supportedLocales[0]));
    }

    public static int getStartDay(Context context) {
        return Integer.parseInt(Shared.get(context, Shared.SAVED_MONTH_START, "1"));
    }

    public static String currencyFormat(Context context, double amount) {
        return NumberFormat.getCurrencyInstance(getSavedLocale(context)).format(amount);
    }

    public static int min(int... x) {
        int m = x[0];
        for (int i : x) if (i < m) m = i;
        return m;
    }

    public static int max(int... x) {
        int m = x[0];
        for (int i : x) if (i > m) m = i;
        return m;
    }
}
