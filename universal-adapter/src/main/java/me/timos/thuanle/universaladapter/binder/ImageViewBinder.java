package me.timos.thuanle.universaladapter.binder;

import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import me.timos.thuanle.universaladapter.DataCallback;
import me.timos.thuanle.universaladapter.OnBindAsyncAction;

/**
 * @param <D> data type
 */
public class ImageViewBinder<D> extends ViewBinder<D, ImageView, ImageViewBinder.Param<D>> {

    private ImageViewBinder(@IdRes int id, Param<D> param) {
        super(id, param);
    }

    @Override
    public void bind(int position, D data, View v) {
        super.bind(position, data, v);

        final ImageView iv = (ImageView) v;
        if (mParam.src != null) {
            mParam.src.map(position, data, new DataCallback<String>() {
                @Override
                public void onResult(String data) {
                    if (TextUtils.isEmpty(data)) {
                    } else if (TextUtils.isDigitsOnly(data)) {
                        int resId = Integer.valueOf(data);
                        iv.setImageResource(resId);
                    } else {
                        Picasso.with(iv.getContext()).load(data).into(iv);
                    }

                }
            });
        }

        if (mParam.imageResource != null) {
            iv.setImageResource(mParam.imageResource[position]);
        }
    }

    static class Param<D> extends ViewBinder.Param<D, ImageView> {
        int[] imageResource;
        OnBindAsyncAction<D, String> src;
    }

    public static class IVBuilder<D> extends VBuilder<D, ImageView> {
        private Param<D> mImageParam;

        public IVBuilder(@IdRes int resId) {
            super(resId);
            mParam = mImageParam = new Param<>();
        }

        @Override
        public ViewBinder<D, ImageView, ? extends ViewBinder.Param<D, ImageView>> build() {
            return new ImageViewBinder<>(mId, mImageParam);
        }

        public IVBuilder<D> image(@DrawableRes int... drawableRes) {
            mImageParam.imageResource = drawableRes;
            return this;
        }

        public IVBuilder<D> image(OnBindAsyncAction<D, String> action1) {
            mImageParam.src = action1;
            return this;
        }
    }
}
