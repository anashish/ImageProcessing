package picazzy.picazzy.com.picazzy;

/**
 * Created by Mohit on 4/23/2016.
 */
public class EffectsModel {
    private String title;

    public int getImageDrawable() {
        return imageDrawable;
    }

    public void setImageDrawable(int imageDrawable) {
        this.imageDrawable = imageDrawable;
    }

    private int imageDrawable;
    public String code;

    public EffectsModel() {
    }

    public EffectsModel(String title,int imageDrawable,String code) {
        this.title = title;
        this.imageDrawable = imageDrawable;
        this.code=code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}