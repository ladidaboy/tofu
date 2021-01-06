package cn.hl.ox.annotation.scan.entity;

import cn.hl.ox.annotation.scan.PeopleAnnotion;

public class WhitePeople extends IPeople {

    @Override
    @PeopleAnnotion(say = "White")
    public void say() {
        System.out.println("I'm WHITE!");
    }

}