package cn.hl.ox.annotation.scan.entity;

import cn.hl.ox.annotation.scan.PeopleAnnotion;

public class YellowPeople extends IPeople {

    @Override
    @PeopleAnnotion(say = "Yellow")
    public void say() {
        System.out.println("I'm YELLOW!");
    }

}