package com.web.Board.Domain.Post;

import com.web.Board.Domain.Category.Category;
import com.web.Board.Domain.Member.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PostRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public PostRepository(DataSource dataSource) {

        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int savePost(Post post) {
        List<Member> member = jdbcTemplate.query( "select * from board.MEMBER where LOGIN_ID = ? ", MemberRowMapper(), post.getMember().getLogin_id());

        String sql = "insert into board.POST (TITLE, MEMBER_ID, CONTENT, CATEGORY_ID, CREATED_DATE) " +
                "values (?, (select MEMBER_ID from board.MEMBER where LOGIN_ID = ? limit 1), ?, ?, ?)";

        //System.out.println("savePost() : " + String.format("%s\n%s\n%s\n%d", post.getTitle(),
                //post.getMember().getLogin_id(), post.getContent(), post.getCategory_id()));
        LocalDateTime postSaveTime = LocalDateTime.now();
        jdbcTemplate.update(sql, post.getTitle(), post.getMember().getLogin_id(), post.getContent(), post.getCategory_id(), postSaveTime);

        return 1;
    }

    public int countPostByTitleAndCategory(String searchKeyword, int category_id) {
        int postCnt = 0;

        if (searchKeyword.equals("") && category_id == -1)
            return countTotalPost();
        else if (category_id != -1){
            postCnt = jdbcTemplate.queryForObject("select count(*) from board.POST where title like ? and category_id = ?",
                    Integer.class, "%"+searchKeyword+"%", category_id);
            //System.out.printf("\nPostRepository -> countPostByTitleAndCategory()\n postCount : %d\n", postCnt);
        }

        return postCnt;
    }

    public List<Post> findAllPost() {
        List<Post> posts = jdbcTemplate.query("select * from board.POST", PostRowMapper());
        posts = getMembernCategory(posts);

        return posts;
    }

    public List<Post> findPostLimited(int limit_start, int pagePostCnt, String search_keyword, int category_id) {
        List<Post> posts;

        if (category_id == -1) {
            posts = jdbcTemplate.query("SELECT p.post_id, title, created_date, member_id, category_id, " +
                            "IF (comment_count IS null, 0, comment_count) AS comment_count " +
                            "FROM board.POST p " +
                            "LEFT JOIN (SELECT post_id, COUNT(comment_id) AS comment_count " +
                            "FROM board.COMMENT GROUP BY post_id) c ON p.post_id = c.post_id " +
                            "WHERE title LIKE ? " +
                            "ORDER BY post_id DESC limit ?, ?;"
                    ,PostListRowMapper(),  "%"+search_keyword+"%", limit_start, pagePostCnt);
        }
        else {
            posts = jdbcTemplate.query("SELECT p.post_id, title, created_date, member_id, category_id, " +
                            "IF (comment_count IS null, 0, comment_count) AS comment_count " +
                            "FROM board.POST p " +
                            "LEFT JOIN (SELECT post_id, COUNT(comment_id) AS comment_count " +
                            "FROM board.COMMENT GROUP BY post_id) c ON p.post_id = c.post_id " +
                            "WHERE title LIKE ? and category_id = ? " +
                            "ORDER BY post_id DESC limit ?, ?;"
                    ,PostListRowMapper(),  "%"+search_keyword+"%", category_id, limit_start, pagePostCnt);
        }
        posts = getMembernCategory(posts);
        return posts;
    }

    public Post findByPost_Id(int post_id) {
        List<Post> post = jdbcTemplate.query("select * from board.POST where POST_ID = ? limit 1", PostRowMapper(), post_id);
        post = getMembernCategory(post);

        //System.out.printf("postRepository -> findByPost_Id()\n post_id : %d\n", post.get(0).getPost_id());
        return post.get(0);
    }

    public int updatePost(Post post) {
        //System.out.printf("postRepository -> updatePost()\n 수정된 게시물 번호 : %d\n", post.getPost_id());
        return jdbcTemplate.update("update board.Post set CATEGORY_ID = ?, TITLE = ?, CONTENT = ? where POST_ID = ?", post.getCategory_id(),
                post.getTitle(), post.getContent(), post.getPost_id());
    }

    public int deletePost(int post_id) {
        jdbcTemplate.update("delete from board.COMMENT where POST_ID = ?", post_id);

        return jdbcTemplate.update("delete from board.POST where POST_ID = ?", post_id);
    }

    private List<Post> getMembernCategory(List<Post> posts) {
        List<Member> member;
        List<Category> category;

        for (int i = 0; i < posts.size(); i++) {
            member = jdbcTemplate.query("select * from board.MEMBER where MEMBER_ID = ?", MemberRowMapper(), posts.get(i).getMember_id());
            category = jdbcTemplate.query("select * from board.CATEGORY where CATEGORY_ID = ?", CategoryRowMapper(), posts.get(i).getCategory_id());

            posts.get(i).setMember(member.get(0));
            posts.get(i).setCategory(category.get(0));

        }
        return posts;
    }

    public int countTotalPost() {
        return jdbcTemplate.queryForObject("select count(*) from board.POST", Integer.class);
    }

    public int getFirstPostId() {
        int firstPost_Id;

        try {
            firstPost_Id = jdbcTemplate.queryForObject("select post_id from board.POST order by post_id asc limit 1;", Integer.class);
        } catch (Exception e) {
            firstPost_Id = 0;
        }

        //System.out.printf("first page number => %d\n", firstPost_Id);

        return firstPost_Id;
    }

    public int getLastPostId() {
        int lastPost_Id = 0;

        try {
            lastPost_Id = jdbcTemplate.queryForObject("select post_id from board.POST order by post_id desc limit 1;", Integer.class);
        } catch (Exception e) {
            lastPost_Id = 0;
        }

        //System.out.printf("last page number => %d\n", lastPost_Id);

        return lastPost_Id;
    }

    public List<Post> searchPost(String search_keyword) {
        //System.out.printf("\nrepository : searchPost -> %s\n", "\'%"+search_keyword+"%\'");
        List<Post> posts = jdbcTemplate.query("select * from board.POST where TITLE like ?;", PostRowMapper(), "%"+search_keyword+"%");

        posts = getMembernCategory(posts);

        return posts;
    }

    private RowMapper<Post> PostRowMapper() {
        return (rs, rowNum) -> {
            Post post = new Post();

            post.setPost_id(rs.getInt("POST_ID"));
            post.setTitle(rs.getString("TITLE"));
            post.setContent(rs.getString("CONTENT"));
            post.setMember_id(rs.getInt("MEMBER_ID"));
            post.setCategory_id(rs.getInt("CATEGORY_ID"));
            post.setCreated_date(rs.getTimestamp("CREATED_DATE").toLocalDateTime());

            return post;
        };
    }
    private RowMapper<Post> PostListRowMapper() {
        return (rs, rowNum) -> {
            Post post = new Post();

            post.setPost_id(rs.getInt("POST_ID"));
            post.setTitle(rs.getString("TITLE"));
            post.setMember_id(rs.getInt("MEMBER_ID"));
            post.setCategory_id(rs.getInt("CATEGORY_ID"));
            post.setCreated_date(rs.getTimestamp("CREATED_DATE").toLocalDateTime());
            post.setComment_count((rs.getInt("COMMENT_COUNT")));
            return post;
        };
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

    private RowMapper<Category> CategoryRowMapper() {
        return (rs, rowNum) -> {
            Category category = new Category();
            category.setCategory_id(rs.getInt("CATEGORY_ID"));
            category.setName(rs.getString("NAME"));
            category.set_deleted(rs.getBoolean("IS_DELETED"));
            return category;
        };
    }

}
