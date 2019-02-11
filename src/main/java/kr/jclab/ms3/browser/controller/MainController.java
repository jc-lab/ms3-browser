package kr.jclab.ms3.browser.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/")
public class MainController {
    @RequestMapping(path = "/")
    public ModelAndView main() {
        ModelAndView mav = new ModelAndView("/view/index.html");
        return mav;
    }
}
