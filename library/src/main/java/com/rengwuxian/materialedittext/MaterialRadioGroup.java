package com.rengwuxian.materialedittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * Created by lukesimpson on 2/27/15.
 */
public class MaterialRadioGroup extends RadioGroup {

  private TextView radioGroupTitle;
  private String title;

  public MaterialRadioGroup(Context context) {
    super(context);
    init(context, null);
  }

  public MaterialRadioGroup(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  /*
  @Override
  protected void onLayout(boolean b, int i, int i2, int i3, int i4) {
    Log.println(Log.DEBUG, "inflateDebug", "In onLayout");
//    LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
//    inflater.inflate(R.layout.material_radio_group, null);
//    radioGroupTitle = (TextView) findViewById(R.id.radio_group_title); // null
//    radioGroupTitle.setText(title); // NPE
  }
  */

  @Override
  protected void onFinishInflate() {
    Log.println(Log.DEBUG, "inflateDebug", "In onFinishInflate");
    TextView viewGroupTitle = new TextView(getContext());
    viewGroupTitle.setText(title);
    viewGroupTitle.setTextColor(getResources().getColor(R.color.accent_material_light));
    this.addView(viewGroupTitle);
    this.bringToFront();
  }

  private void init(Context context, AttributeSet attrs) {
    TypedArray a = context.obtainStyledAttributes(attrs,
        R.styleable.MaterialRadioGroup);

    final int N = a.getIndexCount();
    for (int i = 0; i < N; ++i) {
      int attr = a.getIndex(i);
      if (attr == R.styleable.MaterialRadioGroup_radioGroupTitle) {
          title = a.getString(attr);
      }
    }
    a.recycle();
  }
}
