package com.sinfeeloo.ratiobutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * BigSmallBtn
 *
 * @author: mhj
 * @date: 2018/12/7 10:58
 * <p>
 * 自定义组件：购买数量，带减少增加按钮,可以进行大小单位转换
 */
public class RatiolBtn extends LinearLayout implements View.OnClickListener, OnCountButtonListener {
    Context context;
    private int bigCount = 0; //大单位数量
    private int smallCount = 0; //小单位数量
    private int minCount = 0; //最小数量
    private int maxCount = 9999; //最大数量
    private int ratio = 0;//大小单位转换率
    //先请求网络，后刷新页面使用此监听
    private OnGoChangeListener onGoChangeListener;
    //不请求网络，直接刷新页面使用此监听
    private OnCountChangedListener onCountChangeListener;
    //数量为最小值的监听，只有希望监听到点击减到最小值时才使用。
    private OnMinListener onMinListener;
    //exittext的点击监听，可用于点击弹出弹窗修改数量
    private OnEditListener onEditListener;
    int textSizeTv;
    private EditText tvBigCount;
    private Button btnBigAdd;
    private Button btnBigSub;
    private EditText tvSmallCount;
    private Button btnSmallAdd;
    private Button btnSmallSub;
    //是否可以点击编辑
    private boolean editable;
    //加减按钮是否可用
    private boolean isEnable = true;
    private LinearLayout llBigUnitCount;
    private TextView tvSmallUnit;
    private TextView tvBigUnit;
    private LinearLayout llBtnSmallView;
    private LinearLayout llBtnBigView;
    private boolean haveBigUnit;
    private final String ERRORTIP = "商品数量不能超过最大库存";
    private String bigUnit;
    private String smallUnit;


    public RatiolBtn(Context context) {
        this(context, null);
    }

    public RatiolBtn(final Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_ratio_btn, this);
        llBigUnitCount = findViewById(R.id.ll_big_unit_count);
        llBtnSmallView = findViewById(R.id.ll_btn_small_view);
        llBtnBigView = findViewById(R.id.ll_btn_big_view);
        tvBigCount = findViewById(R.id.tv_big_count);
        btnBigAdd = findViewById(R.id.btn_big_add);
        btnBigSub = findViewById(R.id.btn_big_sub);
        tvSmallCount = findViewById(R.id.tv_small_count);
        btnSmallAdd = findViewById(R.id.btn_small_add);
        btnSmallSub = findViewById(R.id.btn_small_sub);
        tvSmallUnit = findViewById(R.id.tv_small_unit);
        tvBigUnit = findViewById(R.id.tv_big_unit);
        btnBigAdd.setOnClickListener(this);
        btnBigSub.setOnClickListener(this);
        btnSmallAdd.setOnClickListener(this);
        btnSmallSub.setOnClickListener(this);


        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.RatiolBtn);
        int widthBtn = obtainStyledAttributes.getDimensionPixelSize(R.styleable.RatiolBtn_btnWidth, LayoutParams.WRAP_CONTENT);
        int heightBtn = obtainStyledAttributes.getDimensionPixelSize(R.styleable.RatiolBtn_btnHeight, LayoutParams.WRAP_CONTENT);
        int widthTv = obtainStyledAttributes.getDimensionPixelSize(R.styleable.RatiolBtn_tvWidth, 80);
        textSizeTv = obtainStyledAttributes.getDimensionPixelSize(R.styleable.RatiolBtn_tvTextSize, 0);
        int margin = obtainStyledAttributes.getDimensionPixelSize(R.styleable.RatiolBtn_margin, 10);
        int textSizeBtn = obtainStyledAttributes.getDimensionPixelSize(R.styleable.RatiolBtn_btnTextSize, 0);
        editable = obtainStyledAttributes.getBoolean(R.styleable.RatiolBtn_editable, false);
        haveBigUnit = obtainStyledAttributes.getBoolean(R.styleable.RatiolBtn_haveBigUnit, false);
        minCount = obtainStyledAttributes.getInteger(R.styleable.RatiolBtn_minAmount, 0);
        obtainStyledAttributes.recycle();

        //设置大单位框距离小单位框的margin
        LayoutParams bigUnitParams = new LayoutParams(llBigUnitCount.getLayoutParams());
        bigUnitParams.setMargins(0, 0, 0, margin);

        LayoutParams btnParams = new LayoutParams(widthBtn, heightBtn);
        llBtnSmallView.setLayoutParams(btnParams);
        llBtnBigView.setLayoutParams(btnParams);
        if (textSizeBtn != 0) {
            btnBigAdd.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeBtn);
            btnBigSub.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeBtn);
            btnSmallAdd.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeBtn);
            btnSmallSub.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeBtn);
        }

        LayoutParams textParams = new LayoutParams(widthTv, LayoutParams.MATCH_PARENT);
        tvBigCount.setLayoutParams(textParams);
        tvSmallCount.setLayoutParams(textParams);
        if (textSizeTv != 0) {
            tvBigCount.setTextSize(textSizeTv);
            tvSmallCount.setTextSize(textSizeTv);
        }

        if (haveBigUnit) {//如果有大单位
            llBigUnitCount.setVisibility(VISIBLE);
        } else {
            llBigUnitCount.setVisibility(GONE);
        }

        //是否可以直接编辑()
        if (!editable) {
            //禁用editetext
            tvBigCount.setKeyListener(null);
            tvBigCount.setOnClickListener(this);
            tvSmallCount.setKeyListener(null);
            tvSmallCount.setOnClickListener(this);
        } else {
            //大单位编辑
            tvBigCount.addTextChangedListener(new SimpleTextWather() {
                @Override
                public void afterTextChanged(Editable editable) {
                    String str = tvBigCount.getText().toString();
                    //判空和判断是不是数字
                    if (!TextUtils.isEmpty(str) && WorkUtils.isInteger(str)) {
                        bigCount = Integer.parseInt(str);
                    } else {
                        bigCount = minCount;
                    }
                    if (bigCount == 0) {
                        btnBigSub.setEnabled(false);
                    } else {
                        btnBigSub.setEnabled(true);
                    }
                }
            });
        }

        //小单位编辑
        tvSmallCount.addTextChangedListener(new SimpleTextWather() {
            @Override
            public void afterTextChanged(Editable editable) {
                String str = tvSmallCount.getText().toString();
                //判空和判断是不是数字
                if (!TextUtils.isEmpty(str) && WorkUtils.isInteger(str)) {
                    smallCount = Integer.parseInt(str);
                } else {
                    smallCount = minCount;
                }
                if (smallCount == 0) {
                    btnSmallSub.setEnabled(false);
                } else {
                    btnSmallSub.setEnabled(true);
                }
            }
        });


    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_big_count) {//大单位数量显示
            //先请求网络，成功后在刷新页面
            if (null != onGoChangeListener) {
                onGoChangeListener.onEdit(this, bigCount, smallCount, this);
            } else {
                showCountEditDialog(2, bigCount, bigUnit);
            }
            return;
        } else if (i == R.id.btn_big_sub) {//大单位 减
            if (bigCount > minCount) {
                //先请求网络，成功后在刷新页面
                if (null != onGoChangeListener) {
                    //判断按钮是否可用
                    if (!isEnable) {
                        return;
                    }
                    tvBigCount.setText(String.valueOf(bigCount - 1));
                    //去执行网络操作
                    onGoChangeListener.onSub(this, bigCount - 1, smallCount, this);
                    isEnable = false;
                } else {
                    //未传入相应接口，直接改变，刷新页面
                    bigCount--;
                    setCount(smallCount, bigCount);
//                    tvBigCount.setText(String.valueOf(bigCount));
//                    if (bigCount == 0) {
//                        btnBigSub.setEnabled(false);
//                    }
                }
            } else {
                //小于最小值的时候，不可减
                if (null != onMinListener) {
                    //当数量为最小值的时候
                    onMinListener.onMin();
                }
                return;
            }

        } else if (i == R.id.btn_big_add) {//大单位加

            if (checkStorage(bigCount + 1, smallCount)) {
                ToastUtils.showToast(context, ERRORTIP);
            } else {
                //先请求网络，成功后在刷新页面
                if (null != onGoChangeListener) {
                    //判断按钮是否可用
                    if (!isEnable) {
                        return;
                    }
                    tvBigCount.setText(String.valueOf(bigCount + 1));
                    onGoChangeListener.onAdd(this, bigCount + 1, smallCount, this);
                    isEnable = false;
                } else {
                    //未传入相应接口，直接改变，刷新页面
                    bigCount++;
                    setCount(smallCount, bigCount);
//                    tvBigCount.setText(String.valueOf(bigCount));
                }
            }
//            if (bigCount == minCount + 1) {
//                btnBigSub.setEnabled(true);
//            }
        } else if (i == R.id.tv_small_count) {//小单位数量显示
            //先请求网络，成功后在刷新页面
            if (null != onGoChangeListener) {
                onGoChangeListener.onEdit(this, bigCount, smallCount, this);
            } else {
                showCountEditDialog(1, smallCount, smallUnit);
            }

            return;
        } else if (i == R.id.btn_small_sub) {
            if (smallCount > minCount) {
                //先请求网络，成功后在刷新页面
                if (null != onGoChangeListener) {
                    //判断按钮是否可用
                    if (!isEnable) {
                        return;
                    }
                    tvSmallCount.setText(String.valueOf(smallCount - 1));
                    //去执行网络操作
                    onGoChangeListener.onSub(this, bigCount, smallCount - 1, this);
                    isEnable = false;
                } else {
                    //未传入相应接口，直接改变，刷新页面
                    smallCount--;
                    setCount(smallCount, bigCount);
//                    tvSmallCount.setText(String.valueOf(smallCount));
//                    if (smallCount == 0) {
//                        btnSmallSub.setEnabled(false);
//                    }
                }
            } else {
                //小于最小值的时候，不可减
                if (null != onMinListener) {
                    //当数量为0的时候
                    onMinListener.onMin();
                }
                return;
            }

        } else if (i == R.id.btn_small_add) {
            if (checkStorage(bigCount, smallCount + 1)) {
                ToastUtils.showToast(context, ERRORTIP);
            } else {
                //先请求网络，成功后在刷新页面
                if (null != onGoChangeListener) {
                    //判断按钮是否可用
                    if (!isEnable) {
                        return;
                    }
                    tvBigCount.setText(String.valueOf(smallCount + 1));
                    onGoChangeListener.onAdd(this, bigCount, smallCount + 1, this);
                    isEnable = false;
                } else {
                    //未传入相应接口，直接改变，刷新页面
                    smallCount++;
                    setCount(smallCount, bigCount);
//                    if (ratio > 0) {//有大单位
//                        if (smallCount >= ratio) {
//                            smallCount = smallCount % ratio;
//                            bigCount = bigCount + 1;
//                            tvBigCount.setText(String.valueOf(bigCount));
//                        }
//
//                    }
//                    tvSmallCount.setText(String.valueOf(smallCount));
                }
            }

//            if (smallCount == minCount + 1) {
//                btnSmallSub.setEnabled(true);
//            }
        }


    }

    /**
     * 弹出修改数量弹框
     *
     * @param count
     */
    private void showCountEditDialog(final int type, int count, String unit) {
        EditCountDialog editCountDialog = new EditCountDialog(context, R.style.DialogStyle, count, unit, 0, new EditCountDialog.OnDialogListener() {
            @Override
            public void onEditCount(int position, int count) {
                if (type == 1) {//小单位修改
                    if (count == 0) {
                        btnSmallSub.setEnabled(false);
                    } else {
                        btnSmallSub.setEnabled(true);
                    }
                    setCount(count, bigCount);
                } else {//大单位修改
                    if (count == 0) {
                        btnBigSub.setEnabled(false);
                    } else {
                        btnBigSub.setEnabled(true);
                    }
                    setCount(smallCount, count);
                }


            }
        });
        editCountDialog.show();
        //显示软键盘
        showKeyboardForDialog(editCountDialog);
    }

    /**
     * 检查库存是否超出
     *
     * @return
     */
    public boolean checkStorage(int tempBig, int tempSmall) {
        return tempBig * ratio + tempSmall > maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    /**
     * 没有监听的数量设置方法
     * @param tempSmallCount
     * @param tempBigCount
     */
    public void setDisplayCount(int tempSmallCount, int tempBigCount) {
        if (ratio > 0) {//有大单位
            if (tempBigCount * ratio + tempSmallCount > maxCount) {
                ToastUtils.showToast(context, ERRORTIP);
                bigCount = maxCount / ratio;//取整运算
                smallCount = maxCount % ratio;//取余运算
            } else {
                if (tempSmallCount > ratio) {
                    smallCount = tempSmallCount % ratio;
                    bigCount = tempBigCount + tempSmallCount / ratio;
                } else {
                    smallCount = tempSmallCount;
                    bigCount = tempBigCount;
                }
            }
        } else {
            smallCount = tempSmallCount;
            bigCount = tempBigCount;
        }
        if (smallCount == 0) {
            btnSmallSub.setEnabled(false);
        } else {
            btnSmallSub.setEnabled(true);
        }
        if (bigCount == 0) {
            btnBigSub.setEnabled(false);
        } else {
            btnBigSub.setEnabled(true);
        }
        tvSmallCount.setText(String.valueOf(smallCount));
        tvBigCount.setText(String.valueOf(bigCount));
    }

    /**
     * 带变化监听的数量设置方法
     * @param tempSmallCount
     * @param tempBigCount
     */
    public void setCount(int tempSmallCount, int tempBigCount) {
        setDisplayCount(tempSmallCount, tempBigCount);
        if (null != onCountChangeListener) {
            onCountChangeListener.onCountChange(null, bigCount, smallCount);
        }

    }


    /**
     * 设置大单位
     *
     * @param bigUnit
     */
    public void setUnit(String smallUnit, String bigUnit) {
        this.bigUnit = bigUnit;
        this.smallUnit = smallUnit;
        this.tvSmallUnit.setText(smallUnit);
        this.tvBigUnit.setText(bigUnit);
    }


    /**
     * 设置大单位
     *
     * @param bigUnit
     */
    public void setBigUnit(String bigUnit) {
        this.bigUnit = bigUnit;
        this.tvBigUnit.setText(bigUnit);
    }

    /**
     * 设置小单位
     *
     * @param smallUnit
     */
    public void setSmallUnit(String smallUnit) {
        this.smallUnit = smallUnit;
        this.tvSmallUnit.setText(smallUnit);
    }

    /**
     * 1 小单位   2 大单位
     *
     * @param type
     */
    public void setSelectionFocus(int type) {
        if (type == 1) {//小单位
            //光标移动到最后
            String str = String.valueOf(smallCount);
            tvSmallCount.requestFocus();
            tvSmallCount.setSelection(str.length());
        } else {//大单位
            //光标移动到最后
            String str = String.valueOf(bigCount);
            tvBigCount.requestFocus();
            tvBigCount.setSelection(str.length());
        }

    }

    /**
     * 获取小单位数量
     *
     * @return
     */
    public int getSmallCount() {
        return smallCount;
    }

    /**
     * 获取大单位数量
     *
     * @return
     */
    public int getBigCount() {
        return bigCount;
    }

    /**
     * 获取EditText控件
     *
     * @param type
     * @return
     */
    public EditText getEditText(int type) {
        if (1 == type) {
            return tvSmallCount;
        } else {
            return tvBigCount;
        }
    }

    /**
     * 直接改变数量的监听
     *
     * @param onChangedListener
     */
    public void setOnCountChangedLisener(OnCountChangedListener onChangedListener) {
        this.onCountChangeListener = onChangedListener;
    }

    /**
     * 输入数字为0的监听
     *
     * @param onZeroListener
     */
    public void setZeroListener(OnMinListener onZeroListener) {
        this.onMinListener = onZeroListener;
    }

    /**
     * 先执行网络请求，后改变数量的监听
     *
     * @param onGoChangeListener
     */
    public void setGoChangeListener(OnGoChangeListener onGoChangeListener) {
        this.onGoChangeListener = onGoChangeListener;
    }

    /**
     * exittext的点击监听，可用于点击弹出弹窗修改数量
     *
     * @param onEditListener
     */
    public void setOnEditListener(OnEditListener onEditListener) {
        this.onEditListener = onEditListener;
    }

    public void setRatio(int ratio) {
        this.ratio = ratio;
        if (this.ratio > 0) {
            llBigUnitCount.setVisibility(VISIBLE);
        } else {
            llBigUnitCount.setVisibility(GONE);
        }
    }

    /**
     * 增加数量成功
     *
     * @author LiuJiang
     * created at 2017/5/19 20:25
     */
    @Override
    public void onAddSuccess() {
//        count++;
//        tvAmount.setText(count + "");
//        //加减按钮恢复使用
//        isEnable = true;
////        btnDecrease.setEnabled(true);
////        btnIncrease.setEnabled(true);
    }

    /**
     * 减少数量成功
     *
     * @author LiuJiang
     * created at 2017/5/19 20:26
     */
    @Override
    public void onDelSuccess() {
//        count--;
//        tvAmount.setText(count + "");
//        //加减按钮恢复使用
////        btnDecrease.setEnabled(true);
////        btnIncrease.setEnabled(true);
//        isEnable = true;
    }

    /**
     * 修改数量成功
     *
     * @author LiuJiang
     * created at 2017/5/19 20:26
     */
    @Override
    public void onEditSuccess(int count) {
//        if (count > maxCount)
//            this.count = maxCount;
//        else if (count < minCount)
//            this.count = minCount;
//        else
//            this.count = count;
//        tvAmount.setText(this.count + "");
//        //加减按钮恢复使用
////        btnDecrease.setEnabled(true);
////        btnIncrease.setEnabled(true);
//        isEnable = true;
    }

    //取消加减按钮的禁用
    @Override
    public void unForbidden() {
        //加减按钮恢复使用
//        btnDecrease.setEnabled(true);
//        btnIncrease.setEnabled(true);
        isEnable = true;
    }


    public interface OnMinListener {
        void onMin();
    }

    /**
     * 显示软键盘
     *
     * @param countDialog
     */
    private void showKeyboardForDialog(final EditCountDialog countDialog) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                countDialog.showSoftKeyBoard();
            }
        }, 300);//3秒后执行Runnable中的run方法
    }

}

