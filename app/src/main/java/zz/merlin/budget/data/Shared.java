package zz.merlin.budget.data;

import android.content.Context;
import android.util.DisplayMetrics;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import zz.merlin.budget.R;

/**
 * Lots of shared commands, and shared data for the application, this file itself stores no data.
 * And is just a proxy to other calls.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Shared {
    public static final String SPENT = "spent";
    public static final String CATEGORY = "category";
    public static final String SAVED_LOCALE = "locale";
    public static final String SAVED_SPEND = "spend";
    public static final String SAVED_MONTH_START = "month_start";
    public static final SimpleDateFormat date_full = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss", Locale.ENGLISH);
    public static final SimpleDateFormat date = new SimpleDateFormat("EEEE dd MMMM", Locale.ENGLISH);
    public static final SimpleDateFormat time = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
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
    private static final String PREFERENCE_FILE = "budget_preferences";
    /**
     * Currently tested locales.
     */
    public static String[] supportedLocales = new String[]{
            "en-GB",
            "en-US",
            "en-ZA"
    };

    /**
     * Calculate the time in milliseconds for the start of today.
     *
     * @return The start of today in milliseconds.
     */
    public static long todayStart() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTimeInMillis();
    }

    /**
     * The start of the current month, using the provided start of month day. (1 is default)
     *
     * @param context The context in which to find the configs.
     * @return The time in milliseconds.
     */
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
        }
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTimeInMillis();
    }

    /**
     * The end of the current month, using the provided start of month day. (1 is default)
     *
     * @param context The context in which to find the configs.
     * @return The time in milliseconds.
     */
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
        }
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTimeInMillis();
    }

    /**
     * Helper method, covert units in device pixels to pixels
     *
     * @param context The context to find device info.
     * @param dp      The value in device pixels to convert.
     * @return The result in pixels.
     */
    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    /**
     * Helper method, covert units in pixels to device pixels
     *
     * @param context The context to find device info.
     * @param px      The value in pixels to convert.
     * @return The result in device pixels.
     */
    public static int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    /**
     * Set a floating point value in the preferences file.
     *
     * @param context The context to save the preference file for.
     * @param key     The key to use.
     * @param value   The value to store at the key.
     */
    public static void set(Context context, String key, float value) {
        context.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE).edit().putFloat(key, value).apply();
    }

    /**
     * Set a string value in the preferences file.
     *
     * @param context The context to save the preference file for.
     * @param key     The key to use.
     * @param value   The value to store at the key.
     */
    public static void set(Context context, String key, String value) {
        context.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE).edit().putString(key, value).apply();

    }

    /**
     * Retrieve a value from preferences file
     *
     * @param context The context to save the preference file for.
     * @param key     The key to use.
     * @param def     The default value.
     * @return The value or default.
     */
    public static float get(Context context, String key, float def) {
        return context.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE).getFloat(key, def);
    }

    /**
     * Retrieve a value from preferences file
     *
     * @param context The context to save the preference file for.
     * @param key     The key to use.
     * @param def     The default value.
     * @return The value or default.
     */
    public static String get(Context context, String key, String def) {
        return context.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE).getString(key, def);
    }

    /**
     * Helper method to retrieve the saved locale.
     *
     * @param context The context in which to retrieve the saved preference.
     * @return The locale or a default.
     */
    public static Locale getSavedLocale(Context context) {
        return Locale.forLanguageTag(Shared.get(context, Shared.SAVED_LOCALE, Shared.supportedLocales[0]));
    }

    /**
     * Helper method to retrieve the saved start of month value.
     *
     * @param context The context in which to retrieve the saved preference.
     * @return The start of month or a default.
     */
    public static int getStartDay(Context context) {
        return Integer.parseInt(Shared.get(context, Shared.SAVED_MONTH_START, "1"));
    }

    /**
     * Format the given amount value in the saved locale.
     *
     * @param context The context in which to retrieve the saved preference.
     * @param amount  The amount that needs formatting
     * @return The string that is then formatted.
     */
    public static String currencyFormat(Context context, double amount) {
        return NumberFormat.getCurrencyInstance(getSavedLocale(context)).format(amount);
    }

    /**
     * Minimum of the given values.
     *
     * @param x List of parameters to search through.
     * @return The minimum value.
     */
    public static int min(int... x) {
        int m = x[0];
        for (int i : x) if (i < m) m = i;
        return m;
    }

    /**
     * Maximum of the given values.
     *
     * @param x List of parameters to search through.
     * @return The maximum value.
     */
    public static int max(int... x) {
        int m = x[0];
        for (int i : x) if (i > m) m = i;
        return m;
    }
}
