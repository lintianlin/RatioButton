package com.sinfeeloo.ratiobutton;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * EditCountDialog
 *
 * @author: mhj
 * @date: 2018/3/22 9:50
 * <p>
 * 修改数量
 */
public class EditCountDialog extends Dialog {
    private Context context;
    private RatiolBtn bigSmallBtn;
    private int count;
    private OnDialogListener listener;
    private int position;
    private String unit;

    public EditCountDialog(@NonNull Context context) {
        super(context);
    }

    public EditCountDialog(@NonNull Context context, @StyleRes int themeResId, int count, String unit, int position, OnDialogListener listener) {
        super(context, themeResId);
        this.context = context;
        this.listener = listener;
        this.count = count;
        this.position = position;
        this.unit = unit;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_set_count);
        bigSmallBtn = findViewById(R.id.bsb_view);
        bigSmallBtn.setCount(count, 0);
        bigSmallBtn.setSmallUnit(unit);
        bigSmallBtn.setSelectionFocus(1);//设置光标位置

        findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoftKeyboardUtils.hideSoftInput(context, bigSmallBtn.getEditText(1));
                dismiss();
            }
        });
        findViewById(R.id.ok_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onEditCount(position, bigSmallBtn.getSmallCount());
                SoftKeyboardUtils.hideSoftInput(context, bigSmallBtn.getEditText(1));
                dismiss();
            }
        });
    }

    public void showSoftKeyBoard() {
        if (bigSmallBtn.getEditText(1) != null) {
            //设置可获得焦点
            bigSmallBtn.getEditText(1).setFocusable(true);
            bigSmallBtn.getEditText(1).setFocusableInTouchMode(true);
            //请求获得焦点
            bigSmallBtn.getEditText(1).requestFocus();
            //调用系统输入法
            InputMethodManager inputManager = (InputMethodManager) bigSmallBtn.getEditText(1)
                    .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(bigSmallBtn.getEditText(1), 0);
        }
    }

    public interface OnDialogListener {
        void onEditCount(int position, int count);
    }
}
