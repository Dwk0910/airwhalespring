package kr.kro.airwhale.airwhalespring;

// Spring Import
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class Controller {
    @RequestMapping("/login")
    public Object login(@RequestParam Map<String, Object> post) {
        String currentPath = System.getProperty("user.dir");
        File db = new File(currentPath + File.separator + "database.db");
        return true;
    }
}
