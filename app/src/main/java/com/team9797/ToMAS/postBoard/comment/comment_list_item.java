package com.team9797.ToMAS.postBoard.comment;

public class comment_list_item {
    private String title ;
    private String date ;
    private String writer;
    private String html;
    private String post_id;
    private String writer_id;


    public void setTitle(String a) {
        title = a ;
    }
    public void setWriter(String a) {
        writer = a ;
    }
    public void setDate(String a) {
        date = a ;
    }
    public void setHtml(String a) {
        html = a ;
    }
    public void setId(String a){post_id = a;}
    public void setWriterId(String a){writer_id = a;}

    public String getTitle() {
        return this.title ;
    }
    public String getWriter() {
        return this.writer ;
    }
    public String getDate() {
        return this.date ;
    }
    public String getHtml() {
        return this.html ;
    }
    public String getId() {return this.post_id ;}
    public String getWriterId(){return this.writer_id;}

}