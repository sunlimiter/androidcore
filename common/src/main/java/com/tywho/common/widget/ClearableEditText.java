package com.tywho.common.widget;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.tywho.common.R;

/**
 * 创建时间：2017/6/9/0009
 * 创建人：lty
 * 功能描述：
 */

public class ClearableEditText extends AppCompatEditText implements View.OnTouchListener,
        View.OnFocusChangeListener, TextWatcher {
    /**
     * <enum name="NONE" value="0" />
     * <enum name="PHONE" value="1" />
     * <enum name="BANK_CARD" value="2" />
     */
    int inputType;

    public interface Listener {
        void didClearText();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private Drawable xD;
    private Listener listener;

    public ClearableEditText(Context context) {
        super(context);
        init();
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        this.l = l;
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener f) {
        this.f = f;
    }

    private OnTouchListener l;
    private OnFocusChangeListener f;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (getCompoundDrawables()[2] != null) {
            boolean tappedX = event.getX() > (getWidth() - getPaddingRight() - xD
                    .getIntrinsicWidth());
            if (tappedX) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    setText("");
                    if (listener != null) {
                        listener.didClearText();
                    }
                }
                return true;
            }
        }
        if (l != null) {
            return l.onTouch(v, event);
        }
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
        if (f != null) {
            f.onFocusChange(v, hasFocus);
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (isFocused()) {
            setClearIconVisible(getText().length() > 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (inputType != 0) {
            removeTextChangedListener(this);
            String text = editable.toString().replace(" ", "");
            String afterText = getFormatedText(text);
            if (!afterText.endsWith(" ")) {
                setText(afterText);
            }
            setSelection(afterText.length());
            addTextChangedListener(this);
        }
    }

    private void init() {
        xD = getCompoundDrawables()[2];
        if (xD == null) {
            xD = getResources()
                    .getDrawable(R.mipmap.login_icon_close);
        }
        xD.setBounds(0, 0, xD.getIntrinsicWidth(), xD.getIntrinsicHeight());
        setClearIconVisible(false);
        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    protected void setClearIconVisible(boolean visible) {
        boolean wasVisible = (getCompoundDrawables()[2] != null);
        if (visible != wasVisible) {
            Drawable x = visible ? xD : null;
            setCompoundDrawables(getCompoundDrawables()[0],
                    getCompoundDrawables()[1], x, getCompoundDrawables()[3]);
        }
    }

    /**
     * 获取每四位分隔的银行卡号码
     *
     * @param textWithSpace
     */
    private String getFormatedText(String textWithSpace) {
        textWithSpace = textWithSpace.replace(" ", "");
        if (inputType == InputType.PHONE.getValue()) {
            //手机号，在最前面加一个空格
            textWithSpace = " " + textWithSpace;
        }
        String arrs[] = textWithSpace.split("");
        int len = arrs.length;
        StringBuffer sb = new StringBuffer();
        for (int i = 1; i < len; i++) {
            sb.append(arrs[i]);
            if (i % 4 == 0) {
                sb.append(" ");
            }
        }
        if (len % 4 == 1) {
            sb.append(" ");
        }
        return sb.toString().trim();//trim掉可能是手机号的空格
    }


    public String getTextWithoutSpace() {
        return getText().toString().replaceAll(" ", "");
    }

    public void setInputType(InputType inputType) {
        this.inputType = inputType.getValue();
        setText(getTextWithoutSpace());
    }

    @BindingAdapter("setInputType")
    public static void setInputType(ClearableEditText view, InputType inputType) {
        view.setInputType(inputType);
    }

    public enum InputType {
        NONE(0), PHONE(1), BANK_CARD(2);
        int value;

        InputType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
