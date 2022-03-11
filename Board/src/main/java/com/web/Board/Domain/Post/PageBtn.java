package com.web.Board.Domain.Post;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PageBtn {
    private int pageNum;
    public PageBtn(int pageNum) {
        this.pageNum = pageNum;
    }
}
