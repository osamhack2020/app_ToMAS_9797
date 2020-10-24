package com.team9797.ToMAS.ui.calendar;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateLongClickListener;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;
import com.team9797.ToMAS.MainActivity;
import com.team9797.ToMAS.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarFragment extends Fragment implements OnRangeSelectedListener{


    MaterialCalendarView calendarView;
    MainActivity mainActivity;
    Button btn_calendar;
    RangeDayDecorator decorator;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_calendar, container, false);
        mainActivity = (MainActivity)getActivity();
        calendarView = root.findViewById(R.id.calendarView);
        btn_calendar = root.findViewById(R.id.btn_calendar);
        decorator = new RangeDayDecorator(mainActivity);
        calendarView.setOnRangeSelectedListener(this);
        calendarView.addDecorator(decorator);


        return root;
    }

    @Override
    public void onRangeSelected(@NonNull MaterialCalendarView widget, @NonNull List<CalendarDay> dates) {

        if (dates.size() > 0) {
            for (int i = 0; i < dates.size(); i++)
            {
                
            }
            calendarView.invalidateDecorators();
        }

    }
}