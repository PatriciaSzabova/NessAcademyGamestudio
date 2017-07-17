package sk.tuke.gamestudio.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//http://localhost:8080
@Controller
public class IndexController {

    @RequestMapping({"", "/index"})
    public String index() {
        return "index";
    }
}
