package com.web.Board.domain.Member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Email;

@NoArgsConstructor
@Getter
@Setter
public class Member {

    private int member_id;

    private String name;

    private String login_id;

    private String password;

    private String email;

    private String phone;

    private int post_id;

    public Member(int member_id, String name, String login_id, String password, String email, String phone, int post_id) {
        this.member_id = member_id;
        this.name = name;
        this.login_id = login_id;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.post_id = post_id;
    }

}
