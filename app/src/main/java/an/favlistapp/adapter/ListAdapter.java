package an.favlistapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import an.favlistapp.R;
import an.favlistapp.database.DatabaseHandler;
import an.favlistapp.util.CircleTransform;
import an.favlistapp.util.UserDataUtils;
import an.favlistapp.util.SharedPrefsUtils;
import an.favlistapp.util.Utils;
import an.favlistapp.util.custom_ui.CustomFavoriteButton;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    private Context _mContext;
    private List<UserDataUtils> _listData;
    private List<UserDataUtils> _favData;
    private SharedPrefsUtils sharedPrefsUtils;
    private boolean _isFav;
    private DatabaseHandler db;

    public ListAdapter(Context context, List<UserDataUtils> listData, boolean fromFav) {
        this._mContext = context;
        this._listData = listData;
        sharedPrefsUtils = new SharedPrefsUtils(_mContext);
        this._isFav = fromFav;
        this.db = new DatabaseHandler(_mContext);

    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_item_layout, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, int position) {


        final UserDataUtils listUtilsData = _listData.get(position);
        holder._titleTV.setText(listUtilsData.get_title());
        holder._descriptionTV.setText(listUtilsData.get_description());
        holder._counterView_TV.setText(Utils.format(Double.parseDouble(listUtilsData.get_viewCount())));
        holder._offerTV.setText("Type : " + listUtilsData.get_type());
        Glide.with(_mContext).load(listUtilsData.get_imageUrl())
                .thumbnail(0.5f)
                .crossFade()
                .transform(new CircleTransform(_mContext))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder._listImageView);

        if (db != null) {
            boolean isFav = db.ifExists(listUtilsData);
            if (isFav) {
                holder._favoriteButton.setFavorite(true);
            } else {
                holder._favoriteButton.setFavorite(false);
            }
        }

        if (_isFav) {
            holder._favoriteButton.setVisibility(View.GONE);
        } else {
            holder._favoriteButton.setVisibility(View.VISIBLE);
        }
        holder._favoriteButton.setType(CustomFavoriteButton.STYLE_HEART);

        holder._favoriteButton.setOnFavoriteChangeListener(new CustomFavoriteButton.OnFavoriteChangeListener() {
            @Override
            public void onFavoriteChanged(CustomFavoriteButton buttonView, boolean favorite) {

                if (favorite) {
                    Utils.showToastMessage("Liked", _mContext);

                    String title = listUtilsData.get_title();
                    String description = listUtilsData.get_description();
                    String counterView = listUtilsData.get_viewCount();
                    String offerTv = listUtilsData.get_type();
                    String imageUrm = listUtilsData.get_imageUrl();
                    boolean liked = true;

                    Log.d("Liked Data", "Title " + title + ", Des " + description + ", CounterView" + counterView + ", offer " + offerTv + ", ImageUrl " + imageUrm);

                    UserDataUtils newData = new UserDataUtils();
                    newData.set_viewCount(counterView);
                    newData.set_title(title);
                    newData.set_type(offerTv);
                    newData.set_imageUrl(imageUrm);
                    newData.set_description(description);
                    newData.set_fav(true);

                    if (db != null) {
                        boolean existOrNot = db.ifExists(newData);
                        if (!existOrNot) {
                            db.addFav(newData);
                            Log.d("Left Total ", " ::: " + db.getFavItemCount());
                        }
                    }


                } else {
                    Utils.showToastMessage("Unliked", _mContext);

                    String title = listUtilsData.get_title();
                    String description = listUtilsData.get_description();
                    String counterView = listUtilsData.get_viewCount();
                    String offerTv = listUtilsData.get_type();
                    String imageUrm = listUtilsData.get_imageUrl();
                    boolean liked = false;
                    UserDataUtils listutil = new UserDataUtils();
                    listutil.set_title(title);
                    listutil.set_fav(liked);
                    listutil.set_description(description);
                    listutil.set_type(offerTv);
                    listutil.set_imageUrl(imageUrm);
                    listutil.set_viewCount(counterView);
                    if (db != null) {
                        boolean exist = db.ifExists(listutil);
                        if (exist) {
                            db.deleteFavItem(listutil);
                            Log.d("Left Total ", " ::: " + db.getFavItemCount());
                        }
                    }

                }

            }
        });


    }


    @Override
    public int getItemCount() {
        return _listData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView _titleTV, _descriptionTV, _counterView_TV, _offerTV;
        public ImageView _listImageView;
        public CustomFavoriteButton _favoriteButton;

        public ListViewHolder(View itemView) {
            super(itemView);
            _titleTV = (TextView) itemView.findViewById(R.id.titleData);
            _descriptionTV = (TextView) itemView.findViewById(R.id.descriptionData);
            _counterView_TV = (TextView) itemView.findViewById(R.id.viewCountData);
            _offerTV = (TextView) itemView.findViewById(R.id.offerData);
            _favoriteButton = (CustomFavoriteButton) itemView.findViewById(R.id.icon_star);
            _listImageView = (ImageView) itemView.findViewById(R.id.list_image);
        }

        @Override
        public void onClick(View v) {

        }
    }

}
