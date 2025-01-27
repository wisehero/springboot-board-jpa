package org.programmers.board.dto;

public class MemberCreateResponse {

    private final String name;
    private final int age;
    private final String hobby;

    public MemberCreateResponse(String name, int age, String hobby) {
        this.name = name;
        this.age = age;
        this.hobby = hobby;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getHobby() {
        return hobby;
    }
}