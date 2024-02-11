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
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class Controller {
    public static JSONParser parser = new JSONParser();
    public static String currentPath = System.getProperty("user.dir");
    public static File dbf = new File(currentPath + File.separator + "database.db");
    public static boolean checkIsInvalid(Map<String, Object> post) {
        if (post.get("ctpd") == null) return true;
        return !post.get("ctpd").equals("AiRwHaLeBaCkEnDsPrInG");
    }

    @RequestMapping("/login")
    public Object login(@RequestParam Map<String, Object> post) {
        if (checkIsInvalid(post)) return "KEY is invaild";
        try {
            String id = post.get("id").toString();
            String pwd = post.get("pwd").toString();

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
            return "ERROR";
        }
    }

    @RequestMapping("/signup")
    public Object signup(@RequestParam Map<String, Object> post) {
        HashMap<String, String> status = new HashMap<>();

        if (checkIsInvalid(post)) return "KEY is envalid";
        try {
            String id = post.get("id").toString();
            String pwd = post.get("pwd").toString();
            String email = post.get("email").toString();
            String name = post.get("name").toString();

            if (!dbf.exists()) {
                if (!dbf.createNewFile()) throw new Exception();
                FileWriter writer = new FileWriter(dbf);
                writer.write("{}");
                writer.flush();
                writer.close();
            }

            JSONObject old = (JSONObject) parser.parse(new FileReader(dbf));

            for (Object key : old.keySet()) {
                if (key == id) {
                    status.put("status", "idexist");
                    return new JSONObject(status);
                }
            }

            HashMap<String, Object> newUser = new HashMap<>();
            newUser.put("id", id);
            newUser.put("pwd", pwd);
            newUser.put("name", name);
            newUser.put("email", email);

            HashMap<Object, Object> newMap = new HashMap<>();
            for (Object key : old.keySet()) newMap.put(key, old.get(key));
            newMap.put(id, newUser);

            JSONObject newObj = new JSONObject(newMap);
            FileWriter writer = new FileWriter(dbf);
            writer.write(newObj.toJSONString());
            writer.flush();
            writer.close();

            status.put("status", "success");
            return new JSONObject(status);
        } catch (Exception e) {
            e.printStackTrace();
            status.put("status", "fail");
            return new JSONObject(status);
        }
    }
}
