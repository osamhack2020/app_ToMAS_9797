package com.team9797.ToMAS.ui.home.group;

public class ParticipantListItem {
    private String phone;
    private String name ;
    private String position;




    public void setName(String a) {
        name = a ;
    }
    public void setPosition(String a) {
        position = a ;
    }
    public void setPhone(String a) {
        phone = a ;
    }


    public String getName() {
        return this.name ;
    }
    public String getPosition() { return this.position;}
    public String getPhone() { return this.phone;}

}