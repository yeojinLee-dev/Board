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

    final int size = 10;
    int pageBtnCnt = 1;
    final int pageBtnPerPage = 5;

    public void setPageConfig(int postTotalCnt) {
        if (postTotalCnt%size > 0)
            this.pageBtnCnt = postTotalCnt/size + 1;
        else if (postTotalCnt == 0)
            this.pageBtnCnt = 1;
        else
            this.pageBtnCnt = postTotalCnt/size;
    }

    int getTotalElements() {                                        // 전체 데이터 갯수
        return postRepository.countTotalPost();
    }

    int getTotalPages() {                                           // 쿼리를 통해 가져온 요소들을 size 크기에 맞춰 페이징하였을 때 나오는 총 페이지의 갯수
        int totalPages;
        int totalCnt = getTotalElements();

        if (totalCnt%size > 0)
            totalPages = totalCnt/size + 1;
        else if (totalCnt == 0)
            totalPages = 1;
        else
            totalPages = totalCnt/size;

        return totalPages;
    }

    int getNumberOfElements(int page) {                             // 페이지에 존재하는 요소의 갯수 (최대 size 개)
        int totalCnt = getTotalElements();

        if (totalCnt/size >= page)
            return size;
        else if (totalCnt/size < page)
            return totalCnt%size;

        return -1;
    }

    public List<PageBtn> setPageBtn(int page, String searchKeyword) {
        List<PageBtn> pageBtns = new ArrayList<PageBtn>();
        int btnStart = 1, btnEnd;

        setPageConfig(postRepository.countPostByTitle(searchKeyword));

        if (page%pageBtnPerPage  == 0)
            btnStart = 1 + (page/pageBtnPerPage-1)*pageBtnPerPage;
        else
            btnStart = 1 + (page/pageBtnPerPage)*pageBtnPerPage;

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

    public List<Post> setPageList(int page, String search_keyword ) {
        List<Post> posts;

        int postListStart = size*(page-1);

        posts = postRepository.findPostLimited(postListStart, size, search_keyword);

        return posts;
    }

    public int getCurrentPage(int seq_num) {
        int page = 0;

        setPageConfig(seq_num);
        for (int i = 0; i < this.pageBtnCnt; i++) {
            //System.out.printf("%d\n",  startPostId + postCntPerPage*i);

            if (seq_num >= (1 + size*i) && seq_num < (1 + size*(i+1))) {
                page = i+1;
                break;
            }
        }

        //System.out.printf("PageService : getCurrentPage()\n-> currentPage : %d\n", currentPageNum);

        return page;
    }

}