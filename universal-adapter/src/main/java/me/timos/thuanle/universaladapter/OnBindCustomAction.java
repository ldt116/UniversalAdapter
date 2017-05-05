package me.timos.thuanle.universaladapter;

public interface OnBindCustomAction<D, V> {
    void apply(int position, D data, V view);
}
