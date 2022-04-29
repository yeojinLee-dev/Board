package com.web.Board.Domain.Member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class MemberRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    String sql;

    public MemberRepository(DataSource dataSource) {

        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int saveMember(Member member) {

        sql = "insert into board.MEMBER (NAME, LOGIN_ID, PASSWORD, EMAIL, PHONE) " +
                "values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, member.getName(), member.getLogin_id(), member.getPassword(), member.getEmail(), member.getPhone());

        return 1;   // ajax의 done()은 long이나 int 등의 숫자형이 무조건 반환이 되어야 에러가 없는 것으로 인식한다.
    }

    public List<Member> findByMember_Id(int member_id) {
        //System.out.println("findByMember_Id(member_id) => "+ Integer.toString(member_id));
        return jdbcTemplate.query("select M.* from board.MEMBER M where M.MEMBER_ID = ? ", MemberRowMapper(), member_id);
    }

    public List<Member> findByLogin_Id(String login_id) {
        //System.out.println("findByLogin_Id(login_id) => "+ login_id);
        return jdbcTemplate.query( "select * from board.MEMBER where LOGIN_ID = ? ", MemberRowMapper(), login_id);
    }

    public List<Member> findByPassword(String password) {
        //System.out.println("findByLogin_Id(password) => "+ password);
        return jdbcTemplate.query( "select * from board.MEMBER where PASSWORD = ? ", MemberRowMapper(), password);
    }

    public List<Member> findByLoginIdAndPassword(String login_id, String password) {
        return jdbcTemplate.query("select * from board.MEMBER where LOGIN_ID = ? AND PASSWORD = ?", MemberRowMapper(), login_id, password);
    }

    private RowMapper<Member> MemberRowMapper() {
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setName(rs.getString("NAME"));
            member.setLogin_id(rs.getString("LOGIN_ID"));
            member.setPassword(rs.getString("PASSWORD"));
            member.setEmail(rs.getString("EMAIL"));
            member.setPhone(rs.getString("PHONE"));
            return member;
        };
    }
}
