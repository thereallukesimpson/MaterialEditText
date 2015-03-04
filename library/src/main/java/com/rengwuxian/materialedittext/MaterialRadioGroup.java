package com.rengwuxian.materialedittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * RadioGroup in Material Design
 * <p/>
 * author:thereallukesimpson
 * <p/>
 */
public class MaterialRadioGroup extends RadioGroup {

  /**
   * Text for the RadioGroup label
   */
  private String label;

  /**
   * Container for RadioGroup label
   */
  private TextView radioGroupLabel;

  /**
   * Horizontal line used for error message
   */
  private View line;

  /**
   * Container for error message
   */
  private TextView errorText;

  /**
   * ArrayList of all RadioButtons used in MaterialRadioGroup
   */
  private ArrayList<RadioButton> listOfRadioButtons = new ArrayList<RadioButton>();

  int defaultBaseColor;
  int defaultPrimaryColor;

  private boolean hasFocus;

  /**
   * the base color of the line and the texts. default is black.
   */
  private int baseColor;

  /**
   * Default constructor
   * @param context
   */
  public MaterialRadioGroup(Context context) {
    super(context);
    init(context, null);
  }

  /**
   * Constructor with AttributeSet parameter as well as Context
   * @param context
   * @param attrs
   */
  public MaterialRadioGroup(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  /**
   * Initialisations occuring after RadioGroup is inflated
   */
  @Override
  protected void onFinishInflate() {
    radioGroupLabel = new TextView(getContext());
    radioGroupLabel.setText(label);
    radioGroupLabel.setTextColor(baseColor & 0x00ffffff | 0x44000000);
    radioGroupLabel.setTextSize(getResources().getDimension(R.dimen.radio_label_text_size_initial));
    LayoutParams labelTitleParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    this.addViewInLayout(radioGroupLabel, 0, labelTitleParams);

    line = new View(getContext());
    LayoutParams lineLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, 6);
    line.setLayoutParams(lineLayoutParams);
    line.setBackgroundColor(Color.parseColor("#e7492E"));
    line.setVisibility(View.GONE);

    errorText = new TextView(getContext());
    errorText.setTextColor(Color.parseColor("#e7492E"));
    errorText.setVisibility(View.GONE);
    // this.addViewInLayout(line, 2, lineLayoutParams);
    this.addView(line);
    this.addView(errorText);
    this.bringToFront();
    setupRadioButtons();
    setupRadioGroupListener();
  }

  /**
   * Initialises the custom XML "label" attribute for RadioGroup
   * @param context
   * @param attrs
   */
  private void init(Context context, AttributeSet attrs) {
    TypedArray a = context.obtainStyledAttributes(attrs,
        R.styleable.MaterialRadioGroup);

    final int N = a.getIndexCount();
    for (int counter = 0; counter < N; counter++) {
      int attr = a.getIndex(counter);
      if (attr == R.styleable.MaterialRadioGroup_label) {
          label = a.getString(attr);
      }
    }
    a.recycle();

    // retrieve the default baseColor
    TypedValue baseColorTypedValue = new TypedValue();
    context.getTheme().resolveAttribute(android.R.attr.windowBackground, baseColorTypedValue, true);
    defaultBaseColor = Colors.getBaseColor(baseColorTypedValue.data);

    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MaterialEditText);
    baseColor = typedArray.getColor(R.styleable.MaterialEditText_baseColor, defaultBaseColor);

    // retrieve the default primaryColor
    TypedValue primaryColorTypedValue = new TypedValue();
    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        context.getTheme().resolveAttribute(android.R.attr.colorPrimary, primaryColorTypedValue, true);
        defaultPrimaryColor = primaryColorTypedValue.data;
      } else {
        throw new RuntimeException("SDK_INT less than LOLLIPOP");
      }
    } catch (Exception e) {
      try {
        int colorPrimaryId = getResources().getIdentifier("colorPrimary", "attr", getContext().getPackageName());
        if (colorPrimaryId != 0) {
          context.getTheme().resolveAttribute(colorPrimaryId, primaryColorTypedValue, true);
          defaultPrimaryColor = primaryColorTypedValue.data;
        } else {
          throw new RuntimeException("colorPrimary not found");
        }
      } catch (Exception e1) {
        defaultPrimaryColor = defaultBaseColor;
      }
    }
  }

  /**
   * Calculate number of RadioButtons in group and initialise.
   */
  public void setupRadioButtons() {
    final int childCount = this.getChildCount();
    listOfRadioButtons = new ArrayList<RadioButton>();
    for (int counter = 0; counter < childCount; counter++) {
      final View o = this.getChildAt(counter);
      if (o instanceof RadioButton) {
        listOfRadioButtons.add((RadioButton)o);
        /**
         * setFocusableInTouchMode is required to highlight the RadioGroup label.
         * <p/>
         * It also facilitates good flow using android:imeOptions="actionNext" on RadioButtons
         */
        o.setFocusableInTouchMode(true);
        o.setOnFocusChangeListener(new OnFocusChangeListener() {
          @Override
          public void onFocusChange(View view, boolean b) {
            hasFocus = false;
            for (int counter2 = 0; counter2 < listOfRadioButtons.size(); counter2++) {
              if(listOfRadioButtons.get(counter2).isFocused()) {
                radioGroupLabel.setTextSize(getResources().getDimension(R.dimen.radio_label_text_size_small));
                radioGroupLabel.setTextColor(defaultPrimaryColor);
                hasFocus = true;
              }
            }
            if(!hasFocus) {
              radioGroupLabel.setTextColor(defaultBaseColor);
              if(!validate()) {
                radioGroupLabel.setTextSize(getResources().getDimension(R.dimen.radio_label_text_size_initial));
              }
            }
          }
        });
      }
    }
  }

  /**
   * Set an error message. If error parameter is null the error is cleared.
   * @param error
   */
  public void setError(CharSequence error) {
    if(error != null) {
      errorText.setVisibility(View.VISIBLE);
      errorText.setText(error);
      line.setVisibility(View.VISIBLE);
    } else {
      errorText.setVisibility(View.GONE);
      line.setVisibility(View.GONE);
    }
  }

  /**
   * For validity, check if one of the RadioGroup's RadioButtons is checked.
   */
  public boolean validate() {
    boolean isValid = false;
    for (int counter = 0; counter < listOfRadioButtons.size(); counter++) {
      if (listOfRadioButtons.get(counter).isChecked()) {
        isValid = true;
      }
    }
    return isValid;
  }

  public void setupRadioGroupListener() {
    this.setOnCheckedChangeListener(new OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if(validate()) {
          setError(null);
        }
      }
    });
  }
}
