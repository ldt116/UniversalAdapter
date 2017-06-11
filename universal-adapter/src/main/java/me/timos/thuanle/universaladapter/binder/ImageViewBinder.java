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
public class ImageViewBinder<D> extends ViewBinder<D, ImageView, ImageViewBinder.IVParam<D>> {

    private ImageViewBinder(@IdRes int id, IVParam<D> param) {
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
                    onImageBinded(data, iv);
                }
            });
        }

        if (mParam.imageResource != null) {
            iv.setImageResource(mParam.imageResource[position]);
            onImageBinded(String.valueOf(mParam.imageResource[position]), iv);
        }
    }

    private void onImageBinded(String data, ImageView iv) {
        if (mParam.visibilityWhenEmpty != ImageViewBinder.IVParam.VISIBILITY_WHEN_EMPTY_NOT_SET) {
            iv.setVisibility(TextUtils.isEmpty(data) ? mParam.visibilityWhenEmpty : View.VISIBLE);
        }
    }

    static class IVParam<D> extends ViewBinder.Param<D, ImageView> {
        static final int VISIBILITY_WHEN_EMPTY_NOT_SET = -1;

        int[] imageResource;
        OnBindAsyncAction<D, String> src;
        int visibilityWhenEmpty;

        IVParam() {
            visibilityWhenEmpty = VISIBILITY_WHEN_EMPTY_NOT_SET;
        }
    }

    public static class IVBuilder<D> extends VBuilder<D, ImageView> {
        private IVParam<D> mImageParam;

        public IVBuilder(@IdRes int resId) {
            super(resId);
            mParam = mImageParam = new IVParam<>();
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
            return image(action1, IVParam.VISIBILITY_WHEN_EMPTY_NOT_SET);
        }

        public IVBuilder<D> image(OnBindAsyncAction<D, String> action1, int visibilityWhenEmpty) {
            mImageParam.src = action1;
            mImageParam.visibilityWhenEmpty = visibilityWhenEmpty;
            return this;
        }
    }
}
