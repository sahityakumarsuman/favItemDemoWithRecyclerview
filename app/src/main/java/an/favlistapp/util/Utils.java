package an.favlistapp.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.Snackbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import an.favlistapp.R;
import an.favlistapp.network.FailureCallback;
import an.favlistapp.network.NetworkRequest;
import an.favlistapp.network.StatusCodeCallback;
import an.favlistapp.network.SuccessCallback;

/**
 * Created by sahitya on 13/7/17.
 */

public class Utils {

    Context _mContext;

    private static String[] suffix = new String[]{"", "k", "m", "b", "t"};
    private static int MAX_LENGTH = 4;

    public Utils(Context context) {
        this._mContext = context;
    }

    public static String format(double number) {
        String r = new DecimalFormat("##0E0").format(number);
        r = r.replaceAll("E[0-9]", suffix[Character.getNumericValue(r.charAt(r.length() - 1)) / 3]);
        while (r.length() > MAX_LENGTH || r.matches("[0-9]+\\.[a-z]")) {
            r = r.substring(0, r.length() - 2) + r.substring(r.length() - 1);
        }
        return r;
    }

    public static void showingLoadingView(Context context, ProgressDialog progressDialog, String progressMessage) {

        if (context != null && progressDialog != null) {
            progressDialog.setMessage(progressMessage);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setProgress(0);
            progressDialog.show();
        }


    }


    public static void dismissLoadingView(Context context, ProgressDialog progressDialog) {

        if (progressDialog != null && context != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }

    }

    public void networkCallForListData(SuccessCallback successCallback, FailureCallback failureCallback, StatusCodeCallback statusCodeCallback) {
        NetworkRequest networkRequest = new NetworkRequest(_mContext);
        networkRequest.setSuccessCallback(successCallback);
        networkRequest.setFailureCallback(failureCallback);
        networkRequest.setStatusCodeCallback(statusCodeCallback);
        networkRequest.setUrl(Constant.BASE_URL);
        networkRequest.setMethod(Request.Method.GET);
        networkRequest.execute();
    }

    public static void showToastMessage(String toastMessage, Context context) {

        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();

    }

    public static List<ListUtils> parseResponseForList(String response) {

        List<ListUtils> listDatParse = new ArrayList<>();
        try {
            JSONArray mainJsonArray = new JSONArray(response);
            for (int i = 0; i < mainJsonArray.length(); i++) {

                JSONObject singleJsonData = mainJsonArray.getJSONObject(i);
                ListUtils singleData = new ListUtils();
                singleData.set_title(singleJsonData.has("title") ? singleJsonData.getString("title") : "");
                singleData.set_description(singleJsonData.has("desc") ? singleJsonData.getString("desc") : "");
                singleData.set_imageUrl(singleJsonData.has("imageUrl") ? singleJsonData.getString("imageUrl") : "");
                singleData.set_type(singleJsonData.has("type") ? singleJsonData.getString("type") : "");
                singleData.set_viewCount(singleJsonData.has("view-count") ? singleJsonData.getString("view-count") : "");
                singleData.set_fav(false);
                listDatParse.add(singleData);
            }

            return listDatParse;


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static void showErrorDialogBox(final Context context, String errorTitleMessage, String errorMessage, final boolean exit) {


        LayoutInflater inflater = LayoutInflater.from(context);
        View promtView = inflater.inflate(R.layout.error_message_dialog, null);


        final Dialog signUpAlertDialog = new Dialog(context);


        signUpAlertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        signUpAlertDialog.setContentView(promtView);
        signUpAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        signUpAlertDialog.setCanceledOnTouchOutside(true);


        final Button okeyButton = (Button) promtView.findViewById(R.id.success_button_dialogBox);

        final TextView textViewAlertTitle = (TextView) promtView.findViewById(R.id.alertTitleMessage);

        textViewAlertTitle.setText(errorTitleMessage);

        final TextView textView = (TextView) promtView.findViewById(R.id.alertDialog_success_messageTV);
        textView.setText(errorMessage);

        okeyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signUpAlertDialog.dismiss();
                if (exit) {
                    Utils.stopApplictionAndExit(context);
                }

            }
        });

        final LinearLayout parentLinearLayout = (LinearLayout) promtView.findViewById(R.id.parentLinearLayout);

        parentLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpAlertDialog.dismiss();
                if (exit) {
                    Utils.stopApplictionAndExit(context);
                }

            }
        });


        signUpAlertDialog.setCancelable(true);
        signUpAlertDialog.show();


    }

    public static void stopApplictionAndExit(Context context) {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(homeIntent);
    }

    public static int dpToPx(float dp, Resources resources) {
        float px =
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }

}
