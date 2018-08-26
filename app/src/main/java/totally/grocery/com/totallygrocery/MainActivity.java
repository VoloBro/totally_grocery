package totally.grocery.com.totallygrocery;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import totally.grocery.com.totallygrocery.Adapter.CardListAdapter;
import totally.grocery.com.totallygrocery.Helper.Common;
import totally.grocery.com.totallygrocery.Helper.RecyclerItemTouchHelper;
import totally.grocery.com.totallygrocery.Helper.RecyclerItemTouchHelperListener;
import totally.grocery.com.totallygrocery.Model.Item;
import totally.grocery.com.totallygrocery.Remote.IMenuRequest;

public class MainActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    private final String URL_API = "https://api.androidhive.info/json/menu.json";
    private RecyclerView recyclerView;
    private List<Item> list;
    private CardListAdapter adapter;
    private CoordinatorLayout rootLayout;

    IMenuRequest mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mService = Common.getMenuRequest();

        //setup Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Total Cart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        rootLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);
        list = new ArrayList<>();
        adapter = new CardListAdapter(this, list);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        //Request API
        addItemToCart();
    }

    private void addItemToCart() {
        mService.getMennuList(URL_API)
                .enqueue(new Callback<List<Item>>() {
                    @Override
                    public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                        list.clear();
                        list.addAll(response.body());
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<List<Item>> call, Throwable t) {

                    }
                });
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CardListAdapter.MyViewHolder){
            String name = list.get(viewHolder.getAdapterPosition()).getName();
            final Item deletedItem = list.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();
            adapter.removeItem(deletedIndex);

            Snackbar snackbar = Snackbar.make(rootLayout, name + "Removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.show();
        }
    }
}
