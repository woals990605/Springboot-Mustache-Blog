package site.metacoding.springbootmustacheblog.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository // 사실 안붙여도 됨. 부모 클래스에 정의되어 있기 때문.
public interface UserRepository extends JpaRepository<User, Integer> {

    // 없는 메서드는 직접 만들기(복잡한 쿼리)
    // 물음표가 사라졌음 - 물음표 순서를 지켜야했었는데 대신에 키 바인딩 방법이 있음
    // 바인딩하기 위한 어노테이션 @Param("키")
    @Query(value = "SELECT * FROM user WHERE username = :username AND password = :password", nativeQuery = true)
    User mLogin(@Param("username") String username, @Param("password") String password);

    @Query(value = "SELECT * FROM user WHERE username = :username", nativeQuery = true)
    User mUsernameSameCheck(@Param("username") String username);

    // findAll()
    // SELECT * FROM user;

    // findById()
    // SELECT * FROM user WHERE id = ?

    // save()
    // INSERT INTO user(username, password, email, createDate) VALUES(?, ?, ? , ?)

    // deleteById()
    // DELETE FROM user WHERE id = ?

    // update는 없어요!! - 영속성 컨텍스트 공부하면 사용할 수 있음
}
