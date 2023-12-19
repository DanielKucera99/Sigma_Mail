package cz.uhk.sigmamail;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SigmaMailController {

    @RequestMapping("/")
    public String index(){
        return "index";
    }
}
