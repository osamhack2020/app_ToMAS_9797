package com.team9797.ToMAS.ui.social.survey;

public class survey_list_item {
    private String title ;
    private String date ;
    private int numpeople ;
    private String name ;
    private String post_id;


    public void setTitle(String a) {
        title = a ;
    }
    public void setDate(String a) {
        date = a ;
    }
    public void setNumpeople(int a) {
        numpeople = a ;
    }
    public void setName(String a) {
        name = a ;
    }
    public void setId(String a){post_id = a;}

    public String getTitle() {
        return this.title ;
    }
    public int getNumpeople() {
        return this.numpeople ;
    }
    public String getName() {
        return this.name ;
    }
    public String getDate() {
        return this.date ;
    }
    public String getId() {return this.post_id ;}

}