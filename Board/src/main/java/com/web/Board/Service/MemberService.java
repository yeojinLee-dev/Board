package com.web.Board.Service;

import com.web.Board.Domain.Member.Member;
import com.web.Board.Domain.Member.MemberRepository;
import com.web.Board.Config.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.web.Board.Config.BaseResponseStatus.*;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public int joinMember(Member member) throws BaseException {
        if (checkEmail(member.getEmail()) == 1)
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        if (checkPhone(member.getPhone()) == 1)
            throw new BaseException(POST_USERS_EXISTS_PHONE);
        if (checkLoginId(member.getLogin_id()) == 1)
            throw new BaseException(USERS_EXISTS_LOGINID);
        try {
            return memberRepository.saveMember(member);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

    private int checkLoginId(String login_id) throws BaseException {
        try {
            return memberRepository.checkLoginId(login_id);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    private int checkEmail(String email) throws BaseException {
        try {
            return memberRepository.checkEmail(email);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    private int checkPhone(String phone) throws BaseException {
        try {
            return memberRepository.checkPhone(phone);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int isDuplicateLoginId(String login_id) {
        if (memberRepository.findByLogin_Id(login_id).isEmpty())
            return 0;       // 중복 없음
        else
            return 1;
    }

    public int login(String login_id, String password) {
        if (!memberRepository.findByLoginIdAndPassword(login_id, password).isEmpty())
            return 1;
        else
            return 0;
    }
}
