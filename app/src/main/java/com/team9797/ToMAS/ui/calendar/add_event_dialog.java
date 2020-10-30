package com.team9797.ToMAS.ui.calendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.team9797.ToMAS.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import top.defaults.colorpicker.ColorPickerPopup;


public class add_event_dialog extends DialogFragment {

    Context context;
    add_event_dialog_result mdialogResult;
    int selected_color;
    EditText content_editText;
    EditText title_editText;
    Spinner type_spinner;
    String type = null, title = null, content = null;

    public interface add_event_dialog_result{
        void get_result(String type, String title, String content, int color);
    }
    public void setDialogResult(add_event_dialog_result dialogResult)
    {
        mdialogResult = dialogResult;
    }

    public add_event_dialog()
    {

    }

    public add_event_dialog(Context tmp_context)
    {
        context = tmp_context;
    }

    public add_event_dialog(Context tmp_context, String type, String title, String content)
    {
        this.type = type;
        this.title = title;
        this.content = content;
        context = tmp_context;
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.add_event_dialog, null);
        builder.setView(rootView);

        title_editText = rootView.findViewById(R.id.add_event_title);
        content_editText = rootView.findViewById(R.id.add_event_content);
        type_spinner = rootView.findViewById(R.id.type_spinner);

        if (type != null)
        {
            title_editText.setText(this.title);
            content_editText.setText(this.content);
        }

        // type spinner
        ArrayList<String> type_items = new ArrayList<>();
        type_items.add("휴가");
        type_items.add("근무");
        type_items.add("훈련");

        ArrayAdapter<String> category_adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, type_items);
        //category_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        type_spinner.setAdapter(category_adapter);
        type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //color picker
        rootView.findViewById(R.id.color_picker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ColorPickerPopup.Builder(context)
                        .initialColor(Color.RED) // Set initial color
                        .enableAlpha(true) // Enable alpha slider or not
                        .okTitle("Choose")
                        .cancelTitle("Cancel")
                        .showIndicator(true)
                        .showValue(true)
                        .build()
                        .show(rootView, new ColorPickerPopup.ColorPickerObserver() {
                            @Override
                            public void onColorPicked(int color) {
                                rootView.findViewById(R.id.color_picker).setBackgroundColor(color);
                                selected_color = color;
                            }

                            @Override
                            public void onColor(int color, boolean fromUser) {

                            }
                        });


            }
        });

        // click listener 연결
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mdialogResult.get_result(type_spinner.getSelectedItem().toString(), title_editText.getText().toString(), content_editText.getText().toString(), selected_color);
                dismiss();
            }
        })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
