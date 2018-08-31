package com.example.android.inventory2;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ProductsCursorAdapter extends android.widget.CursorAdapter {

    int quantity;

    public ProductsCursorAdapter(Context context, Cursor c) {
        super (context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from (context).inflate (R.layout.inventory_list, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        TextView nameTextView = view.findViewById (R.id.name);
        TextView priceTextView = view.findViewById (R.id.price);
        final Button quantityButtonView = view.findViewById (R.id.quantityButton);

        int nameColumnIndex = cursor.getColumnIndex (Contract.InventoryEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex (Contract.InventoryEntry.COLUMN_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex (Contract.InventoryEntry.COLUMN_QUANTITY);

        String itemName = cursor.getString (nameColumnIndex);
        int price = cursor.getInt (priceColumnIndex);
        quantity = cursor.getInt (quantityColumnIndex);

        StringBuilder priceBuilder = new StringBuilder ();
        priceBuilder.append (String.valueOf (price)).append (" €");

        nameTextView.setText (itemName);
        priceTextView.setText (priceBuilder.toString ());
        quantityButtonView.setText (String.valueOf (quantity));


    }


}
