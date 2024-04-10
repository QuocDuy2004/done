package com.example.quocduy.PlusMinus;

import android.view.View;
import android.widget.TextView;

public class PlusMinus {

    public static void setupPlusMinus(final TextView plusButton, final TextView minusButton, final TextView numberItem) {
        // Thêm sự kiện click vào nút tăng số lượng
        plusButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Lấy ra giá trị hiện tại của số lượng và tăng lên 1 đơn vị
                int currentValue = Integer.parseInt(numberItem.getText().toString());
                numberItem.setText(String.valueOf(currentValue + 1));
            }
        });

        // Thêm sự kiện click vào nút giảm số lượng
        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy ra giá trị hiện tại của số lượng
                int currentValue = Integer.parseInt(numberItem.getText().toString());
                // Kiểm tra nếu giá trị hiện tại là 1 thì không giảm nữa
                if (currentValue > 1) {
                    numberItem.setText(String.valueOf(currentValue - 1));
                }
            }
        });
    }
}
