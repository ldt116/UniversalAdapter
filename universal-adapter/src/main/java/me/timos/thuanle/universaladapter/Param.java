package me.timos.thuanle.universaladapter;

import android.support.annotation.LayoutRes;
import android.view.View;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.timos.thuanle.universaladapter.binder.ViewBinder;

class Param<D> {
    public List<D> data;
    @LayoutRes
    int layoutResId;

    ArrayList<ViewBinder<D, ?, ?>> binders;


    Comparator<D> comparator;

    @LayoutRes
    int groupHeaderLayoutResId;

    Categorizer<D> groupCategorizer;
    ArrayList<ViewBinder<String, ?, ?>> groupHeaderBinders;
    Comparator<String> groupHeaderComparator;
    Comparator<D> groupMemberComparator;
    private Set<Integer> mIds;

    Param(List<D> data) {
        this.data = data;
        binders = new ArrayList<>();
        groupHeaderBinders = new ArrayList<>();

        layoutResId = View.NO_ID;
        groupHeaderLayoutResId = View.NO_ID;
    }

    public void addBinder(ViewBinder<D, ?, ?> binder) {
        binders.add(binder);
    }

    Param<D> cloneBinders() {
        Param<D> param = new Param<>(data);
        param.layoutResId = layoutResId;
        param.binders = new ArrayList<>(binders);
        param.comparator = comparator;

        param.groupHeaderLayoutResId = groupHeaderLayoutResId;
        param.groupCategorizer = groupCategorizer;
        param.groupHeaderBinders = new ArrayList<>(groupHeaderBinders);
        param.groupHeaderComparator = groupHeaderComparator;
        param.groupMemberComparator = groupMemberComparator;

        return param;
    }

    Set<Integer> getIds() {
        if (mIds == null) {
            mIds = new HashSet<>();
            for (ViewBinder<D, ?, ?> binder : binders) {
                mIds.add(binder.getId());
            }
        }
        return mIds;
    }

    boolean hasHeader() {
        return groupCategorizer != null;
    }

}

