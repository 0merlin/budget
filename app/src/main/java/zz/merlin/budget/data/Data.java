package zz.merlin.budget.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;

public class Data extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "BudgetApp.db";
    private static final String TRANSACTIONS_TABLE = "transactions";
    private static final String TRANSACTIONS_COLUMN_ID = "id";
    private static final String TRANSACTIONS_COLUMN_VALUE = "value";
    private static final String TRANSACTIONS_COLUMN_CATEGORY = "category";
    private static final String TRANSACTIONS_COLUMN_COMMENT = "comment";
    private static final String TRANSACTIONS_COLUMN_DATE = "transaction_date";
    private static final String CATEGORY_TABLE = "category";
    private static final String CATEGORY_COLUMN_ID = "id";
    private static final String CATEGORY_COLUMN_ICON = "icon";
    private static final String CATEGORY_COLUMN_NAME = "name";

    public Data(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TRANSACTIONS_TABLE + "  (" +
                TRANSACTIONS_COLUMN_ID + " integer primary key, " +
                TRANSACTIONS_COLUMN_VALUE + " real, " +
                TRANSACTIONS_COLUMN_CATEGORY + " integer, " +
                TRANSACTIONS_COLUMN_COMMENT + " text," +
                TRANSACTIONS_COLUMN_DATE + " integer" +
                ")");
        db.execSQL("create table " + CATEGORY_TABLE + "  (" +
                CATEGORY_COLUMN_ID + " integer primary key, " +
                CATEGORY_COLUMN_ICON + " integer, " +
                CATEGORY_COLUMN_NAME + " text" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TRANSACTIONS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CATEGORY_TABLE);
        onCreate(db);
    }

    /**
     * Create a new transaction, using Now() as the time.
     *
     * @param value    The value of the transaction.
     * @param category The category type of the transaction.
     * @param comment  An optional comment on the transaction.
     */
    public void createTransaction(double value, int category, String comment) {
        createTransaction(value, category, comment, new Date().getTime());
    }

    /**
     * Create a new transaction, using Now() as the time.
     *
     * @param value    The value of the transaction.
     * @param category The category type of the transaction.
     * @param comment  An optional comment on the transaction.
     * @param time     The time at which the transaction occurred.
     */
    public void createTransaction(double value, int category, String comment, long time) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TRANSACTIONS_COLUMN_VALUE, value);
        contentValues.put(TRANSACTIONS_COLUMN_CATEGORY, category);
        contentValues.put(TRANSACTIONS_COLUMN_COMMENT, comment);
        contentValues.put(TRANSACTIONS_COLUMN_DATE, time);

        getWritableDatabase().insert(TRANSACTIONS_TABLE, null, contentValues);
    }

    /**
     * Get all transaction after the provided time.
     *
     * @param timeInMillis Time to check for.
     * @return The list of transaction.
     */
    public ArrayList<Transaction> getTransactionsAfter(long timeInMillis) {
        ArrayList<Transaction> array_list = new ArrayList<>();

        //hp = new HashMap();
        SQLiteDatabase db = getReadableDatabase();
        Cursor res = db.rawQuery(
                "SELECT t.id, t.value, t.comment, t.transaction_date, c.id, c.icon, c.name " +
                        "FROM " + TRANSACTIONS_TABLE + " as t " +
                        "LEFT JOIN " + CATEGORY_TABLE + " as c ON t.category=c.id " +
                        "WHERE t.transaction_date >= ? " +
                        "ORDER BY t.transaction_date DESC",
                new String[]{Long.toString(timeInMillis)});
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(new Transaction(res.getInt(0),
                    res.getDouble(1),
                    new Category(res.getInt(4), res.getInt(5), res.getString(6)),
                    res.getString(2),
                    res.getLong(3)
            ));
            res.moveToNext();
        }
        res.close();
        db.close();
        return array_list;
    }

    /**
     * Get a summary of the transactions after the given time.
     *
     * @param timeInMillis Time to check for.
     * @return The list of summarised transactions.
     */
    public ArrayList<Transaction> getSummaryTransactions(long timeInMillis) {
        ArrayList<Transaction> array_list = new ArrayList<>();

        //hp = new HashMap();
        SQLiteDatabase db = getReadableDatabase();

        Cursor res = db.rawQuery(
                "SELECT c.id, c.icon, c.name, SUM(t.value) " +
                        "FROM " + TRANSACTIONS_TABLE + " as t " +
                        "LEFT JOIN " + CATEGORY_TABLE + " as c ON t.category=c.id " +
                        "WHERE t.transaction_date >= ? " +
                        "GROUP BY c.id " +
                        "ORDER BY c.name",
                new String[]{Long.toString(timeInMillis)});
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(new Transaction(0,
                    res.getDouble(3),
                    new Category(res.getInt(0), res.getInt(1), res.getString(2)),
                    "",
                    0
            ));
            res.moveToNext();
        }
        res.close();
        db.close();
        return array_list;
    }

    /**
     * Fetch all categories.
     *
     * @return The list of categories.
     */
    public ArrayList<Category> getCategories() {
        ArrayList<Category> array_list = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor res = db.rawQuery(
                "SELECT c.id, c.icon, c.name " +
                        "FROM " + CATEGORY_TABLE + " as c " +
                        "GROUP BY c.name",
                new String[]{});
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(new Category(res.getInt(0), res.getInt(1), res.getString(2)));
            res.moveToNext();
        }
        res.close();

        db.close();
        return array_list;
    }

    /**
     * Create a new category.
     *
     * @param name Cateoory name.
     * @param item Category image.
     */
    public void createCategory(String name, int item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY_COLUMN_ICON, item);
        contentValues.put(CATEGORY_COLUMN_NAME, name);

        getWritableDatabase().insert(CATEGORY_TABLE, null, contentValues);
    }

    /**
     * Total spent of the given time.
     *
     * @param timeInMillis Time to search for.
     * @return Total spent.
     */
    public double spentFrom(long timeInMillis) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor res = db.rawQuery(
                "SELECT SUM(t.value) " +
                        "FROM " + TRANSACTIONS_TABLE + " as t " +
                        "WHERE t.transaction_date >= ? ",
                new String[]{Long.toString(timeInMillis)});
        res.moveToFirst();
        double x = 0;

        while (!res.isAfterLast()) {
            x = res.getDouble(0);
            res.moveToNext();
        }
        res.close();
        db.close();
        return x;
    }

    /**
     * Update the given category with the given information.
     *
     * @param category Orriginal category.
     * @param name     New name.
     * @param icon     New icon.
     */
    public void updateCategory(Category category, String name, int icon) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY_COLUMN_ICON, icon);
        contentValues.put(CATEGORY_COLUMN_NAME, name);
        getWritableDatabase().update(CATEGORY_TABLE, contentValues, CATEGORY_COLUMN_ID + " = ?", new String[]{String.valueOf(category.id)});
    }
}