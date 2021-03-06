package me.timos.thuanle.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.timos.thuanle.universaladapter.DataCallback;
import me.timos.thuanle.universaladapter.OnBindAsyncAction;
import me.timos.thuanle.universaladapter.UniversalAdapter;

public class SimpleAdapterFragment extends android.support.v4.app.Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.fragment_recycler, container, false);
        UniversalAdapter.Builder<String> builder = UniversalAdapter.Builder.with("A", "B", "C", "D", "E", "F", "G", "H", "I", "J")
                .itemLayout(R.layout.item_simple);

        builder.bindTextView(R.id.tvTitle)
                .text(new OnBindAsyncAction<String, CharSequence>() {
                    @Override
                    public void map(int position, String data, DataCallback<CharSequence> callback) {
                        callback.onResult(data);
                    }
                });

        UniversalAdapter<String> adapter = builder.build();
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);
        return rv;
    }
}
