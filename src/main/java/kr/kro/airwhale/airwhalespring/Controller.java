package kr.kro.airwhale.airwhalespring;

// Spring Import
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class Controller {
    public static boolean checkIsValid(Map<String, Object> post) {
        if (post.get("ctpd") == null) return false;
        return post.get("ctpd").equals("AiRwHaLeBaCkEnDsPrInG");
    }

    @RequestMapping("/login")
    public Object login(@RequestParam Map<String, Object> post) {
        if (!checkIsValid(post)) return null;
        try {
            String id = post.get("id").toString();
            String pwd = post.get("pwd").toString();

            String currentPath = System.getProperty("user.dir");
            File dbf = new File(currentPath + File.separator + "database.db");

            if (!dbf.exists()) {
                if (!dbf.createNewFile()) throw new Exception();
                FileWriter writer = new FileWriter(dbf);
                writer.write("{}");
                writer.flush();
                writer.close();
            }

            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader(dbf);

            JSONObject db = (JSONObject) parser.parse(reader);
            JSONObject user = (JSONObject) db.get(id);

            if (user == null) return false;
            return user.get("password").equals(pwd);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
