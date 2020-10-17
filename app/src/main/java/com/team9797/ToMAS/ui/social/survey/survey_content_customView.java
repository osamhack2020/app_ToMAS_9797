package com.team9797.ToMAS.ui.social.survey;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.team9797.ToMAS.R;

import java.util.ArrayList;

public class survey_content_customView extends LinearLayout {
    TextView num_textView;
    RadioGroup type1_radioGroup;
    EditText type2_editText;
    LinearLayout container;
    public int question_type;



    public survey_content_customView(@NonNull Context context) {
        this(context, null);
    }

    public survey_content_customView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateViews(context);
    }

    void inflateViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.survey_content_customview, this);
        num_textView = findViewById(R.id.custom_number);
        container = findViewById(R.id.custom_container);

        if (question_type == 1)
        {

        }
        else
        {

        }


    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

}