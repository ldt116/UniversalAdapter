package me.timos.thuanle.universaladapter.binder;

import android.support.annotation.IdRes;
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
    }

    public static class Builder<D> extends ViewBinder.Builder<D, TextView> {
        Param<D> mTextParam;

        private Builder(@IdRes int resId) {
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
