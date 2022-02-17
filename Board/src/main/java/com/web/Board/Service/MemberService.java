package com.web.Board.Service;

import com.web.Board.domain.Member.Member;
import com.web.Board.domain.Member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public int joinMember(Member member) {

        return memberRepository.saveMember(member);
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
