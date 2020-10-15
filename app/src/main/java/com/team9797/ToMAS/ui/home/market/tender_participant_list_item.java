package com.team9797.ToMAS.ui.home.market;

public class tender_participant_list_item {
    private int num;
    private String name ;
    private int price;




    public void setName(String a) {
        name = a ;
    }
    public void setPrice(int a) {
        price = a ;
    }
    public void setNum(int a) {
        num = a ;
    }


    public String getName() {
        return this.name ;
    }
    public int getPrice() { return this.price;}
    public int getNum() { return this.num;}

}