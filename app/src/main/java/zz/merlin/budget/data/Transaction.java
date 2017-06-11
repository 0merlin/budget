package zz.merlin.budget.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Date;

public class Transaction {
    public int id;
    public double value;
    public String comment;
    public Category category;
    public Date date;

    Transaction(int id, double value, Category category, String comment, long date) {
        this.id = id;
        this.value = value;
        this.category = category;
        this.comment = comment;
        this.date = new Date(date);
    }

    public JsonObject json() {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", id);
        obj.addProperty("comment", comment);
        obj.addProperty("date", Shared.date_full.format(date));
        obj.addProperty("timestamp", date.getTime());
        obj.add("category", category.json());
        return obj;
    }
}
