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

    private Category category;

    public Post(String title, String content, LocalDateTime created_date, Member member, Category category) {

        this.title = title;
        this.content = content;
        this.created_date = created_date;
        this.member = member;
        this.category = category;
    }

}
