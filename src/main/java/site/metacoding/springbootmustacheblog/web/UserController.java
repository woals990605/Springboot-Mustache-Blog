package site.metacoding.springbootmustacheblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
public class UserController {

    // 회원가입 페이지 (정적) - 인증(로그인) X
    @GetMapping("/joinForm")
    public String joinForm() {
        return "user/joinForm";
    }

    // username=ssar&password=1234&email=ssar@nate.com (x-www-form-urlencoded 타입)
    // 회원가입 INSERT - 인증(로그인) X
    @PostMapping("/join")
    public String join() { // 행위, 페이지 아님
        return "redirect:/loginForm"; // 로그인페이지 이동해주는 컨트롤러 메서드를 재활용
    }

    // 로그인 페이지 (정적) - 인증(로그인) X
    @GetMapping("/loginForm")
    public String loginForm() {
        return "user/loginForm";
    }

    // 로그인 SELECT * FROM user WHERE username=? AND password=? -> 이런 메서드는 없으니까 직접
    // 만들어줘야함
    // 원래 SELECT는 무조건 GET요청
    // 근데 로그인만 예외! POST요청
    // 이유 : 주소에 패스워드를 남길 수 없으니까!! 보안을 위해!!
    // 로그인 - 인증(로그인) X
    @PostMapping("/login")
    public String login() {
        return "redirect:/";
    }

    // 유저 상세 페이지 (동적 -> DB연동 필요) - 인증(로그인) O
    @GetMapping("/user/{id}")
    public String detail(@PathVariable int id) {
        return "user/detail";
    }

    // 유저 수정 페이지 - 인증(로그인) O
    @GetMapping("/user/{id}/updateForm")
    public String updateForm(@PathVariable int id) {
        return "user/updateForm";
    }

    // 유저 수정 - 인증(로그인) O
    @PutMapping("/user/{id}")
    public String update(@PathVariable int id) {
        return "redirect:/user/" + id;
    }

    // 로그아웃 - 인증(로그인) O
    @GetMapping("/logout")
    public String logout() {
        return "메인 페이지를 돌려주면 됨"; // PostController 만들고 수정하자
    }
}