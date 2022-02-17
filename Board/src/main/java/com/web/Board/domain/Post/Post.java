/* https://jhkang-tech.tistory.com/64 참고 */

package com.web.Board.domain.Post;

import com.web.Board.domain.Category.Category;
import com.web.Board.domain.Member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Post {

    private int post_id;

    private String title;

    private String content;

    private LocalDateTime created_date;

    private Member member;

    private int category_id;

    private int member_id;

    public Post(String title, String content, LocalDateTime created_date, int category_id, int member_id) {

        this.title = title;
        this.content = content;
        this.created_date = created_date;
        this.member_id = member_id;
        this.category_id = category_id;
    }

}
