package com.team9797.ToMAS.ui.mypage;

public class BuyListItem {
    private String title ;
    private String date ;
    private String post_id;


    public void setTitle(String a) {
        title = a ;
    }
    public void setDate(String a) {
        date = a ;
    }
    public void setId(String a){post_id = a;}

    public String getTitle() {
        return this.title ;
    }
    public String getDate() {
        return this.date ;
    }
    public String getId() {return this.post_id ;}

}