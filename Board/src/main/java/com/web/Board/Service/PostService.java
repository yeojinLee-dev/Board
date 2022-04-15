package com.web.Board.Service;

import com.web.Board.Domain.Category.CategoryRepository;
import com.web.Board.Domain.Member.MemberRepository;
import com.web.Board.Domain.Post.Post;
import com.web.Board.Domain.Post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public List<Post> findAllPost() {
        List<Post> posts = postRepository.findAllPost();
        LocalDateTime now = LocalDateTime.now();

        return posts;
    }

    public Post findByPost_Id(int post_id) {
        return postRepository.findByPost_Id(post_id);
    }

    public int updatePost(Post post, String login_id) {
        post.setMember(memberRepository.findByLogin_Id(login_id).get(0));

        return postRepository.updatePost(post);
    }

    public int deletePost(int post_id) {
        return postRepository.deletePost(post_id);
    }

    public int getFirstPost_Id() {
        return postRepository.getFirstPostId();
    }

    public int getLastPost_Id() {
        return postRepository.getLastPostId();
    }

    public int getPostCnt() {
        return postRepository.countTotalPost();
    }

    public List<Post> searchPost(String search_keyword) {
        List<Post> posts = postRepository.searchPost(search_keyword);

        return posts;
    }

}
