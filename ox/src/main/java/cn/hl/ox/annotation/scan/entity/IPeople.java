package cn.hl.ox.annotation.scan.entity;

import cn.hl.ox.annotation.scan.PeopleAnnotion;

public abstract class IPeople {
    @PeopleAnnotion(say = "NoColor")
    public abstract void say();

    public void walk() {
        System.out.println("I can Walk");
    }
}

