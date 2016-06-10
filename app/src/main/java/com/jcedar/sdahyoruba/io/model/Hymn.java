package com.jcedar.sdahyoruba.io.model;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Afolayan Oluwaseyi on 29/05/2016.
 */
public class Hymn {
    @Expose
    @SerializedName("id")
    private String id;

    @Expose
    @SerializedName("song_id")
    private String songId;

    @Expose
    @SerializedName("song_text")
    private String songText;

    @Expose
    @SerializedName("song_title")
    private String songTitle;

    @Expose
    @SerializedName("english_version")
    private String englishVersion;

    public String getId() {
        return id;
    }

    public String getSongId() {
        return songId;
    }

    public String getSongText() {
        return songText;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public String getEnglishVersion() {
        return englishVersion;
    }

    public static Hymn[] fromJson(String json){
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
                .create().fromJson(json,  Hymn[].class);
    }
}
