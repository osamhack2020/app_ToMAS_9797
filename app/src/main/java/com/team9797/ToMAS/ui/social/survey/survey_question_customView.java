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
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.team9797.ToMAS.R;

import java.util.ArrayList;

import static java.lang.Math.max;

public class survey_question_customView extends LinearLayout {
    TextView num_textView;
    EditText question_editText;
    RadioButton radioButton1;
    RadioButton radioButton2;
    Button delete_button;
    LinearLayout container;
    ArrayList<multiple_choice_selection_customView> multi_chice_selection_list;
    public Context tmp_context;
    public int index;



    public survey_question_customView(@NonNull Context context) {
        this(context, null);
    }

    public survey_question_customView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        tmp_context = context;
        multi_chice_selection_list = new ArrayList<>();
        inflateViews(context);
    }

    void inflateViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.survey_question_customview, this);
        num_textView = findViewById(R.id.custom_number);
        question_editText = findViewById(R.id.custom_question_editText);
        radioButton1 = findViewById(R.id.custom_question_radio1);
        radioButton2 = findViewById(R.id.custom_question_radio2);
        container = findViewById(R.id.custom_container);
        delete_button = findViewById(R.id.custom_delete_button);


        radioButton1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                container.removeAllViews();
                Button add_button = new Button(tmp_context);
                add_button.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                add_button.setText("객관식 보기 추가");
                add_button.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        multiple_choice_selection_customView tmp_selection_customView = new multiple_choice_selection_customView(tmp_context);
                        tmp_selection_customView.delete_button.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                container.removeView(tmp_selection_customView);
                                multi_chice_selection_list.remove(tmp_selection_customView.multiple_choice_index);
                                recount();
                            }
                        });
                        container.addView(tmp_selection_customView, multi_chice_selection_list.size());
                        multi_chice_selection_list.add(tmp_selection_customView);
                        recount();
                    }
                });
                container.addView(add_button);
            }
        });
        radioButton2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                container.removeAllViews();
                EditText tmp_edit = new EditText(tmp_context);
                tmp_edit.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                container.addView(tmp_edit);
            }
        });

    }

    public void recount()
    {
        for (int i = 0 ; i< multi_chice_selection_list.size(); i++)
        {
            multi_chice_selection_list.get(i).multiple_choice_index = i;
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

}