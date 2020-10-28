package com.team9797.ToMAS.ui.social.survey;

public class SurveyListItem {
    private String title ;
    private String date ;
    private String name ;
    private boolean isCheck;
    private String post_id;


    public void setTitle(String a) {
        title = a ;
    }
    public void setDate(String a) {
        date = a ;
    }
    public void setName(String a) {
        name = a ;
    }
    public void setCheck(boolean a){isCheck = a;}
    public void setId(String a){post_id = a;}

    public String getTitle() {
        return this.title ;
    }
    public String getName() {
        return this.name ;
    }
    public String getDate() {
        return this.date ;
    }
    public boolean getCheck() { return this.isCheck;}
    public String getId() {return this.post_id ;}

}