package zz.merlin.budget.data;

import com.google.gson.JsonObject;

/**
 * Category data type, as is stored in DB.
 */
public class Category {
    public int id;
    public int icon;
    public String name;

    public Category(int id, int icon, String name) {
        this.id = id;
        this.icon = icon;
        this.name = name;
    }

    JsonObject json() {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", id);
        obj.addProperty("iconId", icon);
        obj.addProperty("name", name);
        return obj;
    }
}
