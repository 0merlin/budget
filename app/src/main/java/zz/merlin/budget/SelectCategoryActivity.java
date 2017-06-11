package zz.merlin.budget;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Toast;

import java.util.ArrayList;

import zz.merlin.budget.data.ArrayAdapterWithIcon;
import zz.merlin.budget.data.Category;
import zz.merlin.budget.data.Data;
import zz.merlin.budget.data.Shared;

public class SelectCategoryActivity extends AppCompatActivity {

    private final int icons[] = new int[]{
            R.drawable.ic_alien,
            R.drawable.ic_baby,
            R.drawable.ic_beer,
            R.drawable.ic_chicken,
            R.drawable.ic_cutlery,
            R.drawable.ic_diamond,
            R.drawable.ic_diaper,
            R.drawable.ic_drinks,
            R.drawable.ic_fork,
            R.drawable.ic_home,
            R.drawable.ic_man,
            R.drawable.ic_medical,
            R.drawable.ic_money,
            R.drawable.ic_ninja,
            R.drawable.ic_photo_camera,
            R.drawable.ic_piggy_bank,
            R.drawable.ic_rain,
            R.drawable.ic_scales,
            R.drawable.ic_shopping_bag,
            R.drawable.ic_spoon,
            R.drawable.ic_sun,
            R.drawable.ic_transport,
            R.drawable.ic_woman
    };

    ArrayList<Category> categories;
    GridLayout grid;

    private double spent = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            spent = extras.getDouble(Shared.SPENT, 0.0);
        } else {
            Toast.makeText(this, "Dumb-Ass", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        grid = (GridLayout) findViewById(R.id.choice_grid);
        reload();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.category_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                getNewName();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void next(Category category) {
        startActivity(new Intent(this, ExtraActivity.class).putExtra(Shared.SPENT, spent).putExtra(Shared.CATEGORY, category.id));
    }

    private void reload() {
        grid.removeAllViews();
        categories = new Data(this).getCategories();
        for (final Category category : categories) {
            Button button = (Button) getLayoutInflater().inflate(R.layout.category, grid, false);
            button.setText(category.name);
            button.setCompoundDrawablesWithIntrinsicBounds(0, icons[category.icon], 0, 0);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    next(category);
                }
            });
            grid.addView(button);
        }
    }

    private void getNewName() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter new category name");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        builder.setView(input);

        builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = input.getText().toString();
                if (text.length() < 3) {
                    Toast.makeText(getApplicationContext(), "Category name too short", Toast.LENGTH_LONG).show();
                    return;
                }
                for (Category category : categories) {
                    if (category.name.equalsIgnoreCase(text)) {
                        Toast.makeText(getApplicationContext(), "Category already exists", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                selectIcon(text);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void selectIcon(final String name) {
        final String[] items = new String[]{
                "Alien",
                "Baby",
                "Beer",
                "Chicken",
                "Cutlery",
                "Diamond",
                "Diaper",
                "Drinks",
                "Fork",
                "Home",
                "Man",
                "Medical",
                "Money",
                "Ninja",
                "Photo camera",
                "Piggy bank",
                "Rain",
                "Scales",
                "Shopping bag",
                "Spoon",
                "Sun",
                "Transport",
                "Woman"
        };
        ListAdapter adapter = new ArrayAdapterWithIcon(this, items, icons);

        new AlertDialog.Builder(this)
                .setTitle("Select Image")
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        new Data(getApplicationContext()).createCategory(name, item);
                        Toast.makeText(getApplicationContext(), "Item Selected: " + item, Toast.LENGTH_SHORT).show();
                        reload();
                    }
                }).show();
    }
}
