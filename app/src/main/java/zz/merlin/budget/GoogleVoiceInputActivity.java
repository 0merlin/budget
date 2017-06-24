package zz.merlin.budget;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.actions.NoteIntents;

import java.util.ArrayList;

import zz.merlin.budget.data.Category;
import zz.merlin.budget.data.Data;

public class GoogleVoiceInputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_voice_input);

        try {
            Intent intent = getIntent();
            String s = "";
            if (NoteIntents.ACTION_CREATE_NOTE.equals(intent.getAction())) {
                s = intent.getStringExtra("android.intent.extra.TEXT");
                if (s.length() > 0) {
                    String[] parts = s.split(" ");
                    if (parts.length == 4) {
                        double value = Double.parseDouble(parts[1]);
                        String rawCategory = parts[3];
                        ArrayList<Category> categories = new Data(this).getCategories();
                        for (Category category : categories) {
                            if (category.name.equalsIgnoreCase(rawCategory)) {
                                new Data(this).createTransaction(value, category.id, "");
                                Toast.makeText(this, "Expense captured", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                finish();
                                return;
                            }
                        }

                    }
                }
            }
            Toast.makeText(this, "Unable to save query: " + s, Toast.LENGTH_LONG).show();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "I didn't understand that", Toast.LENGTH_LONG).show();
        }

    }
}
