package com.example.Animalgram.common.exception;


import com.example.Animalgram.common.error.ErrorCodeIfs;

// 이 인터페이스는 @Getter로 대체가능하다
// 하지만 이렇게 구현해서 사용하는이유는 다형성 때문에 그런다
// 이거 구현한 클래스에서 여기 구현한 메서드 들을 호출해서 그 클래스에서 해당하는 값들을 호출할수 있다
// 이거 구현안할경우는 하나의 errorcode만 구현할때이다 그때는 그냥 그 클래스에서  @getter만 선언하면된다
public interface ApiExceptionIfs {

    ErrorCodeIfs getErrorCodeIfs();
    String getErrorDescription();
}