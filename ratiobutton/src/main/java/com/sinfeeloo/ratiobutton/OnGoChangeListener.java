package com.sinfeeloo.ratiobutton;

import android.view.View;

public interface OnGoChangeListener {
    void onAdd(View view, int bigCount, int smallCount, OnCountButtonListener amountListener);

    void onSub(View view, int bigCount, int smallCount, OnCountButtonListener amountListener);

    void onEdit(View view, int bigCount, int smallCount, OnCountButtonListener amountListener);
}