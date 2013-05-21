// Copyright 2012 Square, Inc.
package com.squareup.timessquare;

import java.text.DateFormat;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MonthView extends LinearLayout {
  private CalendarGridView grid;
  private Listener listener;

  public static MonthView create(ViewGroup parent, LayoutInflater inflater,
      DateFormat weekdayNameFormat, Listener listener) {
    final MonthView view = (MonthView) inflater.inflate(R.layout.month, parent, false);
    view.listener = listener;
    return view;
  }

  public MonthView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    grid = (CalendarGridView) findViewById(R.id.calendar_grid);
  }

  public void init(MonthDescriptor month, List<List<MonthCellDescriptor>> cells) {
    Logr.d("Initializing MonthView for %s", month);
    long start = System.currentTimeMillis();

    final int numRows = cells.size();
    for (int i = 0; i < 6; i++) {
      CalendarRowView weekRow = (CalendarRowView) grid.getChildAt(i);
      weekRow.setListener(listener);
      if (i < numRows) {
        weekRow.setVisibility(VISIBLE);
        List<MonthCellDescriptor> week = cells.get(i);
        for (int c = 0; c < week.size(); c++) {
          MonthCellDescriptor cell = week.get(c);
          CalendarCellView cellView = (CalendarCellView) weekRow.getChildAt(c);

          cellView.setText(Integer.toString(cell.getValue()));
          cellView.setEnabled(true);
          cellView.setSelectable(cell.isSelectable());
          cellView.setSelected(cell.isSelected());
          cellView.setToday(cell.isToday());
          cellView.setPeriodState(cell.getPeriodState());
          cellView.setTag(cell);
        }
      } else {
        weekRow.setVisibility(GONE);
      }
    }
    Logr.d("MonthView.init took %d ms", System.currentTimeMillis() - start);
  }

  public interface Listener {
    void handleClick(MonthCellDescriptor cell);
  }
}
