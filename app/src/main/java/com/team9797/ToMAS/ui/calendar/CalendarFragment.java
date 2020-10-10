package com.team9797.ToMAS.ui.calendar;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.team9797.ToMAS.MainActivity;
import com.team9797.ToMAS.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarFragment extends Fragment {


    CalendarView calendarView;
    MainActivity mainActivity;
    Button btn_calendar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_calendar, container, false);
        mainActivity = (MainActivity)getActivity();
        calendarView = (CalendarView) root.findViewById(R.id.calendarView);
        btn_calendar = root.findViewById(R.id.btn_calendar);

    /*
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                List<Calendar> selectedDates = calendarView.getSelectedDates();
                if (selectedDates.size() == 1)
                {
                    Log.d("QQQ", "one clicked");
                }
                else
                {
                    Log.d("QQQ", "many clicked");
                }
                for (int i = 0; i< selectedDates.size(); i++)
                    Log.d("AAA", selectedDates.get(i).toString());
            }
        });

     */
        List<EventDay> events = new ArrayList<>();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.DAY_OF_MONTH, 2);
        events.add(new EventDay(calendar1, R.drawable.sample_icon_2));
        calendarView.setEvents(events);
        /*
        btn_calendar.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Calendar> selectedDates = calendarView.getSelectedDates();
                List<EventDay> events = new ArrayList<>();
                Log.d("QQ", Integer.toString(selectedDates.size()));
                for (int i = 0; i< selectedDates.size(); i++) {
                    events.add(new EventDay(selectedDates.get(i), R.drawable.sample_icon_2));
                    Log.d("QQ", "AA");
                }
                calendarView.setEvents(events);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.detach(CalendarFragment.this).attach(CalendarFragment.this).commit();
            }
        });

         */

        return root;
    }
}