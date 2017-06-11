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

    public boolean insertMessage(double value, int category, String comment) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TRANSACTIONS_COLUMN_VALUE, value);
        contentValues.put(TRANSACTIONS_COLUMN_CATEGORY, category);
        contentValues.put(TRANSACTIONS_COLUMN_COMMENT, comment);
        contentValues.put(TRANSACTIONS_COLUMN_DATE, new Date().getTime());

        getWritableDatabase().insert(TRANSACTIONS_TABLE, null, contentValues);
        return true;
    }

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
        return array_list;
    }

    public ArrayList<Transaction> getAccumulated(long timeInMillis) {
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
        return array_list;
    }

    public ArrayList<Category> getCategories() {
        ArrayList<Category> array_list = new ArrayList<>();

        //hp = new HashMap();
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
        return array_list;
    }

    public boolean createCategory(String name, int item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY_COLUMN_ICON, item);
        contentValues.put(CATEGORY_COLUMN_NAME, name);

        getWritableDatabase().insert(CATEGORY_TABLE, null, contentValues);
        return true;
    }
}