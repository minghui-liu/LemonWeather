package com.minghui_liu.android.lemonweather;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.minghui_liu.android.lemonweather.database.CityDatabase;
import com.minghui_liu.android.lemonweather.database.CityProvider;
import com.minghui_liu.android.lemonweather.model.weather.Main;

/**
 * Created by kevin on 12/13/15.
 */
public class SearchResultDialogFragment extends DialogFragment {
    private static final String TAG = "LemonWeather";
    private Cursor mCursor;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        ListView resultListView = new ListView(getContext());

        // Specify the columns we want to display in the result
        String[] from = new String[]{CityDatabase.KEY_NAME,
                CityDatabase.KEY_COUNTRY};
        // Specify the corresponding layout elements where we want the columns to go
        int[] to = new int[]{android.R.id.text1, android.R.id.text2};
        // Create a simple cursor adapter for the definitions and apply them to the ListView
        SimpleCursorAdapter cities = new SimpleCursorAdapter(getContext(),
                android.R.layout.simple_list_item_2, mCursor, from, to);

        resultListView.setAdapter(cities);
        // Define the on-click listener for the list items
        resultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Build the Intent used to open WordActivity with a specific word Uri
                Intent insertIntent = new Intent(getContext(), MainActivity.class);
                Uri data = Uri.withAppendedPath(CityProvider.CONTENT_URI,
                        String.valueOf(id));
                insertIntent.setData(data);
                insertIntent.setAction(Intent.ACTION_VIEW);
                startActivity(insertIntent);
                Log.d(TAG, "Selected: " + data.toString());
                dismiss();
            }
        });

        builder.setView(resultListView).setTitle(R.string.result_dialog_title)
                .setNegativeButton(R.string.result_dialog_dismiss, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SearchResultDialogFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    public void setCursor(Cursor cursor) {
        mCursor = cursor;
    }
}
