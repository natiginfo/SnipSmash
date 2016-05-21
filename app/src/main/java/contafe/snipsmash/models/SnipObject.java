package contafe.snipsmash.models;

import java.util.ArrayList;

/**
 * Created by aspanu on 5/21/16.
 */
public class SnipObject {

    private String updatedAt;
    private String wafeformData;
    private String creatorUserName;
    private String creatorDisplayName;
    private String urlAAC;
    private ArrayList<String> culturalSelections;
    private String slug;
    private String name;
    private String url;
    private String waveform640x128;

    public SnipObject(
            String updatedAt,
            String wafeformData,
            String urlAAC,
            String slug,
            String name,
            String url,
            String waveform640x128
    ) {
        this.updatedAt = updatedAt;
        this.wafeformData = wafeformData;
        this.urlAAC = urlAAC;
        this.slug = slug;
        this.name = name;
        this.url = url;
        this.waveform640x128 = waveform640x128;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getWafeformData() {
        return wafeformData;
    }

    public void setWafeformData(String wafeformData) {
        this.wafeformData = wafeformData;
    }

    public String getUrlAAC() {
        return urlAAC;
    }

    public void setUrlAAC(String urlAAC) {
        this.urlAAC = urlAAC;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWaveform640x128() {
        return waveform640x128;
    }

    public void setWaveform640x128(String waveform640x128) {
        this.waveform640x128 = waveform640x128;
    }
}


