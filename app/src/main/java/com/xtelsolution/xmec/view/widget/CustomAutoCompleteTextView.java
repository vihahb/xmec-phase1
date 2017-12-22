package com.xtelsolution.xmec.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;

public class CustomAutoCompleteTextView extends android.support.v7.widget.AppCompatAutoCompleteTextView {

        public CustomAutoCompleteTextView(Context context) {
            super(context);
        }

        public CustomAutoCompleteTextView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public CustomAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        public boolean onKeyPreIme(int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && isPopupShowing()) {
                InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                if (inputManager != null && inputManager.hideSoftInputFromWindow(findFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS)) {
                    return true;
                }
            }

            return super.onKeyPreIme(keyCode, event);
        }

    }