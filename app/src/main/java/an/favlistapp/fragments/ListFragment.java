package an.favlistapp.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.NetworkResponse;

import java.util.ArrayList;
import java.util.List;

import an.favlistapp.R;
import an.favlistapp.adapter.ListAdapter;
import an.favlistapp.network.FailureCallback;
import an.favlistapp.network.StatusCodeCallback;
import an.favlistapp.network.SuccessCallback;
import an.favlistapp.util.DividerItemDecoration;
import an.favlistapp.util.UserDataUtils;
import an.favlistapp.util.Utils;


public class ListFragment extends Fragment {

    private RecyclerView _mainRecyclerViewData;
    private SwipeRefreshLayout _swipeRefreshLayout;
    private ListAdapter _recyclerViewAdapter;
    private List<UserDataUtils> _listData = new ArrayList<>();
    private NetworkResponse networkResponse;
    private LinearLayout _nofavDatacontain;
    private ImageView noDataImageView;

    @Override
    public void onResume() {
        super.onResume();


    }

    public ListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.recyclerview_layout, container, false);

        _mainRecyclerViewData = (RecyclerView) view.findViewById(R.id.mainRecyclerView);
        _swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        _nofavDatacontain = (LinearLayout) view.findViewById(R.id.nofavListDataLinearLayout);
        noDataImageView = (ImageView) view.findViewById(R.id.nothingImageView);
        _swipeRefreshLayout.setRefreshing(true);

        noDataImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getListDataFromNetwork();
            }
        });

        _swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListDataFromNetwork();
            }
        });

        getListDataFromNetwork();


        return view;
    }

    private void getListDataFromNetwork() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        Utils.showingLoadingView(getContext(), progressDialog, "Please wait...");

        Utils utils = new Utils(getContext());
        utils.networkCallForListData(new SuccessCallback() {
            @Override
            public void onResponse(String response) {

                NetworkResponse networkResponse = getNetworkResponse();
                if (networkResponse != null && networkResponse.statusCode == 200) {
                    _nofavDatacontain.setVisibility(View.GONE);
                    _swipeRefreshLayout.setVisibility(View.VISIBLE);
                    List<UserDataUtils> listUtilses = new ArrayList<UserDataUtils>();
                    listUtilses = Utils.parseResponseForList(response);
                    _recyclerViewAdapter = new ListAdapter(getContext(), listUtilses, false);
                    LinearLayoutManager llm = new LinearLayoutManager(getContext());
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    _mainRecyclerViewData.setLayoutManager(llm);
                    _mainRecyclerViewData.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
                    _mainRecyclerViewData.setItemAnimator(new DefaultItemAnimator());

                    _mainRecyclerViewData.setAdapter(_recyclerViewAdapter);
                    Utils.dismissLoadingView(getContext(), progressDialog);
                    _swipeRefreshLayout.setRefreshing(false);


                } else {
                    Utils.showToastMessage("Something went wrong !!", getContext());
                    Utils.dismissLoadingView(getContext(), progressDialog);
                    _swipeRefreshLayout.setRefreshing(false);
                    _nofavDatacontain.setVisibility(View.VISIBLE);
                    _swipeRefreshLayout.setVisibility(View.GONE);

                }

            }
        }, new FailureCallback() {
            @Override
            public void onResponse(String s) {

                Utils.dismissLoadingView(getContext(), progressDialog);
                Utils.showErrorDialogBox(getContext(), "Error", "Server response error !", false);
                _swipeRefreshLayout.setRefreshing(false);

            }
        }, new StatusCodeCallback() {
            @Override
            public void onResponse(NetworkResponse response) {
                setNetworkResponse(response);

            }
        });


    }

    public void setNetworkResponse(NetworkResponse networkResponse) {
        this.networkResponse = networkResponse;
    }

    public NetworkResponse getNetworkResponse() {
        return networkResponse;
    }

}
