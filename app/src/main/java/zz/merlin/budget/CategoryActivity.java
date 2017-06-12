package zz.merlin.budget;

import android.content.DialogInterface;
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

public class CategoryActivity extends AppCompatActivity {

    ArrayList<Category> categories;
    GridLayout grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        grid = (GridLayout) findViewById(R.id.choice_grid);
        reload();
    }

    private void reload() {
        grid.removeAllViews();
        categories = new Data(this).getCategories();
        for (final Category category : categories) {
            Button button = (Button) getLayoutInflater().inflate(R.layout.category, grid, false);
            button.setText(category.name);
            button.setCompoundDrawablesWithIntrinsicBounds(0, Shared.icons[category.icon], 0, 0);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getNewName(category);
                }
            });
            grid.addView(button);
        }
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
                getNewName(null);
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getNewName(final Category category) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter new category name");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        builder.setView(input);
        int padding = Shared.dpToPx(this, 8);
        input.setPadding(input.getPaddingLeft() + padding, input.getPaddingTop(), input.getPaddingRight() + padding, input.getPaddingBottom());
        if (category != null)
            input.setText(category.name);

        builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = input.getText().toString().replaceAll("[^a-zA-Z0-9 _-]", "");
                if (text.length() < 3) {
                    Toast.makeText(getApplicationContext(), "Category name too short, or it uses invalid characters", Toast.LENGTH_LONG).show();
                    return;
                }
                if (category == null || !category.name.equalsIgnoreCase(text))
                    for (Category category : categories) {
                        if (category.name.equalsIgnoreCase(text)) {
                            Toast.makeText(getApplicationContext(), "Category already exists", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                selectIcon(category, text);
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

    private void selectIcon(final Category category, final String name) {

        ListAdapter adapter = new ArrayAdapterWithIcon(this, Shared.items, Shared.icons);

        new AlertDialog.Builder(this)
                .setTitle("Select Image")
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (category == null)
                            new Data(getApplicationContext()).createCategory(name, item);
                        else
                            new Data(getApplicationContext()).updateCategory(category, name, item);
                        Toast.makeText(getApplicationContext(), "Item Selected: " + item, Toast.LENGTH_SHORT).show();
                        reload();
                    }
                }).show();
    }
}
