package com.ssafy.mokkoji.common.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
* Java Meta Annotation
*
* @Target(ElementType.*) - 사용할 어노테이션을 적용할 대상을 지정
* * 안에 여러가지 대상이 들어감
* ANNOTATION_TYPE : 어노테이션
* CONSTRUCTOR : 생성자
* FILED : 필드(멤버 변수, Enum 상수)
* LOCALVARIABLE : 지역 변수
* METHOD : 메서드
* PACKAGE : 패키지
* PARAMETER : 매개변수(파라미터)
* TYPE : 타입(클래스, 인터페이스, Enum)
* TYPE_PARAMETER : 타입 매개변수(ex. 제네릭)
* USE : 타입이 사용되는 모든 대상
*
* @Retention(RetentionPolicy.*) - 특정 어노테이션의 지속 시간 결정(유지 정책)
* * 안에 유지 정책을 결정하여 사용
* SOURCE : 자바 소스파일에 존재, 컴파일 이후 클래스 파일이 되면 사라짐
* CLASS : 클래스 파일까진 존재하나, 실행할 때 사용하지 않음(runtime 직전까지 존재 - defalut)
* RUNTIME : 클래스 파일까지 존재(실행시 사용, 지속 시간이 가장 길다)
*
* @Documented - 어노테이션에 대한 정보가 javadoc으로 작성한 문서에 포함되도록 함
* java에서 지정한 형태의 주석을 인식, html을 통해 api 문서 형태로 만듬
* @Override와 @SuppressWarnings 어노테이션 제외 모든 어노테이션에 적용되있음
*
* @Inherited - 하위 클래스가 어노테이션을 상속하게 함. 즉, 상위 클래스에 붙이면 그 클래스를 상속하는
* 하위 클래스도 상위 클래스에 붙은 어노테이션이 적용됨
*
* @Repeatable - 어노테이션을 반복하여 사용할 수 있도록 허용
* 같은 이름의 어노테이션이 여러번 적용될 수 있다(하나로 묶어주는 어노테이션 별도 지정)
* */

/*
* Swagger annotation
*
* @Tag - api 그룹 설정
*   하위 속성
*   name : 태그의 이름
*   description : 태그에 대한 설명
* 주로 controller class, method 영역에 설정
*
* @Operation - api 동작에 대한 명세를 하기위함
*   하위 속성
*   summary : api에 대한 간략한 설명
*   description : api에 대한 상세 설명
*   response : api Response 리스트
*   parameters : api parameter 리스트
*
* @ApiResponse
*   하위 속성
*   responseCode : http 상태 코드
*   description : response에 대한 설명
*   content : Response payload 구조
*       schema : payload에서 이용하는 Schema
*           hidden : Schema 숨김 여부
*           implementation : Schma 대상 클래스
*
* @ApiResponses - ApiResponse를 묶어서 Annotation으로 적용할 수 있게함
* @ApiResponses(value = {}) 로 구성
* 또는
* @Operation(summary = "", description = "", responses = {})
* responses에 정의하여 사용 가능
*
* @Parameter - 파라미터를 명시
*   하위 속성
*   name : parameter name
*   description : parameter 설명
*   in: parameter 위치
*       ParameterIn.QUERY, ParameterIn.HEADER, ParameterIn.PATH, ParameterIn.COOKIE
*
* @ApiResponse와 마찬가지로 @parameters annotation에 담아 사용하거나
* @Operation annotation의 parameters 요소에 설정 가능하며 (name 필수)
* api method의 인자 값에 붙여 설정 가능하다(name 생략 가능)
*
*
* @Schema - 모델(dto)에 대한 정보를 작성
*   하위 속성
*   description : 설명
*   defalutValue : 기본값
*   allowableValues 허용 가능 값(들어갈 수 있는 값을 리스트 형태로 표시)
* Validation 체크 시 validation 패키지 import, 특정 정규식을 사용 시 @Pattern(regex="") 사용
* */


@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation()
@ApiResponses(value = {
        @ApiResponse(responseCode = "200")
})
public @interface SwaggerSuccess {
    String summary() default "";
    Class<?> implementation();
}
