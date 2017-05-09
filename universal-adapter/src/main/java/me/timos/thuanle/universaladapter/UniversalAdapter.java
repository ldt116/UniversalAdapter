package me.timos.thuanle.universaladapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.collect.ArrayListMultimap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import me.timos.thuanle.universaladapter.binder.ImageViewBinder;
import me.timos.thuanle.universaladapter.binder.TextViewBinder;
import me.timos.thuanle.universaladapter.binder.ViewBinder;

/**
 * <p>The {@link UniversalAdapter} is suitable for most adapter usage.</p>
 */
public class UniversalAdapter<D> extends RecyclerView.Adapter<UniversalAdapter.VH> {
    private static final int VIEW_TYPE_ELEMENT = 0;
    private static final int VIEW_TYPE_HEADER = 1;

    private final Param<D> mParam;
    private List<D> mDataWrapper;
    private SparseArray<String> mHeaderMap;

    UniversalAdapter(Param<D> param) {
        mParam = param;
        prepareData(param.data);
    }

    /**
     * Noted: if you use group or sorting feature, do not use this method
     *
     * @param data
     */
    public void add(D data) {
        mDataWrapper.add(data);
        notifyItemInserted(mDataWrapper.size() - 1);
    }

    public RecyclerView.LayoutManager createLayoutManager(Context context, final int column, int orientation, boolean reverseLayout) {
        if (column == 1) {
            return new LinearLayoutManager(context, orientation, reverseLayout);
        }
        GridLayoutManager glm = new GridLayoutManager(context, column, orientation, reverseLayout);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                D datum = mDataWrapper.get(position);
                if (datum == null) {
                    return column;
                }
                return 1;
            }
        });
        return glm;
    }

    public RecyclerView.LayoutManager createLayoutManager(Context context, int column) {
        return createLayoutManager(context, column, LinearLayoutManager.VERTICAL, false);
    }

    @Override
    public int getItemCount() {
        return mDataWrapper.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderPosition(position)) {
            return VIEW_TYPE_HEADER;
        }
        return VIEW_TYPE_ELEMENT;
    }

    private boolean isHeaderPosition(int position) {
        return mParam.hasHeader() && mHeaderMap.indexOfKey(position) >= 0;
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        if (!isHeaderPosition(position)) {
            D value = mDataWrapper.get(position);
            for (final ViewBinder<D, ?, ?> binder : mParam.binders) {
                int id = binder.getId();
                View v = holder.mViews.get(id);
                binder.bind(position, value, v);
            }
        } else {
            String header = mHeaderMap.get(position);
            for (final ViewBinder<String, ?, ?> binder : mParam.groupHeaderBinders) {
                int id = binder.getId();
                View v = holder.mViews.get(id);
                binder.bind(position, header, v);
            }
        }
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ELEMENT) {
            View v = LayoutInflater.from(parent.getContext()).inflate(mParam.layoutResId, parent, false);
            return new VH(v, mParam.getIds());
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(mParam.groupHeaderLayoutResId, parent, false);
            return new VH(v, mParam.getIds());
        }
    }

    private void prepareData(List<D> data) {
        if (mParam.hasHeader()) {
            ArrayListMultimap<String, D> mmap = ArrayListMultimap.create();
            ArrayList<String> headers = new ArrayList<>();
            for (D d : data) {
                String cate = mParam.groupCategorizer.getCategory(d);
                if (!mmap.containsKey(cate)) {
                    headers.add(cate);
                }
                mmap.put(cate, d);
            }

            if (mParam.groupHeaderComparator != null) {
                Collections.sort(headers, mParam.groupHeaderComparator);
            }

            mDataWrapper = new ArrayList<>(headers.size() + data.size());
            mHeaderMap = new SparseArray<>(headers.size());
            int hIdx = 0;
            for (String header : headers) {
                mHeaderMap.put(hIdx, header);

                //reverse place for header
                mDataWrapper.add(null);

                //add other group members
                List<D> group = mmap.get(header);
                if (mParam.groupMemberComparator != null) {
                    Collections.sort(group, mParam.groupMemberComparator);
                }
                mDataWrapper.addAll(group);

                //next header position
                hIdx += group.size() + 1;
            }

        } else {
            mDataWrapper = data;
            mHeaderMap = new SparseArray<>();
            if (mParam.comparator != null) {
                Collections.sort(mDataWrapper, mParam.comparator);
            }
        }
    }

    public void setData(List<D> data) {
        prepareData(data);
        notifyDataSetChanged();
    }

    static class VH extends RecyclerView.ViewHolder {
        SparseArray<View> mViews;

        VH(View itemView, Set<Integer> ids) {
            super(itemView);

            mViews = new SparseArray<>();
            for (int id : ids) {
                mViews.put(id, itemView.findViewById(id));
            }
        }
    }

    public static class Builder<T> {
        Param<T> mParam;

        Builder(List<T> data) {
            mParam = new Param<>(data);
        }

        private Builder(Builder<T> src) {
            mParam = src.mParam.cloneBinders();
        }

        public static <T> Builder<T> with(Class<T> classOfData) {
            return with(new ArrayList<T>());
        }

        public static <T> Builder<T> with(T... data) {
            return with(Arrays.asList(data));
        }

        public static <T> Builder<T> with(List<T> data) {
            return new Builder<>(data);
        }

        public Builder<T> bind(ViewBinder<T, ?, ?> binder) {
            mParam.binders.add(binder);
            return this;
        }

        public Builder<T> bind(ViewBinder.Builder<T, ?> builder) {
            return bind(builder.build());
        }

        public ImageViewBinder.Builder<T> bindImageView(@IdRes int resId) {
            ImageViewBinder.Builder<T> binder = new ImageViewBinder.Builder<>(resId);
            mParam.binders.add(binder.build());
            return binder;
        }

        public TextViewBinder.Builder<T> bindTextView(@IdRes int resId) {
            TextViewBinder.Builder<T> binder = new TextViewBinder.Builder<>(resId);
            mParam.binders.add(binder.build());
            return binder;
        }

        public ViewBinder.Builder<T, View> bindView(@IdRes int resId) {
            ViewBinder.Builder<T, View> binder = new ViewBinder.Builder<>(resId);
            mParam.binders.add(binder.build());
            return binder;
        }

        public <V extends View> ViewBinder.Builder<T, V> bindView(@IdRes int resId, Class<V> classOfView) {
            ViewBinder.Builder<T, V> binder = new ViewBinder.Builder<>(resId);
            mParam.binders.add(binder.build());
            return binder;
        }

        public UniversalAdapter<T> build() {
            return new UniversalAdapter<>(mParam);
        }

        /**
         * Create a clone builder with clone list of rules. It share the data with the original builder
         *
         * @return the cloned builder
         */
        public Builder<T> cloneBuilders() {
            return new Builder<>(this);
        }

        public Param<T> getParam() {
            return mParam;
        }

        public Builder<T> groupBy(Categorizer<T> categorizer) {
            mParam.groupCategorizer = categorizer;
            return this;
        }

        public Builder<T> groupHeaderBind(ViewBinder<String, ?, ?> binder) {
            mParam.groupHeaderBinders.add(binder);
            return this;
        }

        public Builder<T> groupHeaderBind(ViewBinder.Builder<String, ?> builder) {
            return groupHeaderBind(builder.build());
        }

        public Builder<T> groupHeaderLayout(@LayoutRes int layoutResId) {
            mParam.groupHeaderLayoutResId = layoutResId;
            return this;
        }

        public Builder<T> groupHeaderSortBy(Comparator<String> groupHeaderSortBy) {
            mParam.groupHeaderComparator = groupHeaderSortBy;
            return this;
        }

        public Builder<T> groupdMemberSortBy(Comparator<T> groupMemberSortBy) {
            mParam.groupMemberComparator = groupMemberSortBy;
            return this;
        }

        public Builder<T> itemLayout(@LayoutRes int layoutResId) {
            mParam.layoutResId = layoutResId;
            return this;
        }

        public Builder<T> sortBy(Comparator<T> comparator) {
            mParam.comparator = comparator;
            return this;
        }
    }
}
