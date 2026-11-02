package com.restservice;

import com.alibaba.fastjson.JSONObject;
import com.example.entities.Chapter;
import com.example.entities.ChapterUtil;
import com.example.entities.Clazz;
import com.example.entities.User;
import com.example.repos.ChapterRepository;
import com.example.repos.ClazzRepository;
import com.example.repos.UserClassRepository;
import com.example.repos.UserRepository;
import com.example.service.UserService;
import com.example.tool.Course;
import org.springframework.web.bind.annotation.*;
import com.example.tool.CourseRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class GreetingController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final UserClassRepository userClassRepository;
    private final CourseRepository courseRepository;
    private final ClazzRepository clazzRepository;
    private final ChapterRepository chapterRepository;

    public GreetingController(UserRepository userRepository,
                              UserService userService,
                              UserClassRepository userClassRepository,
                              CourseRepository courseRepository,
                              ClazzRepository clazzRepository, ChapterRepository chapterRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.userClassRepository = userClassRepository;
        this.courseRepository = courseRepository;
        this.clazzRepository = clazzRepository;
        this.chapterRepository = chapterRepository;
    }

    @GetMapping("/index")
    public String index() {
        User user = this.userRepository.findByUsername("abcd");
        return user.getNickname();
    }

    @PostMapping("/isUserExist")
    public CommonMessage isUserExist(String username) {
        boolean exists = userRepository.existsByUsername(username);
        if (exists) {
            return new CommonMessage("200", null);
        } else {
            return new CommonMessage("404", null);
        }
    }

    @PostMapping("/register")
    public CommonMessage register(String username, String password, String nickname) {
        if (username == null || username.isEmpty()
                || password == null || password.isEmpty()
                || nickname == null || nickname.isEmpty()) {
            return new CommonMessage("400", "部分数据为空");
        }

        if (userRepository.existsByUsername(username)) {
            return new CommonMessage("200", null);
        }

        userService.register("2", username, password, nickname, "0");
        return new CommonMessage("200", "注册成功");
    }

    @PostMapping("/login")
    public CommonMessage login(String username, String password) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return new CommonMessage("400", "用户名不存在");
        }
        if (!password.equals(user.getPassword())) {
            return new CommonMessage("401", "密码错误");
        }

        JSONObject data = new JSONObject();
        data.put("course", 1);
        data.put("user", user);
        data.put("url", "main");

        return new CommonMessage("200", "", data);
    }

    @GetMapping("/getCourses")
    public CommonMessage getCourses() {
        List<Course> courseList = courseRepository.findByValid("1");
        return new CommonMessage("200", null, courseList);
    }

    @PostMapping("/getUserClasses")
    public CommonMessage getUserClasses(String username) {
        List<Clazz> list = clazzRepository.findClassesByUsername(username);
        return new CommonMessage("200", null, list);
    }

    @PostMapping("/changeNickname")
    public CommonMessage changeNickname(String username, String nickname) {
        if (username == null || username.isEmpty()
                || nickname == null || nickname.isEmpty()) {
            return new CommonMessage("400", "用户名或昵称为空");
        }

        User user = userRepository.findByUsername(username);
        if (user == null) {
            return new CommonMessage("404", "用户不存在");
        }

        int updated = userRepository.updateNicknameByUsername(username, nickname);

        if (updated > 0) {
            return new CommonMessage("200", "昵称更改成功", null);
        } else {
            return new CommonMessage("500", "更新失败", null);
        }
    }

    @PostMapping("/checkLogin")
    public CommonMessage checkLogin(String username, String password) {
        if (username == null || username.isEmpty()
                || password == null || password.isEmpty()) {
            return new CommonMessage("400", "用户名或密码为空");
        }

        User user = userRepository.findByUsername(username);
        if (user == null) {
            return new CommonMessage("400", "用户名不存在");
        }

        if (!password.equals(user.getPassword())) {
            return new CommonMessage("300", null);
        }

        return new CommonMessage("200", null);
    }

    @PostMapping("/changePassword")
    public CommonMessage changePassword(String username, String password) {
        if (username == null || username.isEmpty()
                || password == null || password.isEmpty()) {
            return new CommonMessage("400", "用户名或密码为空");
        }

        User user = userRepository.findByUsername(username);
        if (user == null) {
            return new CommonMessage("404", "用户不存在");
        }

        int updated = userRepository.updatePasswordByUsername(username, password);

        if (updated > 0) {
            return new CommonMessage("200", "密码更改成功", null);
        } else {
            return new CommonMessage("500", "更新失败", null);
        }
    }

    // ========================== ✅ 适配你前端：返回 serial name code score ==========================
    @PostMapping("/chapterStudy")
    public CommonMessage chapterStudy(
            @RequestParam Integer courseId,
            @RequestParam Integer chapterId) {

        List<Chapter> list = new ArrayList<>();

        if (chapterId == -1) {
            list = chapterRepository.findByCourseIdOrderByCode(courseId);
        } else {
            Chapter main = chapterRepository.findById(chapterId).orElse(null);
            if (main == null) return new CommonMessage("404", "章节不存在");
            List<Chapter> children = chapterRepository.findByCourseIdAndParentCode(courseId, main.getCode());
            list.add(main);
            list.addAll(children);
        }

        // ✅ 强制排序（按 code 数字值，确保 1.2 在 1.10 前面）
        list.sort((a, b) -> {
            try {
                return Integer.parseInt(a.getCode()) - Integer.parseInt(b.getCode());
            } catch (NumberFormatException e) {
                return a.getCode().compareTo(b.getCode());
            }
        });

        List<Map> result = new ArrayList<>();
        for (Chapter c : list) {
            Map m = new HashMap();
            m.put("serial", ChapterUtil.getSerial(c.getCode()));
            m.put("name", c.getName());
            m.put("code", c.getCode());
            m.put("score", c.getScore());
            result.add(m);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("chapters", result);
        data.put("chapterStudy", result);

        return new CommonMessage("200", null, data);
    }

    // ========================== ✅ 下拉菜单显示 ==========================
    @PostMapping("/chapters/old")
    public CommonMessage chaptersOld(@RequestParam Integer courseId) {
        // 1. 直接用原生SQL查询，从数据库层面就按code的数字顺序排好，绕过JPA的所有坑
        List<Chapter> all = chapterRepository.findAllByCourseId(courseId);

        // 2. 组装返回
        List<Map<String, Object>> res = new ArrayList<>();
        Map<String, Object> allItem = new HashMap<>();
        allItem.put("id", -1);
        allItem.put("name", "所有章节");
        res.add(allItem);

        for (Chapter c : all) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", c.getId());
            map.put("serial", ChapterUtil.getSerial(c.getCode()));
            map.put("name", c.getName());
            res.add(map);
        }

        return new CommonMessage("200", null, res);
    }
    // ===================== ✅ 重新计算分数：完全适配你前端 =====================
    @PostMapping("/recomputeUserScore")
    public CommonMessage recomputeUserScore(
            @RequestParam Integer courseId,
            @RequestParam String username) {

        List<Chapter> chapters = chapterRepository.findByCourseIdOrderByCode(courseId);
        List<Map> result = new ArrayList<>();

        for(Chapter c : chapters){
            Map<String,Object> map = new HashMap<>();
            map.put("serial", ChapterUtil.getSerial(c.getCode()));
            map.put("name", c.getName());
            map.put("code", c.getCode());
            map.put("score", c.getScore());
            result.add(map);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("chapters", result);
        data.put("chapterStudy", result);

        return new CommonMessage("200","重算完成",data);
    }

    // ===================== ✅ 添加 + 修改 课程（二合一） =====================
    // ===================== ✅ 新增 + 修改 课程（二合一） =====================
    @PostMapping("/addCourse")
    public CommonMessage addCourse(
            @RequestParam(required = false) Integer id,
            @RequestParam String name,
            @RequestParam String intro,
            @RequestParam String bref
    ) {
        try {
            Course course;

            // =============================================
            // 1. 如果传了 id → 修改课程
            // 2. 没传 id → 新增课程
            // =============================================
            if (id != null) {
                course = courseRepository.findById(id).orElse(null);
                if (course == null) {
                    return new CommonMessage("404", "课程不存在");
                }
            } else {
                course = new Course();
                course.setValid("1"); // 新增必须赋值，否则报错
            }

            // =============================================
            // 统一赋值（和你的 Course 完全对应）
            // =============================================
            course.setName(name);
            course.setIntro(intro);
            course.setBref(bref);
            course.setDatetime(java.time.LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            // 保存
            courseRepository.save(course);

            // =============================================
            // 根据是否有 id 返回不同提示
            // =============================================
            return id == null
                    ? new CommonMessage("200", "课程添加成功")
                    : new CommonMessage("200", "课程修改成功");

        } catch (Exception e) {
            e.printStackTrace();
            return new CommonMessage("500", "操作失败");
        }
    }
    // ===================== ✅ 获取所有课程 =====================
    @GetMapping("/getAllCourses")
    public CommonMessage getAllCourses() {
        List<Course> allCourses = courseRepository.findAll();
        return new CommonMessage("200", null, allCourses);
    }

    // ========================== ✅ 顶部菜单【学习】- 左侧章节导航 ==========================
// ========================== ✅ 学习菜单左侧导航（最终完美版） ==========================
// 返回格式完全匹配你给的JSON：一级章节 + subChapters 子章节
    // ========================== ✅ 左侧导航（最终版：层级缩进，和你截图一样） ==========================
    // ========================== ✅ 章节树结构（带 subChapters，完全匹配你的JSON） ==========================
// ========================== ✅ 章节树结构（带 subChapters，实现点击展开） ==========================
    @PostMapping("/chapters")
    public CommonMessage chapters(@RequestParam Integer courseId) {

        // 1. 从数据库取【一级章节】（code 以 0000 结尾）
        List<Chapter> chapterList = chapterRepository.findByCourseIdAndCodeEndingWithOrderByCode(courseId, "0000");

        List<Map<String, Object>> arr = new ArrayList<>();

        for (Chapter cha : chapterList) {
            Map<String, Object> obj = new HashMap<>();

            // 你要的字段全部加上
            obj.put("score", cha.getScore() == null ? 0 : cha.getScore());
            obj.put("code", cha.getCode());
            obj.put("parentCode", cha.getParentCode());
            obj.put("serial", getChapterName(cha.getCode())); // 第一章、第二章
            obj.put("name", cha.getName());
            obj.put("id", cha.getId());

            // 2. 查询子章节（用当前章节的 code 当 parentCode）
            List<Chapter> subChapters = chapterRepository.findByCourseIdAndParentCodeOrderByCode(courseId, cha.getCode());

            if (subChapters != null && !subChapters.isEmpty()) {
                List<Map<String, Object>> subArr = new ArrayList<>();
                for (Chapter c : subChapters) {
                    Map<String, Object> ob = new HashMap<>();
                    ob.put("score", c.getScore() == null ? 0 : c.getScore());
                    ob.put("code", c.getCode());
                    ob.put("parentCode", c.getParentCode());
                    ob.put("serial", chapterSerialNumber(c.getCode())); // 1.1、1.2
                    ob.put("name", c.getName());
                    ob.put("id", c.getId());
                    subArr.add(ob);
                }
                // 放入 subChapters
                obj.put("subChapters", subArr);
            }

            arr.add(obj);
        }

        // 返回
        CommonMessage message = new CommonMessage("200");
        message.setData(arr);
        message.setInfo(String.valueOf(courseId));
        return message;
    }

    // ===================== 你提供的工具类，直接复制 =====================
    public static String chapterSerialNumber(String code) {
        if (code == null || "".equals(code))
            return "";
        String firstLevelCode = code.substring(0, 2);
        String secondLevelCode = code.substring(2, 4);
        String thirdLevelCode = code.substring(4, 6);
        int m1 = Integer.parseInt(firstLevelCode);
        int m2 = Integer.parseInt(secondLevelCode);
        int m3 = Integer.parseInt(thirdLevelCode);
        if (m2 == 0 && m3 == 0)
            return m1 + "";
        if (m3 == 0) {
            return m1 + "." + m2 + "  ";
        }
        return m1 + "." + m2 + "." + m3 + "  ";
    }

    public static String getChapterName(String code) {
        if (code == null || "".equals(code))
            return "";
        String chapterName = chapterSerialNumber(code);
        int m1 = 0;
        String chapter2 = "";
        if (chapterName.indexOf('.') != -1) {
            String chapter1 = chapterName.substring(0, chapterName.indexOf('.'));
            m1 = Integer.parseInt(chapter1);
            int f = chapterName.indexOf('.');
            int s = chapterName.indexOf('.', f + 1);
            if (s == -1)
                chapter2 = chapterName.substring(f + 1);
            else
                chapter2 = chapterName.substring(f + 1, s);
        } else {
            m1 = Integer.parseInt(chapterName);
        }
        String m = "";
        switch (m1) {
            case 1:
                m = "一";
                break;
            case 2:
                m = "二";
                break;
            case 3:
                m = "三";
                break;
            case 4:
                m = "四";
                break;
            case 5:
                m = "五";
                break;
            case 6:
                m = "六";
                break;
            case 7:
                m = "七";
                break;
            case 8:
                m = "八";
                break;
            case 9:
                m = "九";
                break;
            case 10:
                m = "十";
                break;
            case 11:
                m = "十一";
                break;
            case 12:
                m = "十二";
                break;
            case 13:
                m = "十三";
                break;
            case 14:
                m = "十四";
                break;
            case 15:
                m = "十五";
                break;
            case 16:
                m = "十六";
                break;
            case 17:
                m = "十七";
                break;
            case 18:
                m = "十八";
                break;
            case 19:
                m = "十九";
                break;
            case 20:
                m = "二十";
                break;
            default:
                m = "" + m1;
        }
        StringBuilder buffer = new StringBuilder("第");
        buffer.append(m).append("章");
        if (chapter2 != null && !"".equals(chapter2)) {
            buffer = new StringBuilder(chapterName);
        }
        return buffer.toString();
    }
    @GetMapping("/chapterContent")
    public CommonMessage chapterContent(
            @RequestParam("courseId") Integer courseId,
            @RequestParam("cd") String chapterCode) {

        // 1. 根据课程ID和章节code查询章节
        Chapter chapter = chapterRepository.findByCourseIdAndCode(courseId, chapterCode);
        if (chapter == null) {
            return new CommonMessage("404", "章节不存在");
        }

        // 2. 直接返回章节的内容（你可以根据需求自定义返回结构）
        Map<String, Object> data = new HashMap<>();
        data.put("id", chapter.getId());
        data.put("code", chapter.getCode());
        data.put("name", chapter.getName());
        data.put("content", chapter.getContent()); // 假设你的Chapter实体有content字段
        data.put("score", chapter.getScore() == null ? 0 : chapter.getScore());

        return new CommonMessage("200", null, data);
    }
    @PostMapping("/updateCourse")
    public CommonMessage updateCourse(
            @RequestParam("courseId") Integer courseId,
            @RequestParam("code") String chapterCode,
            @RequestParam(value = "username", required = false) String username) {

        // 1. 根据课程ID和章节code查询章节
        Chapter chapter = chapterRepository.findByCourseIdAndCode(courseId, chapterCode);
        if (chapter == null) {
            return new CommonMessage("404", "章节不存在");
        }

        // 2. 这里你可以添加具体的业务逻辑，比如更新学习进度、标记为已读等
        // 示例：更新章节状态（如果你的表中有状态字段）
        // chapter.setStatus("read");
        // chapterRepository.save(chapter);

        // 3. 返回成功
        return new CommonMessage("200", "更新成功");
    }
}