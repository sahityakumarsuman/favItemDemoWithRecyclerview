package an.favlistapp.network;

import com.android.volley.NetworkResponse;


public interface StatusCodeCallback {
    void onResponse(NetworkResponse response);
}
