package site.metacoding.springbootmustacheblog.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import site.metacoding.springbootmustacheblog.domain.user.User;
import site.metacoding.springbootmustacheblog.service.UserService;
import site.metacoding.springbootmustacheblog.web.dto.ResponseDto;

@RequiredArgsConstructor
@Controller
public class UserController {

    // 컴포지션(의존성 연결) : 컨트롤러는 레파지토리에 의존해야해!
    private final UserService userService;
    private final HttpSession session;

    // DB에 username 확인
    // user의 username이 동일한지 확인해줄래? - 응답은 무조건 json
    // 부득이하게 예외적으로 동사사용할 때도 있음
    // get만으로 동사가 부족하기 때문
    // http://localhost:8080/api/user/username/same-check?username=s
    @GetMapping("/api/user/username/same-check")
    public @ResponseBody ResponseDto<String> sameCheck(String username) {

        String data = userService.유저네임중복검사(username);

        return new ResponseDto<String>(1, "통신성공", data);

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

        // 1번은 필터의 역할

        // 1. username, password, email null 체크 required 속성 걸기(프론트 검사) -> 백엔드 검사
        // username=ssar&email=ssar@nate.com 키 자체가 안들어오는게 null -> password null
        // username=ssar&password=&email=ssar@nate.com 패스워드 null
        // null 체크 -> 공백 체크 (순서 중요)
        if (user.getUsername() == null || user.getPassword() == null || user.getEmail() == null) {
            // 통신하다보면 물리적인 일이 날 수 있음 패킷이 유실되거나
            return "redirect:/joinForm"; // -> 새로고침하면 적은 데이터 다날아감, 뒤로가기 해줘야해(자바스크립트로)
        }

        if (user.getUsername().equals("") || user.getPassword().equals("") || user.getEmail().equals("")) {
            // 통신하다보면 물리적인 일이 날 수 있음 패킷이 유실되거나
            return "redirect:/joinForm";
        }

        // System.out.println("user : " + user);
        userService.회원가입(user);

        return "redirect:/loginForm"; // 로그인페이지 이동해주는 컨트롤러 메서드를 재활용
    }

    // 로그인 페이지 (정적) - 인증(로그인) X
    @GetMapping("/loginForm") // 브라우저가 쿠키를 가지고있으면 자동 전송함, 브라우저만!
    public String loginForm(HttpServletRequest request, Model model) {
        // request.getHeader("Cookie");

        if (request.getCookies() != null) {
            Cookie[] cookies = request.getCookies(); // 파싱해서 배열로 리턴해줌 jSessionId, remember 두개가 있음
            for (Cookie cookie : cookies) {
                System.out.println("쿠키값 : " + cookie.getName());
                if (cookie.getName().equals("remember")) { // getName 키
                    model.addAttribute("remember", cookie.getValue()); // getValue 값
                }
            }
        }
        return "user/loginForm";
    }

    // 로그인 SELECT * FROM user WHERE username=? AND password=? -> 이런 메서드는 없으니까 직접
    // 만들어!
    // 원래 SELECT는 무조건 GET요청
    // 근데 로그인만 예외! POST요청
    // 이유 : 주소에 패스워드를 남길 수 없으니까!! 보안을 위해!!
    // 로그인 - 인증(로그인) X
    @PostMapping("/login")
    public String login(User user, HttpServletResponse response) {
        // HttpSession session = request.getSession(); // 쿠키에 JSESSIONID를 85로 가져오면
        // session의 자기 공간을 가리킴

        User userEntity = userService.로그인(user);

        if (userEntity != null) {
            session.setAttribute("principal", userEntity); // principal 인증된 주체 -> 로그인

            if (user.getRemember() != null && user.getRemember().equals("on")) {
                // F12 Application Cookies 프로토콜이라서 저장한거임!! redirection과 상관 없음 ! 브라우저가 저장시킨다
                response.addHeader("Set-Cookie", "remember=" + userEntity.getUsername()); // 프로토콜에 없는 http 헤더 키값 만들어낸것

                // response.addHeader("Set-Cookie", "hi=hihihihi;"); // 프로토콜에 없는 http 헤더 키값
                // 만들어낸것
                // response.addCookie(cookie); // 프로토콜에 없는 http 헤더 키값 만들어낸것
                // response.setHeader("hello", "안녕");
                // F12 Network Header responseheader에 남는데 얘는 redirect되어서 request 사라짐
            }

            return "redirect:/";

        } else {
            return "redirect:/loginForm";
        }
    }

    // 로그아웃 - 인증(로그인) O
    @GetMapping("/logout")
    public String logout() {
        session.invalidate(); // 해당 JSESSIONID 영역 전체 날리기 -> 이게 로그아웃
        // session.removeAttribute("principal"); // 해당 JSESSIONID 영역의 principal 키값만 날아가는
        // 것
        return "redirect:/loginForm"; // PostController 만들고 수정하자
    }

    // http://localhost:8080/user/1
    // 유저 상세 페이지 (동적 -> DB연동 필요) - 인증(로그인) O
    @GetMapping("/s/user/{id}")
    public String detail(@PathVariable int id, Model model) {

        // 유효성 검사 하기(수십개... 엄청 많겠지?)
        User principal = (User) session.getAttribute("principal");

        // 1. 인증 체크 (로그인하지 않고 주소로 접근 막기)
        if (principal == null) {
            return "error/page1";
        }

        // 2. 권한 체크
        if (principal.getId() != id) {
            return "error/page1";
        }

        User userEntity = userService.유저정보보기(id);

        if (userEntity == null) {
            // 누군가 고의로 DELETE 하지 않는 이상 거의 타지 않는 오류
            return "error/page1";
        } else {
            model.addAttribute("user", userEntity);
            return "user/detail";
        }

        // DB에 로그 남기기 (로그인 한 아이디도 남기기)
        // Heidi SQL에서도 남겨야해 근데 디비는 알아서 로그가 남음
    }

    // 유저 수정 페이지 - 인증(로그인) O
    @GetMapping("/s/user/updateForm")
    public String updateForm() {
        // 세션값을 뿌렸는데, 원래는 DB에서 가져와야 한다.
        return "user/updateForm";
    }

    // username(x), password(o), email(o)
    // password=1234&email=ssar@nate.com (x-www-form-urlencoded)
    // {"password":"1234", "email":"ssar@nate.com"} (application/json)
    // json을 받을 것이기 때문에 Spring이 데이터 받을 때 파싱전략 변경!!
    // Put 요청은 Http Body가 있다. Http Header의 Content-Type에 MIME타입을 알려줘야 한다.

    // @RequestBody -> BufferedReader + JSON 파싱 (자바 오브젝트)
    // @ResponseBody -> BufferedWriter + JSON 파싱 (자바 오브젝트)

    // 유저 수정 - 인증(로그인) O
    @PutMapping("/s/user/{id}")
    public @ResponseBody ResponseDto<String> update(@PathVariable int id, @RequestBody User user) {
        // 보안의 문제
        // 유효성 검사 하기(수십개... 엄청 많겠지?)
        User principal = (User) session.getAttribute("principal");

        // 1. 인증 체크
        if (principal == null) {
            return new ResponseDto<String>(-1, "인증안됨", null);
        }

        // 2. 권한 체크
        if (principal.getId() != id) {
            return new ResponseDto<String>(-1, "권한없음", null);
        }

        User userEntity = userService.유저수정(id, user); // 세션에는 아직도 원래 비밀번호, 이메일로 저장되어있음 -> 변경해주자

        session.setAttribute("principal", userEntity); // 세션 변경 - 덮어쓰기!!

        return new ResponseDto<String>(1, "통신성공", null);
    }

}