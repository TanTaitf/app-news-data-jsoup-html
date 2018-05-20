package com.media.tf.ung_dung_doc_bao.Class_Imlepment;

/**
 * Created by Windows 8.1 Ultimate on 27/10/2016.
 */

public class Docbao {
    public String title;
    public String link;
    public String image;

    public Docbao(String title, String link, String image){
        this.title = title;
        this.link = link;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public String getImage() {
        return image;
    }
}
