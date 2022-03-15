package com.web.Board.Domain.Post;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PageBtn {
    private String pageNum;     // 하단에 표시되는 페이지 번호
    private int pageNumParam;   // 실제 페이지 번호

    public PageBtn(String pageNum, int pageNumParam) {
        this.pageNum = pageNum;
        this.pageNumParam = pageNumParam;
    }
}
