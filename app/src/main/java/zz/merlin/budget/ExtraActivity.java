package zz.merlin.budget;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import zz.merlin.budget.data.Data;
import zz.merlin.budget.data.Shared;
import zz.merlin.budget.widgets.Helper;

public class ExtraActivity extends AppCompatActivity {

    EditText extra;
    double spent;
    int category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            spent = extras.getDouble(Shared.SPENT, 0.0);
            category = extras.getInt(Shared.CATEGORY, 0);
        } else {
            Toast.makeText(this, "Dumb-Ass", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        extra = (EditText) findViewById(R.id.extra_content);

        findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Data(getApplicationContext()).insertMessage(spent, category, extra.getText().toString());
                Helper.updateMyWidgets(getApplicationContext());
                startActivity(new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY));
            }
        });
    }
}
