package com.web.Board.Controller;

import com.web.Board.Service.MemberService;
import com.web.Board.domain.Member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@RestController
public class MemberApiController {
    private final MemberService memberService;

    @PostMapping("/api/member/join")
    public int joinMember(@RequestBody Member member) {
        int isPossibleID = 0;
        //System.out.println(member.getName());

        return memberService.joinMember(member);

    }

    @ResponseBody
    @PostMapping("/api/member/join/check/login-id")
    public int checkLoginId(@RequestParam String login_id) {
        int isDuplicateId = -1;

        if (memberService.isDuplicateLoginId(login_id) == 1)
            isDuplicateId = 1;
        else
            isDuplicateId = 0;

        //System.out.print(isDuplicateID);
        return isDuplicateId;
    }

    @ResponseBody
    @PostMapping("/api/member/join/check/password")
    public int checkPassword(@RequestParam String password1, @RequestParam String password2) {
        int isSamePW = -1;

        if (password1.equals(password2))
            isSamePW = 1;
        else
            isSamePW = 0;

        return isSamePW;
    }

    @PostMapping("/api/member/login")
    public int login(@RequestParam String login_id, @RequestParam String password, HttpServletRequest request) {
        //System.out.println("아이디 => " + login_id);
        HttpSession session = request.getSession();


        return memberService.login(login_id, password);
    }
}
