package com.sinfeeloo.ratiobutton;

/**
 * 请求网络完成后的回掉接口
 */

public interface OnCountButtonListener {
    void onAddSuccess();

    void onDelSuccess();

    void onEditSuccess(int count);

    void unForbidden();
}
