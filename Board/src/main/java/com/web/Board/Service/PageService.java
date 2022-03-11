package com.web.Board.Service;

import com.web.Board.Domain.PageBtn;
import com.web.Board.Domain.Post.Post;
import com.web.Board.Domain.Post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PageService {

    private final PostRepository postRepository;

    final int postCntPerPage = 10;
    int pageBtnCnt;

    public void setPageConfig() {
        int postCnt = postRepository.countPost();

        if (postCnt%postCntPerPage > 0)
            this.pageBtnCnt = postCnt/postCntPerPage + 1;
        else
            this.pageBtnCnt = postCnt/postCntPerPage;
    }

    public List<PageBtn> setPageBtn() {
        List<PageBtn> pageBtns = new ArrayList<PageBtn>();

        setPageConfig();
        for (int i = 0; i < this.pageBtnCnt; i++) {
            PageBtn pageBtn = new PageBtn(i+1);
            pageBtns.add(pageBtn);
        }

        return pageBtns;
    }

    public List<Post> setPageList(int pageNum) {
        List<Post> posts;
        int postListStart = postCntPerPage*(pageNum-1);

        posts = postRepository.findPostLimited(postListStart, postCntPerPage);

        return posts;
    }








}