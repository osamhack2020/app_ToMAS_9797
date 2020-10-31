package com.team9797.ToMAS.ui.mypage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.team9797.ToMAS.R;


public class ShowBuyDialog extends DialogFragment {

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

    public ShowBuyDialog()
    {

    }

    public ShowBuyDialog(Context tmp_context)
    {
        context = tmp_context;
    }

    public ShowBuyDialog(Context tmp_context, String title, String date, String place, int price)
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

        title_textView = rootView.findViewById(R.id.title);
        date_textView = rootView.findViewById(R.id.date);
        place_textView = rootView.findViewById(R.id.place);
        price_textView = rootView.findViewById(R.id.price);

        title_textView.setText("제목 : " + this.title);
        date_textView.setText("마감일 : " + this.date);
        place_textView.setText("장소 : " + this.place);
        price_textView.setText("가격 : " + Integer.toString(this.price));


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
        builder.setView(rootView);
        return builder.create();
    }
}
