package physicks.secondBoard.web.controller.advice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice {

    // TODO : Migration 이후 thymeleaf 3.0 에서는 Servlet 객체를 기본으로 얻지 못한다. 그래서 임시로 이 ControllerAdvice 를 추가했다. 이후 수정 필요.

    /**
     * Model 에 Request URI 를 넣어준다.
     * Request URI 의 사용 용도는 Thymeleaf 에서 현재 메뉴가 어디인지 나타내기 위해 사용한다.
     * (ex. 현재 /board 인 경우 Header Nav Bar 의 Board 버튼에 Active Css 를 넣어줘야함)
     */
    @ModelAttribute("nowUri")
    public String getHeaderNavBarActiveStatus(HttpServletRequest request) {
        log.info("request.getRequestURI() : {}", request.getRequestURI());
        if(request.getRequestURI().equals("/board")) {
            return "/board";
        }
        return "";
    }
}
