package com.web.Board.Service;

import com.web.Board.Domain.Post.PageBtn;
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
    private final PostService postService;

    final int postCntPerPage = 10;
    int pageBtnCnt = 1;
    final int pageBtnPerPage = 5;

    public void setPageConfig() {
        int postTotalCnt = postRepository.countTotalPost();

        if (postTotalCnt%postCntPerPage > 0)
            this.pageBtnCnt = postTotalCnt/postCntPerPage + 1;
        else if (postTotalCnt == 0)
            this.pageBtnCnt = 1;
        else
            this.pageBtnCnt = postTotalCnt/postCntPerPage;
    }

    public void setPageConfig(int postTotalCnt) {
        if (postTotalCnt%postCntPerPage > 0)
            this.pageBtnCnt = postTotalCnt/postCntPerPage + 1;
        else if (postTotalCnt == 0)
            this.pageBtnCnt = 1;
        else
            this.pageBtnCnt = postTotalCnt/postCntPerPage;
    }

    public List<PageBtn> setPageBtn(int currentPageNum) {
        List<PageBtn> pageBtns = new ArrayList<PageBtn>();
        int btnStart = 1, btnEnd;

        setPageConfig();

        if (currentPageNum%pageBtnPerPage == 0)
            btnStart = 1 + (currentPageNum/pageBtnPerPage-1)*pageBtnPerPage;
        else
            btnStart = 1 + (currentPageNum/pageBtnPerPage)*pageBtnPerPage;

        if (btnStart + pageBtnPerPage < pageBtnCnt)
            btnEnd = btnStart + pageBtnPerPage;
        else
            btnEnd = pageBtnCnt;


        if (btnStart == 1) {
            for (int i = 0; i < btnEnd; i++) {
                if (btnStart + pageBtnPerPage < pageBtnCnt && i == btnEnd-1) {
                    PageBtn pageBtn = new PageBtn(">>", i+1);
                    pageBtns.add(pageBtn);
                }
                else {
                    PageBtn pageBtn = new PageBtn(Integer.toString(i+1), i+1);
                    pageBtns.add(pageBtn);
                }
            }
        }
        else if (btnStart > pageBtnPerPage) {
            for (int i = btnStart-1; i < btnEnd+1; i++) {
                if (i == btnStart-1) {
                    PageBtn pageBtn = new PageBtn("<<", i);
                    pageBtns.add(pageBtn);
                }
                else if (btnStart + pageBtnPerPage <= pageBtnCnt && i == btnEnd) {
                    PageBtn pageBtn = new PageBtn(">>", i);
                    pageBtns.add(pageBtn);
                }
                else {
                    PageBtn pageBtn = new PageBtn(Integer.toString(i), i);
                    pageBtns.add(pageBtn);
                }
            }
        }

        return pageBtns;
    }

    public List<Post> setPageList(int currentPageNum) {
        List<Post> posts;

        int postListStart = postCntPerPage*(currentPageNum-1);

        posts = postRepository.findPostLimited(postListStart, postCntPerPage);

        return posts;
    }

    public int getCurrentPage(int seq_num) {
        int currentPageNum = 0;

        setPageConfig(seq_num);
        for (int i = 0; i < this.pageBtnCnt; i++) {
            //System.out.printf("%d\n",  startPostId + postCntPerPage*i);

            if (seq_num >= (1 + postCntPerPage*i) && seq_num < (1 + postCntPerPage*(i+1))) {
                currentPageNum = i+1;
                break;
            }
        }

        System.out.printf("PageService : getCurrentPage()\n-> currentPage : %d\n", currentPageNum);

        return currentPageNum;
    }

}