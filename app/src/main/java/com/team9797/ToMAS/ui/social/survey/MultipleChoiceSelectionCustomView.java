package com.team9797.ToMAS.ui.social.survey;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.team9797.ToMAS.R;

import java.util.ArrayList;

public class MultipleChoiceSelectionCustomView extends LinearLayout {
    EditText selection_editText;
    Button delete_button;
    ArrayList<EditText> multi_chice_selection_list;
    public Context tmp_context;
    public int multiple_choice_index = 0;



    public MultipleChoiceSelectionCustomView(@NonNull Context context) {
        this(context, null);
    }

    public MultipleChoiceSelectionCustomView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        tmp_context = context;
        multi_chice_selection_list = new ArrayList<>();
        inflateViews(context);
    }

    void inflateViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.multiple_choice_selection_customview, this);
        selection_editText = findViewById(R.id.selection_custom_editText);
        delete_button = findViewById(R.id.selection_custom_delete_button);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

}