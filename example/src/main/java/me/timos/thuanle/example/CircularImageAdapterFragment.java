package me.timos.thuanle.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import me.timos.thuanle.universaladapter.DataCallback;
import me.timos.thuanle.universaladapter.OnBindAsyncAction;
import me.timos.thuanle.universaladapter.OnBindCustomAction;
import me.timos.thuanle.universaladapter.UniversalAdapter;

public class CircularImageAdapterFragment extends android.support.v4.app.Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.fragment_recycler, container, false);
        UniversalAdapter.Builder<Hero> builder = UniversalAdapter.Builder.with(HeroService.getHeroes())
                .itemLayout(R.layout.item_circular);

        builder.bindTextView(R.id.tvTitle)
                .text(new OnBindAsyncAction<Hero, CharSequence>() {
                    @Override
                    public void map(int position, Hero data, DataCallback<CharSequence> callback) {
                        callback.onResult(data.title);
                    }
                });

        builder.bindView(R.id.ivThumb, RoundedImageView.class)
                .customBind(new OnBindCustomAction<Hero, RoundedImageView>() {
                    @Override
                    public void apply(int position, Hero data, RoundedImageView view) {
                        Picasso.with(getContext()).load(data.url).into(view);
                        view.setBorderColor(getResources().getColor(HeroService.getColor(data.primary)));
                    }
                });
        UniversalAdapter<Hero> adapter = builder.build();
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);
        return rv;
    }
}
