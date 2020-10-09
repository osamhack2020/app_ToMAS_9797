package com.team9797.ToMAS.ui.home.group;

public class group_list_item {
    private String title ;
    private String numpeople;
    private String place;
    private String date ;
    private String time ;
    private String post_id;


    public void setTitle(String a) {
        title = a ;
    }
    public void setNumpeople(String a) {
        numpeople = a ;
    }
    public void setPlace(String a) {
        place = a ;
    }
    public void setDate(String a) {
        date = a ;
    }
    public void setTime(String a) {
        time = a ;
    }
    public void setId(String a){post_id = a;}

    public String getTitle() {
        return this.title ;
    }
    public String getNumpeople() {
        return this.numpeople ;
    }
    public String getPlace() {
        return this.place ;
    }
    public String getDate() {
        return this.date ;
    }
    public String getTime() {
        return this.time ;
    }
    public String getId() {return this.post_id ;}

}