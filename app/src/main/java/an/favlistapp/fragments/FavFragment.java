package an.favlistapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

import an.favlistapp.R;
import an.favlistapp.adapter.ListAdapter;
import an.favlistapp.database.DatabaseHandler;
import an.favlistapp.util.DividerItemDecoration;
import an.favlistapp.util.UserDataUtils;
import an.favlistapp.util.Utils;

public class FavFragment extends Fragment {


    private RecyclerView _recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListAdapter _listAdapter;
    private List<UserDataUtils> _favListData;
    private LinearLayout _nofavdataLinearLayout;
    private ImageView _favIcon;
    private DatabaseHandler db;

    public FavFragment() {

    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d("onResume", " onResume method called");
        if (db != null && db.getFavItemCount() > 0) {
            swipeRefreshLayout.setRefreshing(true);
            setFavDataTolist();
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("onAttach", " onAttach method called");
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("onCreate", " onCreate method called");
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_layout, container, false);
        _recyclerView = (RecyclerView) view.findViewById(R.id.mainRecyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        _nofavdataLinearLayout = (LinearLayout) view.findViewById(R.id.nofavListDataLinearLayout);
        _favIcon = (ImageView) view.findViewById(R.id.nothingImageView);
        _favIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFavDataTolist();
            }
        });
        swipeRefreshLayout.setRefreshing(true);
        setFavDataTolist();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setFavDataTolist();
            }
        });


        return view;
    }

    public void setFavDataTolist() {
        db = new DatabaseHandler(getContext());
        if (db != null && db.getFavItemCount() > 0) {
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            _nofavdataLinearLayout.setVisibility(View.GONE);
            _favListData = db.getAllFavItem();
            _listAdapter = new ListAdapter(getContext(), _favListData, true);
            LinearLayoutManager llm = new LinearLayoutManager(getContext());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            _recyclerView.setLayoutManager(llm);
            _recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
            _recyclerView.setItemAnimator(new DefaultItemAnimator());
            _recyclerView.setAdapter(_listAdapter);
            swipeRefreshLayout.setRefreshing(false);

        } else {
            Utils.showToastMessage("You don't have any fav. list", getContext());

            swipeRefreshLayout.setVisibility(View.GONE);
            _nofavdataLinearLayout.setVisibility(View.VISIBLE);

            swipeRefreshLayout.setRefreshing(false);
        }

    }

    public void refreshList() {
        if (_listAdapter != null) {
            _listAdapter.notifyDataSetChanged();
        }
    }

}
