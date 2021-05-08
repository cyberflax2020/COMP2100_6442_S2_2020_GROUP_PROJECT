package com.example.procomsearch.buttonEditText;

/**
 * Author:Chaofan Li
 * Reference:https://www.jianshu.com/p/2fdaf9211d01,
 *           https://blog.csdn.net/qq_34207101/article/details/75599523
 */

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.example.procomsearch.R;

public class ClearableEditText extends AppCompatEditText implements View.OnTouchListener,View.OnFocusChangeListener, TextWatcher {
    Drawable mclearTextIcon;
    private OnFocusChangeListener mOnFocusChangeListener;
    private OnTouchListener mOnTouchListener;


    public ClearableEditText(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ClearableEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ClearableEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context){
        final Drawable drawable = ContextCompat.getDrawable(context, R.drawable.clear);
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(wrappedDrawable,getCurrentHintTextColor());
         mclearTextIcon = wrappedDrawable;
         mclearTextIcon.setBounds(0,0,mclearTextIcon.getIntrinsicWidth(),mclearTextIcon.getIntrinsicHeight());
         setClearIconVisible(false);
         super.setOnTouchListener(this);
         super.setOnFocusChangeListener(this);
         addTextChangedListener(this);
    }

    private void setClearIconVisible(final boolean visible){
        mclearTextIcon.setVisible(visible,false);
        final Drawable[] compoundDrawables = getCompoundDrawables();
        setCompoundDrawables(
                compoundDrawables[0],
                compoundDrawables[1],
                visible?mclearTextIcon:null,
                compoundDrawables[3]
        );
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (isFocused()){
            setClearIconVisible(s.length()>0);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
        mOnFocusChangeListener = onFocusChangeListener;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus){
            setClearIconVisible(getText().length()>0);
        }else {
            setClearIconVisible(false);
        }
        if (mOnFocusChangeListener!=null){
            mOnFocusChangeListener.onFocusChange(v,hasFocus);
        }

    }

    @Override
    public void setOnTouchListener(OnTouchListener onTouchListener) {
        mOnTouchListener = onTouchListener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int x = ((int)event.getX());
        if(mclearTextIcon.isVisible()&&x>getWidth()-getPaddingRight()-mclearTextIcon.getIntrinsicWidth()){
            if (event.getAction()==MotionEvent.ACTION_UP) {
                setText("");
                return true;
            }
        }
        return mOnTouchListener!=null&&mOnTouchListener.onTouch(v,event);
    }
}
