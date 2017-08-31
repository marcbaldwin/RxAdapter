package xyz.marcb.rxadapter.app;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.subjects.BehaviorSubject;
import xyz.marcb.rxadapter.Item;
import xyz.marcb.rxadapter.Items;
import xyz.marcb.rxadapter.RxAdapter;
import xyz.marcb.rxadapter.Section;
import xyz.marcb.rxadapter.StaticItem;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.dateslist) RecyclerView listView;

    private final BehaviorSubject<List<Date>> items = BehaviorSubject.create(Collections.<Date>emptyList());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            final List<Date> newItems = new ArrayList<>(this.items.getValue());
            newItems.add(new Date());
            this.items.onNext(newItems);
        });

        final RxAdapter adapter = new RxAdapter();

        //
        // Header Section
        //
        adapter.registerViewHolder(HeaderViewHolder.class, parent ->
                new HeaderViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false)
                ), null
        );

        final Section headerSection = adapter.section(null);

        // Static item
        final StaticItem<HeaderViewHolder> headerRow = headerSection.item(HeaderViewHolder.class, null);
        headerRow.setBinder(viewHolder -> {
            viewHolder.title.setText("Dates to Remember");
            return null;
        });

        // Nullable item
        final Item<String,HeaderViewHolder> optionalHeader = headerSection.item(HeaderViewHolder.class, Observable.just(null), null);
        optionalHeader.setBinder((s, viewHolder) -> {
            viewHolder.title.setText("Nulls are allowed");
            return null;
        });

        //
        // Dates Section
        //
        adapter.registerViewHolder(DateViewHolder.class, parent ->
                new DateViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false)
                ), null
        );

        final Section datesSection = adapter.section(null);

        // Observable items
        final StaticItem<DateViewHolder> now = datesSection.item(DateViewHolder.class, null);
        now.setBinder((viewHolder -> {
            viewHolder.title.setText("TODAY");
            return null;
        }));

        final Items<Date, DateViewHolder> items = datesSection.items(DateViewHolder.class, this.items, null);
        items.setId((date -> "" + date.hashCode()));
        items.setBinder((date, viewHolder) -> {
            viewHolder.title.setText(date.toLocaleString());
            viewHolder.itemView.setOnClickListener((view) -> {
                final List<Date> newItems = new ArrayList<>(this.items.getValue());
                newItems.remove(date);
                this.items.onNext(newItems);
            });
            return null;
        });

        // Bind it
        listView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listView.setAdapter(adapter.create());
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title) TextView title;

        HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class DateViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title) TextView title;

        DateViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
