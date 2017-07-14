package an.favlistapp.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class NetworkRequest {


    private SuccessCallback successCallback;
    private FailureCallback failureCallback;
    private StatusCodeCallback statusCodeCallback;
    private Context context;
    private HashMap params;
    private String url;
    private int method;
    private HashMap headers;


    public void setSuccessCallback(SuccessCallback successCallback) {
        this.successCallback = successCallback;
    }

    public void setFailureCallback(FailureCallback failureCallback) {
        this.failureCallback = failureCallback;
    }

    public void setParams(HashMap params) {
        this.params = params;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setMethod(int method) {
        this.method = method;
    }


    public NetworkRequest(Context context) {

        this.context = context;

    }

    public void setHeader(HashMap<String, String> header) {
        this.headers = header;
    }

    public void execute() {

        Log.i("NetWork", "Sending Req to:" + url);

        if (method == -99)
            method = Request.Method.POST;


        Log.i("Network Req", "Method:" + method);

        StringRequest postRequest = new StringRequest(method, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("NetworkRes", "Response for " + url + response);
                        successCallback.onResponse(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.e("Volley Error", error.getMessage());
                        NetworkResponse networkResponseError = error.networkResponse;
                        statusCodeCallback.onResponse(error.networkResponse);
                        failureCallback.onResponse(error.getMessage());
                    }


                }
        )


        {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> paramsTemp = new HashMap<>();
                // the POST parameters:
                if (params == null)
                    return paramsTemp;
                return params;

            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {

                if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
                    VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
                    volleyError = error;
                }

                return volleyError;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                statusCodeCallback.onResponse(response);
                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // something to do here ??
                Map<String, String> headerTemp = new HashMap<>();
                if (headers != null)
                    return headers;
                return headerTemp;
            }

        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(context).add(postRequest);
    }

    public StatusCodeCallback getStatusCodeCallback() {
        return statusCodeCallback;
    }

    public void setStatusCodeCallback(StatusCodeCallback statusCodeCallback) {
        this.statusCodeCallback = statusCodeCallback;
    }
}
