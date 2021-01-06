package cn.hl.ox.annotation.scan.entity;

import cn.hl.ox.annotation.scan.PeopleAnnotion;

public class BlackPeople extends IPeople {

    @Override
    @PeopleAnnotion(say = "Black")
    public void say() {
        System.out.println("I'm BLACK!");
    }

}