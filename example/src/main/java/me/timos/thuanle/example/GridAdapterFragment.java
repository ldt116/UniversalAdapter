package me.timos.thuanle.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.timos.thuanle.universaladapter.DataCallback;
import me.timos.thuanle.universaladapter.OnBindAsyncAction;
import me.timos.thuanle.universaladapter.UniversalAdapter;
import me.timos.thuanle.universaladapter.binder.TextViewBinder;

public class GridAdapterFragment extends android.support.v4.app.Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.fragment_recycler, container, false);
        UniversalAdapter<String> adapter = UniversalAdapter.Builder.with("A","B","C","D","E","F","G","H","I","J")
                .itemLayout(R.layout.item_simple)
                .bind(TextViewBinder.Builder.with(R.id.tvTitle, String.class)
                        .text(new OnBindAsyncAction<String, String>() {
                            @Override
                            public void map(int position, String data, DataCallback<String> callback) {
                                callback.onResult(data);
                            }
                        }))
                .build();
        rv.setLayoutManager(adapter.createLayoutManager(getContext(), 2));
        rv.setAdapter(adapter);
        return rv;
    }
}
