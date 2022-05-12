package site.metacoding.springbootmustacheblog.web;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import site.metacoding.springbootmustacheblog.domain.post.Post;
import site.metacoding.springbootmustacheblog.domain.user.User;
import site.metacoding.springbootmustacheblog.service.PostService;

@RequiredArgsConstructor
@Controller
public class PostController {

    private final HttpSession session;
    private final PostService postService;

    // GET 글쓰기 페이지 /post/writeForm - 인증 O
    @GetMapping("/s/post/writeForm")
    public String writeForm() {

        if (session.getAttribute("principal") == null) {
            return "redirect:/loginForm";
        }

        return "post/writeForm";
    }

    // 메인 페이지 - 인증 X
    // GET 글 목록 페이지 /post/list/
    @GetMapping({ "/", "post/list" }) // { "/", "post/list" }로 쓰면 두 가지 방법으로 들어올 수 있음
    public String list(@RequestParam(defaultValue = "0") Integer page, Model model) {

        Page<Post> pagePosts = postService.글목록보기(page);

        // 2. model에 담기
        model.addAttribute("posts", pagePosts); // paging
        model.addAttribute("nextPage", page + 1);
        model.addAttribute("previewPage", page - 1);
        // 3. mustache 파일에 뿌리기

        return "post/list";
    }

    // 글 상세보기 페이지 /post/{id} (삭제버튼 만들어두면 되니까 삭제페이지 필요 X)
    @GetMapping("/post/{id}") // Get요청에 /post 제외시키기(인증 X)
    public String detail(@PathVariable Integer id, Model model) { // int는 null이 없음, 초기값이 0
        // Integer는 초기값이 null

        Post postEntity = postService.글상세보기(id); // EAGER니까 user 가지고있음

        if (postEntity == null) {
            return "error/page1";
        } else {
            model.addAttribute("post", postEntity);
            return "post/detail";
        }
    }

    // 글 수정 페이지 /post/{id}/updateForm - 인증 O
    @GetMapping("/s/post/{id}/updateForm")
    public String updateForm(@PathVariable Integer id) {
        return "post/updateForm"; // ViewResolver 도움 받음
    }

    // DELETE 글 삭제 /post/{id} -> 글 목록으로 가기 - 인증 O
    @DeleteMapping("/s/post/{id}")
    public String delete(@PathVariable Integer id) {
        return "redirect:/";
    }

    // UPDATE 글 수정 /post/{id} -> 글 상세보기 페이지 가기 - 인증 O
    @PutMapping("/s/post/{id}")
    public String update(@PathVariable Integer id) {
        return "redirect:/post/" + id;
    }

    // POST 글 쓰기 /post -> 글 목록으로 가기 - 인증 O
    @PostMapping("/s/post")
    public String post(Post post) {

        // title, content null검사, 공백검사, 길이검사 ,,,,

        if (session.getAttribute("principal") == null) {
            return "redirect:/loginForm";
        }

        // id가 필요한데 오브젝트를 넣어도 될까? 된다 -> 알아서 id(PK)만 뽑아간다
        // 오브젝트 안넣으면 FK연결 안된다
        User principal = (User) session.getAttribute("principal");

        postService.글쓰기(post, principal);

        return "redirect:/"; // 다시 컨트롤러의 메서드를 찾아가는 것
    }
}