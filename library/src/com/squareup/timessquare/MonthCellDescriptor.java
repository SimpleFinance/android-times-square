// Copyright 2012 Square, Inc.

package com.squareup.timessquare;

import java.util.Date;

/** Describes the state of a particular date cell in a {@link MonthView}. */
class MonthCellDescriptor {
  public enum PeriodState {
    NONE, FIRST, MIDDLE, LAST
  }

  private final Date date;
  private final int value;
  private boolean isSelected;
  private boolean isCurrentMonth;
  private final boolean isToday;
  private final boolean isSelectable;
  private PeriodState periodState;

  MonthCellDescriptor(Date date, boolean selectable, boolean selected,
      boolean currentMonth, boolean today, int value, PeriodState periodState) {
    this.date = date;
    isSelectable = selectable;
    isSelected = selected;
    isCurrentMonth = currentMonth;
    isToday = today;
    this.value = value;
    this.periodState = periodState;
  }

  public Date getDate() {
    return date;
  }

  public boolean isSelectable() {
    return isSelectable;
  }

  public boolean isSelected() {
    return isSelected;
  }

  public void setSelected(boolean selected) {
    isSelected = selected;
  }

  public boolean isCurrentMonth() {
    return isCurrentMonth;
  }

  public void setCurrentMonth(boolean currentMonth) {
    isCurrentMonth = currentMonth;
  }

  public boolean isToday() {
    return isToday;
  }

  public PeriodState getPeriodState() {
    return periodState;
  }

  public void setPeriodState(PeriodState periodState) {
    this.periodState = periodState;
  }

  public int getValue() {
    return value;
  }

  @Override public String toString() {
    return "MonthCellDescriptor{"
        + "date="
        + date
        + ", value="
        + value
        + ", isSelected="
        + isSelected
        + ", isCurrentMonth="
        + isCurrentMonth
        + ", isToday="
        + isToday
        + ", isSelectable="
        + isSelectable
        + ", periodState="
        + periodState
        + '}';
  }
}
