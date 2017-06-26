package zz.merlin.budget.data;

import com.google.gson.JsonObject;

import java.util.Date;

/**
 * Transaction data type, as is stored in DB.
 */
public class Transaction {
    public int id;
    public double value;
    public String comment;
    public Category category;
    public Date date;
    public long timestamp;

    Transaction(int id, double value, Category category, String comment, long date) {
        this.id = id;
        this.value = value;
        this.category = category;
        this.comment = comment;
        this.date = new Date(date);
        this.timestamp = date;
    }

    public Transaction(JsonObject jsonObject) {
        this(
                jsonObject.get("id").getAsInt(),
                jsonObject.get("value").getAsDouble(),
                new Category(jsonObject.getAsJsonObject("category")),
                jsonObject.get("comment").getAsString(),
                jsonObject.get("timestamp").getAsLong()
        );
    }

    public JsonObject json() {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", id);
        obj.addProperty("comment", comment);
        obj.addProperty("value", value);
        obj.addProperty("date", Shared.date_full.format(date));
        obj.addProperty("timestamp", timestamp);
        obj.add("category", category.json());
        return obj;
    }
}
