package me.timos.thuanle.universaladapter.binder;

import android.support.annotation.IdRes;
import android.view.View;

import me.timos.thuanle.universaladapter.DataCallback;
import me.timos.thuanle.universaladapter.OnBindAsyncAction;
import me.timos.thuanle.universaladapter.OnBindCustomAction;
import me.timos.thuanle.universaladapter.OnClickElementListener;

/**
 * @param <V> View class
 * @param <D> Data class
 * @param <P> Param class
 */
public class ViewBinder<D, V extends View, P extends ViewBinder.Param<D, V>> {
    @IdRes
    private final int id;
    P mParam;

    public ViewBinder(int id, P param) {
        this.id = id;
        mParam = param;
    }

    public void bind(final int position, final D data, final View v) {
        if (mParam.backColor != null) {
            mParam.backColor.map(position, data, new DataCallback<Integer>() {
                @Override
                public void onResult(Integer data) {
                    v.setBackgroundColor(data);
                }
            });
        }

        if (mParam.onClickListener != null) v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mParam.onClickListener.onClick(v, position, data);
            }
        });

        if (mParam.visibility != null) {
            mParam.visibility.map(position, data, new DataCallback<Integer>() {
                @Override
                public void onResult(Integer data) {
                    v.setVisibility(data);
                }
            });
        }

        if (mParam.customBindAction != null) {
            mParam.customBindAction.apply(position, data, (V) v);
        }
    }

    @IdRes
    public int getId() {
        return id;
    }

    static class Param<D, V> {
        OnBindAsyncAction<D, Integer> backColor;
        OnClickElementListener<D> onClickListener;
        OnBindCustomAction<D, V> customBindAction;
        OnBindAsyncAction<D, Integer> visibility;
    }

    public static class Builder<D, V extends View> {
        final int mId;
        Param<D, V> mParam;

        Builder(@IdRes int resId) {
            mId = resId;
            mParam = new Param<>();
        }

        public static <V extends View, T> Builder<T, V> with(@IdRes int resId, Class<V> classOfView, Class<T> classOfData) {
            return new Builder<>(resId);
        }

        public Builder<D, V> backColor(OnBindAsyncAction<D, Integer> action1) {
            mParam.backColor = action1;
            return this;
        }

        public ViewBinder<D, V, ? extends Param<D, V>> build() {
            return new ViewBinder<>(mId, mParam);
        }

        public Builder<D, V> customBind(OnBindCustomAction<D, V> action1) {
            mParam.customBindAction = action1;
            return this;
        }

        public Builder<D, V> onClick(OnClickElementListener<D> listener) {
            mParam.onClickListener = listener;
            return this;
        }

        public Builder<D, V> visibility(OnBindAsyncAction<D, Integer> action1) {
            mParam.visibility = action1;
            return this;
        }
    }
}
