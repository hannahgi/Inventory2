package com.example.android.inventory2;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int PRODUCTS_LOADER = 0;

    ProductsCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn= findViewById(R.id.clickMe);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });
        ListView inventoryListView = findViewById(R.id.list);

        View emptyView = findViewById(R.id.empty_view);
        inventoryListView.setEmptyView(emptyView);

        mCursorAdapter = new ProductsCursorAdapter (this, null);
        inventoryListView.setAdapter(mCursorAdapter);

        inventoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                Uri currentInventoryUri = ContentUris.withAppendedId(Contract.InventoryEntry.CONTENT_URI, id);
                intent.setData(currentInventoryUri);
                startActivity(intent);
            }
        });


        getLoaderManager().initLoader(PRODUCTS_LOADER, null, this);

    }

    public void decreaseProduct(View v) {
        ListView lV = findViewById(R.id.list);
        final int position = lV.getPositionForView((View) v.getParent());

        Button b = (Button) v;
        int quantity = Integer.parseInt(b.getText().toString());

        if (quantity > 0) {

            ContentValues values = new ContentValues();
            values.put(Contract.InventoryEntry.COLUMN_QUANTITY, quantity - 1);

            Uri uri = Uri.parse(Contract.InventoryEntry.CONTENT_URI + "/" + mCursorAdapter.getItemId(position));
            getContentResolver().update(uri, values, null, null);
            mCursorAdapter.notifyDataSetChanged();

        } else {
            Toast.makeText(this, R.string.decrease_error_message, Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private void deleteAll() {
        int rowsDeleted = getContentResolver().delete(Contract.InventoryEntry.CONTENT_URI, null, null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.show_whole_data:

                deleteAll();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {
                Contract.InventoryEntry._ID,
                Contract.InventoryEntry.COLUMN_PRODUCT_NAME,
                Contract.InventoryEntry.COLUMN_PRICE,
                Contract.InventoryEntry.COLUMN_QUANTITY
        };

        return new CursorLoader (this,
                Contract.InventoryEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        mCursorAdapter.swapCursor(null);
    }

}
