package me.timos.thuanle.universaladapter;

public interface OnBindAsyncAction<S, D> {
    void map(int position, S data, DataCallback<D> callback);
}

