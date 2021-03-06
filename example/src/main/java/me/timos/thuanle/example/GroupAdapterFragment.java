package me.timos.thuanle.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.timos.thuanle.universaladapter.Categorizer;
import me.timos.thuanle.universaladapter.DataCallback;
import me.timos.thuanle.universaladapter.OnBindAsyncAction;
import me.timos.thuanle.universaladapter.UniversalAdapter;

public class GroupAdapterFragment extends android.support.v4.app.Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.fragment_recycler, container, false);
        UniversalAdapter.Builder<Hero> builder = UniversalAdapter.Builder.with(HeroService.getHeroes())
                .itemLayout(R.layout.item_simple);

        builder.bindTextView(R.id.tvTitle)
                .text(new OnBindAsyncAction<Hero, CharSequence>() {
                    @Override
                    public void map(int position, Hero data, DataCallback<CharSequence> callback) {
                        callback.onResult(data.title);
                    }
                });

        builder.bindImageView(R.id.ivThumb)
                .image(new OnBindAsyncAction<Hero, String>() {
                    @Override
                    public void map(int position, Hero data, DataCallback<String> callback) {
                        callback.onResult(data.url);
                    }
                });
        builder.groupBy(new Categorizer<Hero>() {
            @Override
            public String getCategory(Hero data) {
                return data.primary;
            }
        })
                .groupHeaderLayout(R.layout.item_header);

        builder.groupHeaderBindTextView(R.id.tvTitle)
                .text(new OnBindAsyncAction<String, CharSequence>() {
                    @Override
                    public void map(int position, String data, DataCallback<CharSequence> callback) {
                        callback.onResult(data.toUpperCase());
                    }
                });
        UniversalAdapter<Hero> adapter = builder.build();
        rv.setLayoutManager(adapter.createLayoutManager(getContext(), 2));
        rv.setAdapter(adapter);
        return rv;
    }
}
