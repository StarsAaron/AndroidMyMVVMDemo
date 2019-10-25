package com.aaron.mymvvm.normalExample.dialog;

import android.view.Gravity;
import androidx.fragment.app.FragmentActivity;
import com.aaron.utilslibrary.BaseDialog;
import com.aaron.utilslibrary.BaseDialogFragment;
import com.aaron.mymvvm.R;

/**
 *    desc   : 可进行拷贝的副本
 */
public final class CopyDialog {

    public static final class Builder
            extends BaseDialogFragment.Builder<Builder> {

        public Builder(FragmentActivity activity) {
            super(activity);

            setContentView(R.layout.dialog_copy);
            setAnimStyle(BaseDialog.AnimStyle.BOTTOM);
            setGravity(Gravity.BOTTOM);
        }
    }
}