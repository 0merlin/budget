package zz.merlin.budget.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ArrayAdapterWithIcon extends ArrayAdapter<String> {

    private int[] images;


    public ArrayAdapterWithIcon(Context context, String[] items, int[] images) {
        super(context, android.R.layout.select_dialog_item, items);
        this.images = images;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView) view.findViewById(android.R.id.text1);

        textView.setCompoundDrawablesRelativeWithIntrinsicBounds(images[position], 0, 0, 0);
        textView.setCompoundDrawablePadding(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getContext().getResources().getDisplayMetrics()));
        return view;
    }

}