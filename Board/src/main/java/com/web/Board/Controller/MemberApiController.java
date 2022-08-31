package com.web.Board.Controller;

import com.web.Board.Domain.Member.MemberLoginReq;
import com.web.Board.Service.MemberService;
import com.web.Board.Domain.Member.Member;
import com.web.Board.Config.BaseException;
import com.web.Board.Config.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.web.Board.Config.BaseResponseStatus.*;
import static com.web.Board.utils.ValidationRegex.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class MemberApiController {
    private final MemberService memberService;

    @PostMapping("/join")
    public BaseResponse<Integer> joinMember(@RequestBody Member member) {
        if (member.getLogin_id().length() == 0)
            return new BaseResponse<>(USERS_EMPTY_USER_ID);
        if (member.getPassword().length() == 0)
            return new BaseResponse<>(USERS_EMPTY_USER_PASSWORD);
        if (member.getName().length() == 0)
            return new BaseResponse<>(USERS_EMPTY_USER_NAME);
        if (member.getEmail().length() == 0)
            return new BaseResponse<>(USERS_EMPTY_USER_EMAIL);
        if (member.getPhone().length() == 0)
            return new BaseResponse<>(USERS_EMPTY_USER_PHONE);
        if (!isRegexEmail(member.getEmail()))
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        if (!isRegexPhone(member.getPhone()))
            return new BaseResponse<>(POST_USERS_INVAlILD_PHONE);

        try {
            return new BaseResponse<>(memberService.joinMember(member));
        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @PostMapping("/join/login-id")
    public int checkLoginId(@RequestParam String login_id) {
        int isDuplicateId = -1;

        if (memberService.isDuplicateLoginId(login_id) == 1)
            isDuplicateId = 1;
        else
            isDuplicateId = 0;

        //System.out.print(isDuplicateID);
        return isDuplicateId;
    }

    @PostMapping("/join/password")
    public int checkPassword(@RequestParam String password1, @RequestParam String password2) {
        int isSamePW = -1;

        if (password1.equals(password2))
            isSamePW = 1;
        else
            isSamePW = 0;

        return isSamePW;
    }

    @PostMapping("/login")
    public int login(@RequestBody MemberLoginReq memberLoginReq, HttpServletRequest request) {
        //System.out.println("아이디 => " + login_id);
        HttpSession session = request.getSession();
        session.setAttribute("login_id", memberLoginReq.getLogin_id());

        return memberService.login(memberLoginReq.getLogin_id(), memberLoginReq.getPassword());
    }

    @PostMapping("/logout")
    public int logout(HttpSession session) {
        session.removeAttribute("login_id");

        //System.out.printf("세션에서 로그인 정보 삭제 완료\n");

        return 1;
    }
}
