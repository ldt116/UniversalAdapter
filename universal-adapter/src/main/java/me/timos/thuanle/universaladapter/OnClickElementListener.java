package me.timos.thuanle.universaladapter;

import android.view.View;

public interface OnClickElementListener<D> {
    void onClick(View view, int position, D data);
}
