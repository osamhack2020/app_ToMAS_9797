package com.team9797.ToMAS.ui.calendar;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class CalendarFragment extends Fragment implements OnRangeSelectedListener{


    MaterialCalendarView calendarView;
    MainActivity mainActivity;
    Button btn_calendar;
    RangeDayDecorator basedecorator;
    RangeDayDecorator rangedecorator;
    ArrayList<CalendarDay> datelist;


    ArrayList<ArrayList<CalendarDay>> alldateList;
    int count=0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_calendar, container, false);
        mainActivity = (MainActivity)getActivity();
        calendarView = root.findViewById(R.id.calendarView);
        btn_calendar = root.findViewById(R.id.btn_calendar);
        basedecorator = new RangeDayDecorator(mainActivity); //db에서 받은 기존일정들을 그려준다


        calendarView.setOnRangeSelectedListener(this);
        // calendarView.addDecorator(basedecorator);
        rangedecorator= new RangeDayDecorator(mainActivity);
        calendarView.addDecorator(rangedecorator);

        alldateList=new ArrayList<ArrayList<CalendarDay>>();
        datelist= new ArrayList<CalendarDay>();





        calendarView.invalidateDecorators();
        rangedecorator.shoulddecorateall(alldateList);

        btn_calendar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // 일정추가에서 받은 날짜들을 그려준다.

                alldateList.add(count,datelist);

                for(int i=0;i<alldateList.size();i++){
                    rangedecorator.addalllist(alldateList.get(i));
                    Log.d("test",alldateList.get(i).toString());
                }
                Log.d("test",alldateList.get(count).toString());

                count++;

                calendarView.invalidateDecorators();
                //rangedecorator.list.clear();

            }
        });
        return root;
    }



    @Override
    public void onRangeSelected(@NonNull MaterialCalendarView widget, @NonNull List<CalendarDay> dates) {
        Log.d("test","calendar test");

        if (dates.size() > 0) {
            for (int i = 0; i < dates.size(); i++)
            {
                datelist.add(i,dates.get(i));
                Log.d("test",datelist.get(i).toString());
            }
            // calendarView.invalidateDecorators();

        }



    }



}