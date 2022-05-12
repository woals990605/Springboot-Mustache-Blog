package site.metacoding.springbootmustacheblog.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import lombok.RequiredArgsConstructor;
import site.metacoding.springbootmustacheblog.domain.post.Post;
import site.metacoding.springbootmustacheblog.domain.post.PostRepository;
import site.metacoding.springbootmustacheblog.domain.user.User;

@RequiredArgsConstructor
@Controller
public class PostController {

    private final HttpSession session;
    private final PostRepository postRepository;

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
    public String list(Model model, Post post) {

        List<Post> posts = new ArrayList<>();

        // 1. postRepository의 findAll() 호출
        posts = postRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        // 2. model에 담기
        model.addAttribute("posts", posts);
        // 3. mustache 파일에 뿌리기

        return "post/list";
    }

    // 글 상세보기 페이지 /post/{id} (삭제버튼 만들어두면 되니까 삭제페이지 필요 X)
    @GetMapping("/post/{id}") // Get요청에 /post 제외시키기(인증 X)
    public String detail(@PathVariable Integer id, Model model) { // int는 null이 없음, 초기값이 0
        // Integer는 초기값이 null

        // 박스에 담기 null 방지
        Optional<Post> postOp = postRepository.findById(id); // 유저정보

        // 핵심 로직 : 박스가 null인지 확인 => 나중에 try catch로 바꾸기
        if (postOp.isPresent()) { // 박스안에 뭐가 있으면
            Post postEntity = postOp.get();
            model.addAttribute("post", postEntity);
            return "post/detail";
        } else { // 없으면 == isEmpty
            // 누군가 고의로 DELETE 하지 않는 이상 거의 타지 않는 오류
            return "error/page1";
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
        post.setUser(principal);
        // insert into post(title, content, userId)
        // values(입력 받기, 입력 받기, 세션오브젝트의 PK)

        postRepository.save(post);

        return "redirect:/"; // 다시 컨트롤러의 메서드를 찾아가는 것
    }
}