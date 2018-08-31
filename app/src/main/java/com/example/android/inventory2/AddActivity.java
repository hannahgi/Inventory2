package com.example.android.inventory2;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor> {
    private static final int LOADER = 0;
    private Uri mCurrentUri;
    private EditText mProductName;
    private EditText mPrice;
    private EditText mQuantity;
    private EditText mSupplierName;
    private EditText mPhoneNumber;
    private boolean mItemHasChanged = false;
    private View.OnTouchListener mTouchListener = new View.OnTouchListener () {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mItemHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.add_activity);
        Intent intent = getIntent ();
        mCurrentUri = intent.getData ();
        Button minus = findViewById (R.id.minus);
         Button plus = findViewById (R.id.plus);
         Button call = findViewById (R.id.call_us);

        if (mCurrentUri == null) {
            setTitle (getString (R.string.add_title));
            plus.setVisibility (View.GONE);
            minus.setVisibility (View.GONE);
            invalidateOptionsMenu ();
        } else {

            setTitle (getString (R.string.edit_title));
            call.setVisibility (View.VISIBLE);
            getLoaderManager ().initLoader (LOADER, null, this);
        }

        mProductName = findViewById (R.id.product_name);
        mProductName.setOnTouchListener (mTouchListener);
        mPrice = findViewById (R.id.product_price);
        mPrice.setOnTouchListener (mTouchListener);
        mQuantity = findViewById (R.id.product_quantity);
        mQuantity.setOnTouchListener (mTouchListener);
        mSupplierName = findViewById (R.id.supplier_name);
        mSupplierName.setOnTouchListener (mTouchListener);
        mPhoneNumber = findViewById (R.id.phone_number);
        mPhoneNumber.setOnTouchListener (mTouchListener);

        plus.setOnTouchListener (mTouchListener);
        minus.setOnTouchListener (mTouchListener);

        plus.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                String quantityString = mQuantity.getText ().toString ().trim ();
                int quantity = Integer.parseInt (quantityString);
                if (quantity < 100000) {
                    quantity = Integer.parseInt (quantityString);
                } else {
                    return;
                }

                mQuantity.setText (String.valueOf (quantity + 1));
            }
        });

        minus.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                String quantityString = mQuantity.getText ().toString ().trim ();
                int quantity = Integer.parseInt (quantityString);
                if (quantity > 0) {
                    quantity = Integer.parseInt (quantityString);
                } else {
                    return;
                }

                mQuantity.setText (String.valueOf (quantity - 1));
            }
        });

        call.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                String number = mPhoneNumber.getText ().toString ().trim ();
                Uri call = Uri.parse ("tel:" + number);
                Intent intent = new Intent (Intent.ACTION_DIAL, call);
                if (intent.resolveActivity (getPackageManager ()) != null) {
                    startActivity (intent);
                }
            }
        });


    }

    private void saveItem() {

        String nameProduct = mProductName.getText ().toString ().trim ();

        String priceString = mPrice.getText ().toString ().trim ();
        int price = 0;

        String quantityString = mQuantity.getText ().toString ().trim ();
        int quantity = 0;

        String nameSupplier = mSupplierName.getText ().toString ().trim ();

        String phoneString = mPhoneNumber.getText ().toString ().trim ();
        int phone = 0;

        ContentValues values = new ContentValues ();
        values.put (Contract.InventoryEntry.COLUMN_PRODUCT_NAME, nameProduct);

        if (!TextUtils.isEmpty (priceString)) {
            price = Integer.parseInt (priceString);
        }
        values.put (Contract.InventoryEntry.COLUMN_PRICE, price);


        if (!TextUtils.isEmpty (quantityString)) {
            quantity = Integer.parseInt (quantityString);
        }
        values.put (Contract.InventoryEntry.COLUMN_QUANTITY, quantity);

        values.put (Contract.InventoryEntry.COLUMN_SUPPLIER_NAME, nameSupplier);

        if (!TextUtils.isEmpty (phoneString)) {
            phone = Integer.parseInt (phoneString);
        }
        values.put (Contract.InventoryEntry.COLUMN_SUPPLIER_PHONE, phone);

        if (mCurrentUri == null) {
            if (TextUtils.isEmpty (nameProduct) || TextUtils.isEmpty (priceString) || TextUtils.isEmpty (quantityString) || TextUtils.isEmpty (nameSupplier) || TextUtils.isEmpty (phoneString)) {
                Toast.makeText (this, R.string.empty_fields_alert, Toast.LENGTH_LONG).show ();
                return;
            }
            Uri newUri = getContentResolver ().insert (Contract.InventoryEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText (this, R.string.nothing_to_insert, Toast.LENGTH_SHORT).show ();
            } else {
                Toast.makeText (this, R.string.insert_item_successful, Toast.LENGTH_SHORT).show ();
            }
        } else {

            if (TextUtils.isEmpty (nameProduct) || TextUtils.isEmpty (priceString) || TextUtils.isEmpty (quantityString) || TextUtils.isEmpty (nameSupplier) || TextUtils.isEmpty (phoneString)) {
                Toast.makeText (this, R.string.empty_fields_alert, Toast.LENGTH_LONG).show ();
                return;
            }
            int rowsAffected = getContentResolver ().update (mCurrentUri, values, null, null);

            if (rowsAffected == 0) {
                Toast.makeText (this, R.string.nothing_to_insert, Toast.LENGTH_SHORT).show ();
            } else {
                Toast.makeText (this, R.string.insert_item_successful, Toast.LENGTH_SHORT).show ();
            }
        }

        finish ();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater ().inflate (R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu (menu);

        if (mCurrentUri == null) {
            MenuItem menuItem = menu.findItem (R.id.action_delete);
            menuItem.setVisible (false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId ()) {

            case R.id.action_save:
                saveItem ();
                return true;

            case R.id.action_delete:
                showDeleteConfirmationDialog ();
                return true;

            case android.R.id.home:

                if (!mItemHasChanged) {
                    NavUtils.navigateUpFromSameTask (AddActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener () {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NavUtils.navigateUpFromSameTask (AddActivity.this);
                    }
                };

                showUnsavedChangesDialog (discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected (item);
    }

    @Override
    public void onBackPressed() {
        if (!mItemHasChanged) {
            super.onBackPressed ();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish ();
            }
        };

        showUnsavedChangesDialog (discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {Contract.InventoryEntry._ID, Contract.InventoryEntry.COLUMN_PRODUCT_NAME, Contract.InventoryEntry.COLUMN_PRICE, Contract.InventoryEntry.COLUMN_QUANTITY, Contract.InventoryEntry.COLUMN_SUPPLIER_NAME, Contract.InventoryEntry.COLUMN_SUPPLIER_PHONE};


        return new CursorLoader (this, mCurrentUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount () < 1) {
            return;
        }

        if (cursor.moveToFirst ()) {
            int nameColumnIndex = cursor.getColumnIndex (Contract.InventoryEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex (Contract.InventoryEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex (Contract.InventoryEntry.COLUMN_QUANTITY);
            int supplierColumnIndex = cursor.getColumnIndex (Contract.InventoryEntry.COLUMN_SUPPLIER_NAME);
            int phoneColumnIndex = cursor.getColumnIndex (Contract.InventoryEntry.COLUMN_SUPPLIER_PHONE);

            String name = cursor.getString (nameColumnIndex);
            int price = cursor.getInt (priceColumnIndex);
            int quantity = cursor.getInt (quantityColumnIndex);
            String supplier = cursor.getString (supplierColumnIndex);
            int phone = cursor.getInt (phoneColumnIndex);


            mProductName.setText (name);
            mPrice.setText (Integer.toString (price));
            mQuantity.setText (Integer.toString (quantity));
            mSupplierName.setText (supplier);
            mPhoneNumber.setText (Integer.toString (phone));

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mProductName.setText ("");
        mPrice.setText ("");
        mQuantity.setText ("");
        mSupplierName.setText ("");
        mPhoneNumber.setText ("");
    }

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder (
                this);
        builder.setMessage (R.string.unsaved_changes_dialog_message);
        builder.setPositiveButton (R.string.discard, discardButtonClickListener);
        builder.setNegativeButton (R.string.keep_editing, new DialogInterface.OnClickListener () {
            public void onClick(DialogInterface dialog, int id) {

                if (dialog != null) {
                    dialog.dismiss ();
                }
            }
        });

        AlertDialog alertDialog = builder.create ();
        alertDialog.show ();
    }


    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder (this);
        builder.setMessage (R.string.delete_msg);
        builder.setPositiveButton (R.string.delete, new DialogInterface.OnClickListener () {
            public void onClick(DialogInterface dialog, int id) {

                deleteItem ();
            }
        });
        builder.setNegativeButton (R.string.cancel, new DialogInterface.OnClickListener () {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss ();
                }
            }
        });

        AlertDialog alertDialog = builder.create ();
        alertDialog.show ();
    }


    private void deleteItem() {

        if (mCurrentUri != null) {

            int rowsDeleted = getContentResolver ().delete (mCurrentUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText (this, R.string.nothing_to_insert, Toast.LENGTH_SHORT).show ();
            } else {
                Toast.makeText (this, R.string.insert_item_successful, Toast.LENGTH_SHORT).show ();
            }
        }

        finish ();
    }
}
