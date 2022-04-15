package com.web.Board.Domain.Post;

public class PageBtn {
    private String button;     // 하단에 표시되는 페이지 번호
    private int pageNumber;    // 실제 페이지 번호

    public PageBtn(String button, int pageNumber) {
        this.button = button;
        this.pageNumber = pageNumber;
    }
}
