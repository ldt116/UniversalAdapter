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
 * @param <P> IVParam class
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
        if (mParam.backgroundResource != null) {
            mParam.backColor.map(position, data, new DataCallback<Integer>() {
                @Override
                public void onResult(Integer data) {
                    v.setBackgroundResource(data);
                }
            });
        }

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
        } else if (mParam.goneWhen != null || mParam.invisibleWhen != null) {
            v.setVisibility(View.VISIBLE);
            if (mParam.goneWhen != null) {
                mParam.goneWhen.map(position, data, new DataCallback<Boolean>() {
                    @Override
                    public void onResult(Boolean data) {
                        if (data) {
                            v.setVisibility(View.GONE);
                        }
                    }
                });
            }

            if (mParam.invisibleWhen != null) {
                mParam.invisibleWhen.map(position, data, new DataCallback<Boolean>() {
                    @Override
                    public void onResult(Boolean data) {
                        if (data) {
                            v.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
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
        OnBindAsyncAction<D, Boolean> goneWhen;
        OnBindAsyncAction<D, Boolean> invisibleWhen;
        OnBindAsyncAction<D, Integer> backgroundResource;
    }

    public static class VBuilder<D, V extends View> {
        final int mId;
        Param<D, V> mParam;

        public VBuilder(@IdRes int resId) {
            mId = resId;
            mParam = new Param<>();
        }


        public VBuilder<D, V> backgroundResource(OnBindAsyncAction<D, Integer> action1) {
            mParam.backgroundResource = action1;
            return this;
        }


        public VBuilder<D, V> backColor(OnBindAsyncAction<D, Integer> action1) {
            mParam.backColor = action1;
            return this;
        }

        public ViewBinder<D, V, ? extends Param<D, V>> build() {
            return new ViewBinder<>(mId, mParam);
        }

        public VBuilder<D, V> customBind(OnBindCustomAction<D, V> action1) {
            mParam.customBindAction = action1;
            return this;
        }

        /**
         * Hide view if specific condition happened.
         *
         * @param action1
         * @return builder for chain call
         */
        public VBuilder<D, V> goneWhen(OnBindAsyncAction<D, Boolean> action1) {
            if (mParam.visibility != null) {
                throw new IllegalArgumentException("Conflict binding options. You can not set both 'visibility' and 'goneWhen' at one object.");
            }
            mParam.goneWhen = action1;
            return this;
        }

        /**
         * Hide view if specific condition happened.
         *
         * @param action1
         * @return builder for chain call
         */
        public VBuilder<D, V> invisibleWhen(OnBindAsyncAction<D, Boolean> action1) {
            if (mParam.visibility != null) {
                throw new IllegalArgumentException("Conflict binding options. You can not set both 'visibility' and 'invisibleWhen' at one object.");
            }
            mParam.invisibleWhen = action1;
            return this;
        }

        public VBuilder<D, V> onClick(OnClickElementListener<D> listener) {
            mParam.onClickListener = listener;
            return this;
        }

        public VBuilder<D, V> visibility(OnBindAsyncAction<D, Integer> action1) {
            if (mParam.goneWhen != null || mParam.invisibleWhen != null) {
                throw new IllegalArgumentException("Conflict binding options. You can not set both 'visibility' and 'goneWhen' or 'invisibleWhen' at one object.");
            }
            mParam.visibility = action1;
            return this;
        }
    }
}
