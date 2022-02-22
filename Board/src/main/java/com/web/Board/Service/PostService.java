package com.web.Board.Service;

import com.web.Board.Domain.Category.Category;
import com.web.Board.Domain.Category.CategoryRepository;
import com.web.Board.Domain.Member.MemberRepository;
import com.web.Board.Domain.Post.Post;
import com.web.Board.Domain.Post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Category> findAllCategory() {
        return categoryRepository.findAllCategory();
    }

    public List<Post> findAllPostList() {
        return postRepository.findAllPost();
    }

}
