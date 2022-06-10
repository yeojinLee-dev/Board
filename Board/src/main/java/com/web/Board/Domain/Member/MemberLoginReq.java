package com.web.Board.Domain.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MemberLoginReq {
    private String login_id;
    private String password;
}
