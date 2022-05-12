package site.metacoding.springbootmustacheblog.domain.user;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 테이블을 자바세상에 옮기는게 모델링
// JPA 라이브러리는 Java Persistence(영구적인 저장) API(노출되어 있는 메서드)
// 1. CRUD 메서드 기본 제공
// 2. 자바코드로 DB를 자동 생성 기능 제공 -> 지금 배워보자! -> 설정 필요!
// 3. ORM 제공 - 이 부분은 지금 몰라도 됨

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity // 서버 실행 시 해당 클래스로 테이블을 생성해!!
@EntityListeners(AuditingEntityListener.class) // 현재시간 입력을 위해 필요한 어노테이션
public class User {
    // IDENTITY 전략은 DB에게 번호증가 전략을 위임하는 것 -> 알아서 디비에 맞게 찾아줌
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // PK

    @Column(length = 20, unique = true) // VARCHAR(20) 길이설정
    private String username; // ssar 아이디

    @Column(length = 12, nullable = false)
    private String password;

    // varchar의 크기를 벗어나는 length를 넣으면 알아서 longtext타입으로 바뀐다!
    @Column(length = 16000000, nullable = false)
    private String email;

    // 시간 -> DB는 LocalDateTime 타입이 없으니까 알아서 datetime 타입으로 바뀜!
    // 자바에선 커멜표기법인데 디비는 언더바가 디폴트임
    // 내가 설정한 이름 그대로 설정되게 하는 기능이 있음
    @CreatedDate // insert 때 동작
    private LocalDateTime createDate; // 회원가입 날짜
    @LastModifiedDate // insert, update 때 동작
    private LocalDateTime updateDate; // 업데이트 된 날짜

    //////////////////////////////////////////////////////////////////// DB 테이블과
    //////////////////////////////////////////////////////////////////// 상관없음
    @Transient // 데이터베이스에 컬럼 만들지마!
    private String remember; // 이렇게 하면 테이블만들어짐, 필요없는데
}