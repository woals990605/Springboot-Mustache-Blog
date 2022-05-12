package site.metacoding.springbootmustacheblog.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseDto<T> {
    private Integer code; // -1 통신 실패 1 통신 성공
    private String msg;
    // 응답의 body 데이터
    private T data; // 데이터 타입이 결정되지 않았다
}