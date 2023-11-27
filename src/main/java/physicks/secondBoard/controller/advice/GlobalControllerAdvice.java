package physicks.secondBoard.controller.advice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("nowUri")
    public String getHeaderNavBarActiveStatus(HttpServletRequest request) {
        if(request.getRequestURI().equals("/board")) {
            return "board";
        }
        return "";
    }

}
