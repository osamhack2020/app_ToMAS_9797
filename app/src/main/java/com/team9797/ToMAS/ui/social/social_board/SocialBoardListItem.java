package com.team9797.ToMAS.ui.social.social_board;

public class SocialBoardListItem {
    private String title;
    private String date;
    private String writer;
    private String post_id;
    private boolean isRead;




    public void setTitle(String a) {
        title = a ;
    }
    public void setDate(String a) {
        date = a ;
    }
    public void setWriter(String a) {
        writer = a ;
    }
    public void setId(String a) {
        post_id = a ;
    }
    public void setRead(boolean a) {
        isRead = a ;
    }


    public String getTitle() {
        return this.title ;
    }
    public String getDate() { return this.date;}
    public String getWriter() { return this.writer;}
    public String getId() { return this.post_id; }
    public boolean getRead() { return this.isRead; }

}