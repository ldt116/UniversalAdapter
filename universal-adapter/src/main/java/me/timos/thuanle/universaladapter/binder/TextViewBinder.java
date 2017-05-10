package me.timos.thuanle.universaladapter.binder;

import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import me.timos.thuanle.universaladapter.DataCallback;
import me.timos.thuanle.universaladapter.OnBindAsyncAction;

/**
 * Created by thuanle on 3/3/17.
 */
public class TextViewBinder<T> extends ViewBinder<T, TextView, TextViewBinder.Param<T>> {
    private TextViewBinder(@IdRes int id, Param<T> param) {
        super(id, param);
    }

    @Override
    public void bind(int position, T data, View v) {
        super.bind(position, data, v);

        final TextView tv = (TextView) v;
        if (mParam.text != null) {
            mParam.text.map(position, data, new DataCallback<String>() {
                @Override
                public void onResult(String data) {
                    tv.setText(data);

                    if (mParam.goneWhenEmpty) {
                        tv.setVisibility(TextUtils.isEmpty(data) ? View.GONE : View.VISIBLE);
                    } else if (mParam.invisibleWhenEmpty) {
                        tv.setVisibility(TextUtils.isEmpty(data) ? View.INVISIBLE : View.VISIBLE);
                    }
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

    static class Param<D> extends ViewBinder.Param<D, TextView> {
        OnBindAsyncAction<D, String> text;
        OnBindAsyncAction<D, Integer> textColor;
        boolean goneWhenEmpty;
        boolean invisibleWhenEmpty;

        Param() {
            goneWhenEmpty = false;
            invisibleWhenEmpty = false;
        }
    }

    public static class Builder<D> extends ViewBinder.Builder<D, TextView> {
        Param<D> mTextParam;

        public Builder(@IdRes int resId) {
            super(resId);
            mParam = mTextParam = new Param<>();
        }

        public static <T> Builder<T> with(@IdRes int resId, Class<T> classOfData) {
            return new Builder<>(resId);
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
        public Builder<D> goneWhenEmpty() {
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
        public Builder<D> invisibleWhenEmpty() {
            if (mTextParam.goneWhenEmpty) {
                throw new IllegalArgumentException("Conflict binding options. You can not set both 'goneWhenEmpty' and 'invisibleWhenEmpty' at one object.");
            }
            mTextParam.invisibleWhenEmpty = true;
            return this;
        }

        public Builder<D> text(OnBindAsyncAction<D, String> action1) {
            mTextParam.text = action1;
            return this;
        }

        public Builder<D> textColor(OnBindAsyncAction<D, Integer> action1) {
            mTextParam.textColor = action1;
            return this;
        }

    }
}
