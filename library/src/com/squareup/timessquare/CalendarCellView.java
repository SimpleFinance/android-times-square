// Copyright 2013 Square, Inc.

package com.squareup.timessquare;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.squareup.timessquare.MonthCellDescriptor.PeriodState;

public class CalendarCellView extends TextView {

  private static final int[] STATE_SELECTABLE = {
      R.attr.state_selectable
  };
  private static final int[] STATE_CURRENT_MONTH = {
      R.attr.state_current_month
  };
  private static final int[] STATE_TODAY = {
      R.attr.state_today
  };
  private static final int[] STATE_PERIOD_FIRST = {
      R.attr.state_period_first
  };
  private static final int[] STATE_PERIOD_MIDDLE = {
      R.attr.state_period_middle
  };
  private static final int[] STATE_PERIOD_LAST = {
      R.attr.state_period_last
  };

  private static LruCache<String, Typeface> typefaceCache =
      new LruCache<String, Typeface>(12);

  private boolean isSelectable = false;
  private boolean isCurrentMonth = false;
  private boolean isToday = false;
  private PeriodState periodState = PeriodState.NONE;

  private String typefaceName;
  private ColorStateList shadowColor;
  private float shadowDx;
  private float shadowDy;
  private float shadowRadius;

  public static Typeface getTypeface(Context context, String family) {
    Typeface typeface = typefaceCache.get(family);

    if (typeface == null) {
      AssetManager am = context.getApplicationContext().getAssets();
      typeface = Typeface.createFromAsset(am,
          String.format("fonts/%s-Family.otf", family));

      // Cache the Typeface object
      typefaceCache.put(family, typeface);
    }

    return typeface;
  }

  public CalendarCellView(Context context) {
    super(context);
  }

  public CalendarCellView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs, 0);

  }

  public CalendarCellView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context, attrs, defStyle);
  }

  private void init(Context context, AttributeSet attrs, int defStyle) {
    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CalendarCellView, defStyle,
        0);
    final int attributeCount = a.getIndexCount();
    for (int i = 0; i < attributeCount; i++) {
      int attr = a.getIndex(i);

      switch (attr) {
        case R.styleable.CalendarCellView_calendarCellTypeface:
          typefaceName = a.getString(attr);
          if (!TextUtils.isEmpty(typefaceName) && !isInEditMode()) {
            setTypeface(getTypeface(context, typefaceName));
          }
          break;

        case R.styleable.CalendarCellView_shadowColors:
          shadowColor = a.getColorStateList(attr);
          break;

        case R.styleable.CalendarCellView_shadowDx:
          shadowDx = a.getDimension(attr, 0);
          break;

        case R.styleable.CalendarCellView_shadowDy:
          shadowDy = a.getDimension(attr, 0);
          break;

        case R.styleable.CalendarCellView_shadowRadius:
          shadowRadius = a.getDimension(attr, 0);
          break;
      }
    }
    a.recycle();
    updateShadowColor();
  }

  public void setSelectable(boolean isSelectable) {
    this.isSelectable = isSelectable;
    refreshDrawableState();
  }

  public void setCurrentMonth(boolean isCurrentMonth) {
    this.isCurrentMonth = isCurrentMonth;
    refreshDrawableState();
  }

  public void setToday(boolean isToday) {
    this.isToday = isToday;
    refreshDrawableState();
  }

  public void setPeriodState(PeriodState periodState) {
    this.periodState = periodState;
    refreshDrawableState();
  }

  @Override protected int[] onCreateDrawableState(int extraSpace) {
    final int[] drawableState = super.onCreateDrawableState(extraSpace + 4);

    if (isSelectable) {
      mergeDrawableStates(drawableState, STATE_SELECTABLE);
    }

    if (isCurrentMonth) {
      mergeDrawableStates(drawableState, STATE_CURRENT_MONTH);
    }

    if (isToday) {
      mergeDrawableStates(drawableState, STATE_TODAY);
    }

    if (periodState == PeriodState.FIRST) {
      mergeDrawableStates(drawableState, STATE_PERIOD_FIRST);
    } else if (periodState == PeriodState.MIDDLE) {
      mergeDrawableStates(drawableState, STATE_PERIOD_MIDDLE);
    } else if (periodState == PeriodState.LAST) {
      mergeDrawableStates(drawableState, STATE_PERIOD_LAST);
    }

    return drawableState;
  }

  @Override protected void drawableStateChanged() {
    super.drawableStateChanged();
    updateShadowColor();
  }

  private void updateShadowColor() {
    if (shadowColor != null) {
      setShadowLayer(shadowRadius, shadowDx, shadowDy,
          shadowColor.getColorForState(getDrawableState(), 0));
      invalidate();
    }
  }
}
