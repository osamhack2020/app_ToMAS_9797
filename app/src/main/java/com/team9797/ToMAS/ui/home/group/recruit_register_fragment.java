package com.team9797.ToMAS.ui.home.group;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.team9797.ToMAS.MainActivity;
import com.team9797.ToMAS.R;

public class recruit_register_fragment extends Fragment {

    Spinner spinner_category;
    Spinner spinner_num_people;
    MainActivity mainActivity;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.recruit_register, container, false);
        mainActivity = (MainActivity)getActivity();
        //카테고리 spinner 설정
        spinner_category = (Spinner) root.findViewById(R.id.recruit_register_category);
        ArrayAdapter category_adapter = ArrayAdapter.createFromResource(mainActivity, R.array.list_exercise, android.R.layout.simple_spinner_item);
        category_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_category.setAdapter(category_adapter);
        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            //need to fix
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner_num_people = (Spinner) root.findViewById(R.id.recruit_register_num_people);
        ArrayAdapter num_people_adapter = ArrayAdapter.createFromResource(mainActivity, R.array.list_num_people, android.R.layout.simple_spinner_item);
        num_people_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_num_people.setAdapter(num_people_adapter);
        spinner_num_people.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //need to fix
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return root;
    }
}