package site.metacoding.springbootmustacheblog.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import site.metacoding.springbootmustacheblog.domain.user.User;
import site.metacoding.springbootmustacheblog.domain.user.UserRepository;

@Controller
public class UserController {

    // 컴포지션(의존성 연결) : 컨트롤러는 레파지토리에 의존해야해!
    private UserRepository userRepository;

    // DI 받는 코드!!
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 회원가입 페이지 (정적) - 인증(로그인) X
    @GetMapping("/joinForm")
    public String joinForm() {
        return "user/joinForm";
    }

    // username=ssar&password=1234&email=ssar@nate.com (x-www-form-urlencoded 타입)
    // 회원가입 INSERT - 인증(로그인) X
    @PostMapping("/join")
    public String join(User user) { // 행위, 페이지 아님
        System.out.println("user : " + user);
        User userEntity = userRepository.save(user);
        System.out.println("userEntity : " + userEntity);
        return "redirect:/loginForm"; // 로그인페이지 이동해주는 컨트롤러 메서드를 재활용
    }

    // 로그인 페이지 (정적) - 인증(로그인) X
    @GetMapping("/loginForm")
    public String loginForm() {
        return "user/loginForm";
    }

    // 로그인 SELECT * FROM user WHERE username=? AND password=? -> 이런 메서드는 없으니까 직접
    // 만들어!
    // 원래 SELECT는 무조건 GET요청
    // 근데 로그인만 예외! POST요청
    // 이유 : 주소에 패스워드를 남길 수 없으니까!! 보안을 위해!!
    // 로그인 - 인증(로그인) X
    @PostMapping("/login")
    public String login(HttpServletRequest request, User user) {
        HttpSession session = request.getSession(); // 쿠키에 JSESSIONID를 85로 가져오면 session의 자기 공간을 가리킴

        // 1. DB연결해서 username, password 있는지 확인
        User userEntity = userRepository.mLogin(user.getUsername(), user.getPassword());

        // 2. 있으면 session 영역에 인증됨이라고 메시지 하나 넣어두자
        if (userEntity == null) {
            System.out.println("아이디 혹은 패스워드가 틀렸습니다.");
        } else {
            System.out.println("로그인 되었습니다.");
            // 세션에 옮겨담자, request는 사라졌지만 세션영역에 보관
            session.setAttribute("principal", userEntity); // principal 인증된 주체 -> 로그인
        }
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