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
import me.timos.thuanle.universaladapter.binder.ImageViewBinder;
import me.timos.thuanle.universaladapter.binder.TextViewBinder;

public class GroupAdapterFragment extends android.support.v4.app.Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.fragment_recycler, container, false);
        UniversalAdapter<Hero> adapter = UniversalAdapter.Builder.with(HeroService.getHeroes())
                .itemLayout(R.layout.item_simple)
                .bind(TextViewBinder.Builder.with(R.id.tvTitle, Hero.class)
                        .text(new OnBindAsyncAction<Hero, String>() {
                            @Override
                            public void map(int position, Hero data, DataCallback<String> callback) {
                                callback.onResult(data.title);
                            }
                        }))
                .bind(ImageViewBinder.Builder.with(R.id.ivThumb, Hero.class)
                        .image(new OnBindAsyncAction<Hero, String>() {
                            @Override
                            public void map(int position, Hero data, DataCallback<String> callback) {
                                callback.onResult(data.url);
                            }
                        }))
                .groupBy(new Categorizer<Hero>() {
                    @Override
                    public String getCategory(Hero data) {
                        return data.primary;
                    }
                })
                .groupHeaderLayout(R.layout.item_header)
                .groupHeaderBind(TextViewBinder.Builder.with(R.id.tvTitle, String.class)
                        .text(new OnBindAsyncAction<String, String>() {
                            @Override
                            public void map(int position, String data, DataCallback<String> callback) {
                                callback.onResult(data.toUpperCase());
                            }
                        }))
                .build();
        rv.setLayoutManager(adapter.createLayoutManager(getContext(), 2));
        rv.setAdapter(adapter);
        return rv;
    }
}
