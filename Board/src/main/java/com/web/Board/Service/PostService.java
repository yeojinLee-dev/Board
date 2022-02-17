package com.web.Board.Service;

import com.web.Board.domain.Category.Category;
import com.web.Board.domain.Category.CategoryRepository;
import com.web.Board.domain.Member.Member;
import com.web.Board.domain.Member.MemberRepository;
import com.web.Board.domain.Post.Post;
import com.web.Board.domain.Post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;

    public int createPost(Post post, String login_id) {
        post.setMember(memberRepository.findByLogin_Id(login_id).get(0));


        return postRepository.savePost(post);
    }

    public int saveCategory(Category category) {
        return categoryRepository.saveCategory(category);
    }
}
