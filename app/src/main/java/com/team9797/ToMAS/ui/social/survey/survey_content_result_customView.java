package com.team9797.ToMAS.ui.social.survey;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.team9797.ToMAS.R;

import java.util.ArrayList;

public class survey_content_result_customView extends LinearLayout {
    TextView title_textView;
    RadioGroup type1_radioGroup;
    EditText type2_editText;
    LinearLayout container;
    public int index;
    public int question_type;
    public String title;
    public String answer;
    public ArrayList<String> multi_chioce_question_list;



    public survey_content_result_customView(@NonNull Context context) {
        this(context, null);
    }

    public survey_content_result_customView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateViews(context);
    }
    public survey_content_result_customView(@NonNull Context context, AttributeSet attrs, int type, int count, String tmp_title, ArrayList<String> list, String tmp_answer) {
        super(context, attrs);
        question_type = type;
        multi_chioce_question_list = list;
        index = count;
        title = tmp_title;
        answer = tmp_answer;
        inflateViews(context);
    }

    void inflateViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.survey_content_customview, this);
        container = findViewById(R.id.custom_container);
        title_textView = findViewById(R.id.custom_question_title);

        title_textView.setText(Integer.toString(index)+". " +title);
        if (question_type == 1)
        {// 객관식
            type1_radioGroup = new RadioGroup(context);
            for (int i = 0; i< multi_chioce_question_list.size(); i++)
            {
                RadioButton tmp_radio_button = new RadioButton(context);
                tmp_radio_button.setText(multi_chioce_question_list.get(i));
                tmp_radio_button.setId(View.generateViewId());
                if (i == Integer.parseInt(answer))
                {
                    Log.d("index : ", answer);
                    tmp_radio_button.setChecked(true);
                }
                tmp_radio_button.setEnabled(false);
                type1_radioGroup.addView(tmp_radio_button);
            }
            container.addView(type1_radioGroup);
        }
        else
        {// 주관식
            type2_editText = new EditText(context);
            type2_editText.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            type2_editText.setText(answer);
            type2_editText.setEnabled(false);
            container.addView(type2_editText);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

}