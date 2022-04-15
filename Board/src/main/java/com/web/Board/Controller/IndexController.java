package com.web.Board.Controller;

import com.web.Board.Service.PageService;
import com.web.Board.Service.CategoryService;
import com.web.Board.Service.MemberService;
import com.web.Board.Service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostService postService;
    private final MemberService memberService;
    private final CategoryService categoryService;
    private final PageService pageService;

    @GetMapping("/")
    public String login() {
        //model.addAttribute("post", postService.findAll());
        return "login";
    }
    @GetMapping("/member/join")
    public String join() {
        return "join";
    }

    @GetMapping("/Board/postList/page={pageNum}&query={searchKeyword}")
    public String postList(@PathVariable int pageNum, @PathVariable String searchKeyword, Model postList,
                           Model Login_Id, Model pageBtn, Model currentPage, Model search_keyword, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String login_id = (String) session.getAttribute("login_id");

        Login_Id.addAttribute("login_id", login_id);

        postList.addAttribute("postList", pageService.setPageList(pageNum, searchKeyword));
        //System.out.printf("IndexController.postList() : postList");

        pageBtn.addAttribute("pageBtn", pageService.setPageBtn(pageNum, searchKeyword));
        //System.out.printf("IndexController.postList() : pageBtn");

        currentPage.addAttribute("currentPage", pageNum);
        //System.out.printf("IndexController.postList() : currentPage : %d\n", pageNumParam);

        search_keyword.addAttribute("searchKeyword", searchKeyword);
        return "post-list";
    }

    @GetMapping("/Board/post/create")
    public String createPost(Model member, Model category, Model last_page, Model seq_num, HttpServletRequest request) {
        HttpSession session = request.getSession();

        String login_id = (String) session.getAttribute("login_id");
        int post_seq_num = postService.getPostCnt() + 1;

        seq_num.addAttribute("seq_num", post_seq_num);
        member.addAttribute("login_id", login_id);
        category.addAttribute("category", categoryService.findAllCategory());
        last_page.addAttribute("last_page", pageService.getCurrentPage(post_seq_num));

        //System.out.printf("\nIndexController -> createPost()\nlast_page : %d\n", pageService.getCurrentPage(post_seq_num));
        return "post-create";
    }

    @GetMapping("/member/info")
    public String memberInfo () {
        return "member-info";
    }

    @GetMapping("/Board/post/read/{post_id}&page={currentPage}")
    public String readPost(@PathVariable int post_id, @PathVariable int currentPage,
                           Model Post, Model Login_id, Model CurrentPage, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String login_id = (String) session.getAttribute("login_id");

        Login_id.addAttribute("login_id", login_id);
        Post.addAttribute("post", postService.findByPost_Id(post_id));
        CurrentPage.addAttribute("currentPage", currentPage);

        //System.out.printf("IndexController -> readPost()\n currentPage : %d\n", currentPage);


        return "post-read";
    }

    @GetMapping("/Board/post/update/{post_id}&page={currentPage}")
    public String readPostToUpdate(@PathVariable int post_id, @PathVariable int currentPage, Model CurrentPage, Model Post, Model Category) {
        Post.addAttribute("post", postService.findByPost_Id(post_id));
        Category.addAttribute("category", categoryService.findAllCategory());
        CurrentPage.addAttribute("currentPage", currentPage);

        return "post-modify";
    }

    /*
    @GetMapping("/Board/post/list&search.keyword={search_keyword}&pageNum={pageNumParam}")
    public String searchPost(@PathVariable String search_keyword, @PathVariable int pageNumParam,
                             Model postList, Model Login_Id, Model pageBtn, Model currentPage, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String login_id = (String) session.getAttribute("login_id");

        //System.out.printf("\nsearchPost : %s\n", search_keyword);
        Login_Id.addAttribute("login_id", login_id);
        postList.addAttribute("postList", postService.searchPost(search_keyword));
        pageBtn.addAttribute("pageBtn", pageService.setPageBtn(pageNumParam, ""));

        currentPage.addAttribute("currentPage", pageNumParam);

        return "post-list";
    }
    */
}
