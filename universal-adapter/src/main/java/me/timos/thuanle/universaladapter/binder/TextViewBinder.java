package me.timos.thuanle.universaladapter.binder;

import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import me.timos.thuanle.universaladapter.DataCallback;
import me.timos.thuanle.universaladapter.OnBindAsyncAction;

public class TextViewBinder<T> extends ViewBinder<T, TextView, TextViewBinder.TVParam<T>> {
    private TextViewBinder(@IdRes int id, TVParam<T> param) {
        super(id, param);
    }

    @Override
    public void bind(int position, T data, View v) {
        super.bind(position, data, v);

        final TextView tv = (TextView) v;
        if (mParam.text != null) {
            mParam.text.map(position, data, new DataCallback<CharSequence>() {
                @Override
                public void onResult(CharSequence data) {
                    tv.setText(data);
                    onTextBinded(data, tv);
                }
            });
        }

        if (mParam.textColor != null) {
            mParam.textColor.map(position, data, new DataCallback<Integer>() {
                @Override
                public void onResult(Integer data) {
                    tv.setTextColor(data);
                }
            });
        }
    }

    private void onTextBinded(CharSequence data, TextView tv) {
        if (mParam.visibilityWhenEmpty != TVParam.VISIBILITY_WHEN_EMPTY_NOT_SET) {
            tv.setVisibility(TextUtils.isEmpty(data) ? mParam.visibilityWhenEmpty : View.VISIBLE);
        }
    }

    static class TVParam<D> extends ViewBinder.Param<D, TextView> {
        static final int VISIBILITY_WHEN_EMPTY_NOT_SET = -1;
        OnBindAsyncAction<D, CharSequence> text;
        OnBindAsyncAction<D, Integer> textColor;
        int visibilityWhenEmpty;

        TVParam() {
            visibilityWhenEmpty = VISIBILITY_WHEN_EMPTY_NOT_SET;
        }
    }

    public static class TVBuilder<D> extends VBuilder<D, TextView> {
        TVParam<D> mTextParam;

        public TVBuilder(@IdRes int resId) {
            super(resId);
            mParam = mTextParam = new TVParam<>();
        }

        @Override
        public ViewBinder<D, TextView, ? extends ViewBinder.Param<D, TextView>> build() {
            return new TextViewBinder<>(mId, mTextParam);
        }

        /**
         * Set text
         *
         * @param action1
         * @return
         */
        public TVBuilder<D> text(OnBindAsyncAction<D, CharSequence> action1) {
            return text(action1, TVParam.VISIBILITY_WHEN_EMPTY_NOT_SET);
        }

        /**
         * Set text and visibility when text are empty
         *
         * @param action1             the text callback
         * @param visibilityWhenEmpty the visibility when text are empty
         * @return
         */
        public TVBuilder<D> text(OnBindAsyncAction<D, CharSequence> action1, int visibilityWhenEmpty) {
            mTextParam.text = action1;
            mTextParam.visibilityWhenEmpty = visibilityWhenEmpty;
            return this;
        }

        public TVBuilder<D> textColor(OnBindAsyncAction<D, Integer> action1) {
            mTextParam.textColor = action1;
            return this;
        }
    }
}
