package physicks.secondBoard.domain.hello;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class HelloController {

    @GetMapping("/hello")
    public String helloMain(Model model) {
        log.info("ENTER helloController");

        Hello hello = new Hello();
        hello.setName("Hello World!!!!");

        model.addAttribute("hello", hello);

        return "hello";
    }
}
