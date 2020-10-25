package com.team9797.ToMAS.ui.calendar;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.team9797.ToMAS.R;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Decorate 2 days.
 */
public class RangeDayDecorator implements DayViewDecorator {

  public final HashSet<CalendarDay> list = new HashSet<>();
  private final Drawable drawable;

  public RangeDayDecorator(final Context context) {
    drawable = context.getResources().getDrawable(R.drawable.ic_my_selector);
  }

  @Override
  public boolean shouldDecorate(CalendarDay day) {
    return list.contains(day);
  }

  public void shoulddecorateall(final ArrayList<ArrayList<CalendarDay>> alllist){
    for(int i=0;i<alllist.size();i++){
      for(int k=0;k<alllist.get(i).size();k++){
        shouldDecorate(alllist.get(i).get(k));
      }

    }
  }

  @Override
  public void decorate(DayViewFacade view) {
    view.setSelectionDrawable(drawable);
  }

  /**
   * We're changing the dates, so make sure to call {@linkplain MaterialCalendarView#invalidateDecorators()}
   */
  public void addFirstAndLast(final CalendarDay first, final CalendarDay last) {
    list.clear();
    list.add(first);
    list.add(last);
  }
  public void addalllist(final ArrayList<CalendarDay> dlist){
    for(int i=0;i<dlist.size();i++){
      list.clear();
      list.addAll(dlist);

    }

  }

}

