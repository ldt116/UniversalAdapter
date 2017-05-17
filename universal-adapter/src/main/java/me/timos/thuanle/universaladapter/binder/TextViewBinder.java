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
        if (mParam.goneWhenEmpty) {
            tv.setVisibility(TextUtils.isEmpty(data) ? View.GONE : View.VISIBLE);
        } else if (mParam.invisibleWhenEmpty) {
            tv.setVisibility(TextUtils.isEmpty(data) ? View.INVISIBLE : View.VISIBLE);
        }
    }

    static class TVParam<D> extends ViewBinder.Param<D, TextView> {
        OnBindAsyncAction<D, CharSequence> text;
        OnBindAsyncAction<D, Integer> textColor;
        boolean goneWhenEmpty;
        boolean invisibleWhenEmpty;

        TVParam() {
            goneWhenEmpty = false;
            invisibleWhenEmpty = false;
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
         * Set visibility is <code>View.GONE</code> when the text is null or 0-length.
         *
         * @return
         */
        public TVBuilder<D> goneWhenEmpty() {
            if (mTextParam.invisibleWhenEmpty) {
                throw new IllegalArgumentException("Conflict binding options. You can not set both 'goneWhenEmpty' and 'invisibleWhenEmpty' at one object.");
            }
            mTextParam.goneWhenEmpty = true;
            return this;
        }

        /**
         * Set visibility is <code>View.INVISIBLE</code> when the text is null or 0-length.
         *
         * @return
         */
        public TVBuilder<D> invisibleWhenEmpty() {
            if (mTextParam.goneWhenEmpty) {
                throw new IllegalArgumentException("Conflict binding options. You can not set both 'goneWhenEmpty' and 'invisibleWhenEmpty' at one object.");
            }
            mTextParam.invisibleWhenEmpty = true;
            return this;
        }

        public TVBuilder<D> text(OnBindAsyncAction<D, CharSequence> action1) {
            mTextParam.text = action1;
            return this;
        }

        public TVBuilder<D> textColor(OnBindAsyncAction<D, Integer> action1) {
            mTextParam.textColor = action1;
            return this;
        }
    }
}
