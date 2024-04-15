package com.example.quocduy;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
public class HorizontalNumberPicker extends LinearLayout {
    private TextView et_number;
    private int min, max;
    private OnValueChangeListener valueChangeListener;
    public HorizontalNumberPicker(Context context, @Nullable AttributeSet
            attrs) {
        super(context, attrs);
        inflate(context, R.layout.horizontal_number_picker_layout, this);
        et_number = findViewById(R.id.et_number);
        final Button btn_less = findViewById(R.id.btn_less);
        btn_less.setOnClickListener(new AddHandler(-1));
        final Button btn_more = findViewById(R.id.btn_more);
        btn_more.setOnClickListener(new AddHandler(1));
    }
    /***
     * HANDLERS
     **/
    private class AddHandler implements OnClickListener {
        final int diff;
        public AddHandler(int diff) {
            this.diff = diff;
        }
        @Override
        public void onClick(View v) {
            int newValue = getValue() + diff;
            if (newValue < min) {
                newValue = min;
            } else if (newValue > max) {
                newValue = max;
            }
            et_number.setText(String.valueOf(newValue));
// Kiểm tra và gọi phương thức callback khi giá trị thay đổi
            if (valueChangeListener != null) {
                valueChangeListener.onValueChange(newValue);
            }
        }
    }
    /***
     * GETTERS & SETTERS
     */
    public int getValue() {
        if (et_number != null) {
            try {
                final String value = et_number.getText().toString();
                return Integer.parseInt(value);
            } catch (NumberFormatException ex) {
                Log.e("HorizontalNumberPicker", ex.toString());
            }
        }
        return 0;
    }
    public void setValue(final int value) {
        if (et_number != null) {
            et_number.setText(String.valueOf(value));
        }
    }
    public int getMin() {
        return min;
    }
    public void setMin(int min) {
        this.min = min;
    }
    public int getMax() {
        return max;
    }
    public void setMax(int max) {
        this.max = max;
    }
    public void setOnValueChangeListener(OnValueChangeListener listener)
    {
        this.valueChangeListener = listener;
    }
    // Interface để lắng nghe sự kiện thay đổi giá trị
    public interface OnValueChangeListener {
        void onValueChange(int newValue);
    }
}
