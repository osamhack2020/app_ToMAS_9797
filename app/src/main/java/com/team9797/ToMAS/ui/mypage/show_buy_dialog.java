package com.team9797.ToMAS.ui.mypage;

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
import android.widget.TextView;
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
import com.team9797.ToMAS.postBoard.register_board_content;
import com.team9797.ToMAS.ui.home.market.register_market_content;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import top.defaults.colorpicker.ColorPickerPopup;


public class show_buy_dialog extends DialogFragment {

    Context context;
    show_buy_dialog_result mdialogResult;
    TextView title_textView;
    TextView date_textView;
    TextView place_textView;
    TextView price_textView;
    String title = null, date = null, place = null;
    int price = 0;


    public interface show_buy_dialog_result{
        void get_result();
    }
    public void setDialogResult(show_buy_dialog_result dialogResult)
    {
        mdialogResult = dialogResult;
    }

    public show_buy_dialog()
    {

    }

    public show_buy_dialog(Context tmp_context)
    {
        context = tmp_context;
    }

    public show_buy_dialog(Context tmp_context, String title, String date, String place, int price)
    {
        this.title = title;
        this.date = date;
        this.place = place;
        this.price = price;
        context = tmp_context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.show_buy_dialog, null);
        builder.setView(rootView);

        title_textView = rootView.findViewById(R.id.title);
        date_textView = rootView.findViewById(R.id.date);
        place_textView = rootView.findViewById(R.id.place);
        price_textView = rootView.findViewById(R.id.price);


        // click listener 연결
        builder.setPositiveButton("구매확정", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mdialogResult.get_result();
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
