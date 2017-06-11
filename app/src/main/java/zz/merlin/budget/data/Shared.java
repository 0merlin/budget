package zz.merlin.budget.data;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Shared {
    public static final String SPENT = "spent";
    public static final String CATEGORY = "category";
    public static final SimpleDateFormat date_full = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss", Locale.ENGLISH);
    public static final DecimalFormat currency = new DecimalFormat("R #.##");
    public static final SimpleDateFormat date = new SimpleDateFormat("EEEE dd MMMM", Locale.ENGLISH);
    public static final SimpleDateFormat time = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
}
