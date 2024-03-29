package com.aaron.mymvvm.normalExample.dialog;

import android.view.View;
import android.widget.TextView;
import com.aaron.utilslibrary.BaseDialog;
import com.aaron.utilslibrary.BaseDialogFragment;
import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentActivity;

import com.aaron.mymvvm.R;

/**
 *    desc   : 消息对话框
 */
public final class MessageDialog {

    public static final class Builder
            extends BaseDialogFragment.Builder<Builder>
            implements View.OnClickListener {

        private OnListener mListener;
        private boolean mAutoDismiss = true;

        private final TextView mTitleView;
        private final TextView mMessageView;

        private final TextView mCancelView;
        private final View mLineView;
        private final TextView mConfirmView;

        public Builder(FragmentActivity activity) {
            super(activity);
            // 设置Dialog的视图
            setContentView(R.layout.dialog_message);
            // 设置动画
            setAnimStyle(BaseDialog.AnimStyle.IOS);

            // 获取视图的控件初始化
            mTitleView = findViewById(R.id.tv_message_title);
            mMessageView = findViewById(R.id.tv_message_message);
            mCancelView  = findViewById(R.id.tv_message_cancel);
            mLineView = findViewById(R.id.v_message_line);
            mConfirmView  = findViewById(R.id.tv_message_confirm);
            mCancelView.setOnClickListener(this);
            mConfirmView.setOnClickListener(this);
        }

        public Builder setTitle(@StringRes int id) {
            return setTitle(getString(id));
        }
        public Builder setTitle(CharSequence text) {
            mTitleView.setText(text);
            return this;
        }

        public Builder setMessage(@StringRes int id) {
            return setMessage(getString(id));
        }
        public Builder setMessage(CharSequence text) {
            mMessageView.setText(text);
            return this;
        }

        public Builder setCancel(@StringRes int id) {
            return setCancel(getString(id));
        }
        public Builder setCancel(CharSequence text) {
            mCancelView.setText(text);
            mLineView.setVisibility((text == null || "".equals(text.toString())) ? View.GONE : View.VISIBLE);
            return this;
        }

        public Builder setConfirm(@StringRes int id) {
            return setConfirm(getString(id));
        }
        public Builder setConfirm(CharSequence text) {
            mConfirmView.setText(text);
            return this;
        }

        public Builder setAutoDismiss(boolean dismiss) {
            mAutoDismiss = dismiss;
            return this;
        }

        public Builder setListener(OnListener listener) {
            mListener = listener;
            return this;
        }

        @Override
        public BaseDialog create() {
            // 如果内容为空就抛出异常
            if ("".equals(mMessageView.getText().toString())) {
                throw new IllegalArgumentException("Dialog message not null");
            }
            return super.create();
        }

        /**
         * {@link View.OnClickListener}
         */
        @Override
        public void onClick(View v) {
            if (mAutoDismiss) {
                dismiss();
            }

            if (mListener != null) {
                if (v == mConfirmView) {
                    mListener.onConfirm(getDialog());
                } else if (v == mCancelView) {
                    mListener.onCancel(getDialog());
                }
            }
        }
    }

    public interface OnListener {

        /**
         * 点击确定时回调
         */
        void onConfirm(BaseDialog dialog);

        /**
         * 点击取消时回调
         */
        void onCancel(BaseDialog dialog);
    }
}