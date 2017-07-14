package an.favlistapp.util;

/**
 * Created by sahitya on 13/7/17.
 */

public class ListUtils {

    public ListUtils() {

    }

    private String _title;
    private String _description;
    private String _type;
    private String _viewCount;
    private String _imageUrl;
    private boolean _fav;


    public String get_title() {
        return _title;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public String get_description() {
        return _description;
    }

    public void set_description(String _description) {
        this._description = _description;
    }

    public String get_type() {
        return _type;
    }

    public void set_type(String _type) {
        this._type = _type;
    }

    public String get_viewCount() {
        return _viewCount;
    }

    public void set_viewCount(String _viewCount) {
        this._viewCount = _viewCount;
    }

    public String get_imageUrl() {
        return _imageUrl;
    }

    public void set_imageUrl(String _imageUrl) {
        this._imageUrl = _imageUrl;
    }

    public boolean is_fav() {
        return _fav;
    }

    public void set_fav(boolean _fav) {
        this._fav = _fav;
    }
}
