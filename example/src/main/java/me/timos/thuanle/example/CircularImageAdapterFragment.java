package me.timos.thuanle.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.koushikdutta.ion.Ion;
import com.makeramen.roundedimageview.RoundedImageView;

import me.timos.thuanle.universaladapter.DataCallback;
import me.timos.thuanle.universaladapter.OnBindAsyncAction;
import me.timos.thuanle.universaladapter.OnBindCustomAction;
import me.timos.thuanle.universaladapter.UniversalAdapter;
import me.timos.thuanle.universaladapter.binder.TextViewBinder;
import me.timos.thuanle.universaladapter.binder.ViewBinder;

public class CircularImageAdapterFragment extends android.support.v4.app.Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.fragment_recycler, container, false);
        UniversalAdapter<Hero> adapter = UniversalAdapter.Builder.with(HeroService.getHeroes())
                .itemLayout(R.layout.item_circular)
                .bind(TextViewBinder.Builder.with(R.id.tvTitle, Hero.class)
                        .text(new OnBindAsyncAction<Hero, String>() {
                            @Override
                            public void map(int position, Hero data, DataCallback<String> callback) {
                                callback.onResult(data.title);
                            }
                        }))
                .bind(ViewBinder.Builder.with(R.id.ivThumb, RoundedImageView.class, Hero.class)
                        .customBind(new OnBindCustomAction<Hero, RoundedImageView>() {
                            @Override
                            public void apply(int position, Hero data, RoundedImageView view) {
                                Ion.with(view)
                                        .load(data.url);
                                view.setBorderColor(getResources().getColor(HeroService.getColor(data.primary)));
                            }
                        }))
                .build();
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);
        return rv;
    }
}
