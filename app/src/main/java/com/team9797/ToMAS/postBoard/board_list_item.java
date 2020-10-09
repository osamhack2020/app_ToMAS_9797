package com.team9797.ToMAS.postBoard;
import android.graphics.drawable.Drawable;

public class board_list_item {
    private String title ;
    private String date ;
    private String sub ;
    private String name ;
    private String clicks ;
    private String post_id;


    public void setTitle(String a) {
        title = a ;
    }
    public void setDate(String a) {
        date = a ;
    }
    public void setSub(String a) {
        sub = a ;
    }
    public void setName(String a) {
        name = a ;
    }
    public void setClicks(String a) {
        clicks = a ;
    }
    public void setId(String a){post_id = a;}

    public String getTitle() {
        return this.title ;
    }
    public String getSub() {
        return this.sub ;
    }
    public String getName() {
        return this.name ;
    }
    public String getClicks() {
        return this.clicks ;
    }
    public String getDate() {
        return this.date ;
    }
    public String getId() {return this.post_id ;}

}