package com.aaron.mymvvm.normalExample.dialog;

import android.view.View;
import android.widget.TextView;
import com.aaron.utilslibrary.BaseDialog;
import com.aaron.utilslibrary.BaseDialogFragment;
import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentActivity;

import com.aaron.mymvvm.R;

/**
 *    desc   : 等待加载对话框
 */
public final class WaitDialog {

    public static final class Builder
            extends BaseDialogFragment.Builder<Builder> {

        private final TextView mMessageView;

        public Builder(FragmentActivity activity) {
            super(activity);
            setContentView(R.layout.dialog_wait);
            setAnimStyle(BaseDialog.AnimStyle.TOAST);
            setBackgroundDimEnabled(false);
            setCancelable(false);

            mMessageView = findViewById(R.id.tv_wait_message);
        }

        public Builder setMessage(@StringRes int id) {
            return setMessage(getString(id));
        }
        public Builder setMessage(CharSequence text) {
            mMessageView.setText(text);
            mMessageView.setVisibility(text == null ? View.GONE : View.VISIBLE);
            return this;
        }
    }
}