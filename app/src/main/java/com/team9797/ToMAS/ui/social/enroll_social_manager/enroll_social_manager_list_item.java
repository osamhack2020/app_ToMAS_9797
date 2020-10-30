package com.team9797.ToMAS.ui.social.enroll_social_manager;

public class enroll_social_manager_list_item {
    private String belong;
    private String name;
    private String tmp_class;
    private String user_id;



    public void setName(String a) {
        name = a ;
    }
    public void setBelong(String a) {
        belong = a ;
    }
    public void setTmpClass(String a) {
        tmp_class = a ;
    }
    public void setId(String a){user_id = a;}

    public String getBelong() {
        return this.belong ;
    }
    public String getTmpClass() {
        return this.tmp_class ;
    }
    public String getName() {
        return this.name ;
    }
    public String getId() {return this.user_id ;}

}