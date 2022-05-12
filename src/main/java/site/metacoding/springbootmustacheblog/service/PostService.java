package site.metacoding.springbootmustacheblog.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.metacoding.springbootmustacheblog.domain.post.Post;
import site.metacoding.springbootmustacheblog.domain.post.PostRepository;
import site.metacoding.springbootmustacheblog.domain.user.User;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    public Page<Post> 글목록보기(Integer page) {
        // 1. postRepository의 findAll() 호출
        // posts = postRepository.findAll(Sort.by(Sort.Direction.DESC, "id")); // sort
        PageRequest pq = PageRequest.of(page, 3); // 페이지대로 3개씩 알아서 넘어가준다
        return postRepository.findAll(pq);
    }

    // 글상세보기, 글수정페이지
    public Post 글상세보기(Integer id) {

        // 박스에 담기 null 방지
        Optional<Post> postOp = postRepository.findById(id); // 유저정보

        // 핵심 로직 : 박스가 null인지 확인 => 나중에 try catch로 바꾸기!
        if (postOp.isPresent()) { // 박스안에 뭐가 있으면
            Post postEntity = postOp.get();
            return postEntity;

        } else { // 없으면 == isEmpty
            // 누군가 고의로 DELETE 하지 않는 이상 거의 타지 않는 오류
            return null;
        }
    }

    @Transactional
    public void 글수정하기(Post post, Integer id) {
        // 영속화
        Optional<Post> postOp = postRepository.findById(id);

        // 변경감지
        if (postOp.isPresent()) {
            Post postEntity = postOp.get();
            postEntity.setTitle(post.getTitle());
            postEntity.setContent(post.getContent());
        }
    } // 더티체킹 완료 (수정됨)

    @Transactional
    public void 글삭제하기(Integer id) {
        postRepository.deleteById(id); // 실패했을 때 내부적으로 exception 터짐

    }

    @Transactional
    public void 글쓰기(Post post, User principal) {

        post.setUser(principal); // User FK 추가
        // insert into post(title, content, userId)
        // values(입력 받기, 입력 받기, 세션오브젝트의 PK)

        postRepository.save(post);
    }
}