package picazzy.picazzy.com.picazzy.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ashish on 19/5/16.
 */
public class ImageParam {

    @SerializedName("code")
    private String codeTitle;
    @SerializedName("image")
    private String base64ImageString;
    @SerializedName("dimension")
    private String dimension;
    @SerializedName("effectname")
    private String effectName;
    @SerializedName("height")
    private String height="mobile";


    public String getCodeTitle() {
        return codeTitle;
    }

    public void setCodeTitle(String codeTitle) {
        this.codeTitle = codeTitle;
    }

    public String getBase64ImageString() {
        return base64ImageString;
    }

    public void setBase64ImageString(String base64ImageString) {
        this.base64ImageString = base64ImageString;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getEffectName() {
        return effectName;
    }

    public void setEffectName(String effectName) {
        this.effectName = effectName;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
