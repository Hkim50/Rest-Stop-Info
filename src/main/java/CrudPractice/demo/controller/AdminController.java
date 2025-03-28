package CrudPractice.demo.controller;

import CrudPractice.demo.domain.ReportedEntity;
import CrudPractice.demo.domain.TempStore;
import CrudPractice.demo.domain.UserEntity;
import CrudPractice.demo.service.MemberService2;
import CrudPractice.demo.service.ReviewReportService;
import CrudPractice.demo.service.TemporaryStoreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final MemberService2 memberService2;
    private final ReviewReportService reportService;
    private final TemporaryStoreService temporaryStoreService;

    public AdminController(MemberService2 memberService2, ReviewReportService reportService, TemporaryStoreService temporaryStoreService) {
        this.memberService2 = memberService2;
        this.reportService = reportService;
        this.temporaryStoreService = temporaryStoreService;
    }

    @GetMapping
    public String adminPage(Model model) {

        Long userCount = memberService2.getUserCount();
        UserEntity user = memberService2.getUser();

        List<ReportedEntity> reportedList = reportService.getRecentReportedList();

        model.addAttribute("admin", user);
        model.addAttribute("totalUsers", userCount);
        model.addAttribute("recentReportedReviews", reportedList);

        return "admin";
    }

    @GetMapping("/reported-reviews")
    public String reportedList(Model model) {

        UserEntity user = memberService2.getUser();
        List<ReportedEntity> reportedList = reportService.getRecentReportedList();

        model.addAttribute("admin", user);
        model.addAttribute("reportedReviews", reportedList);

        return "reportList";
    }

    @GetMapping("/users")
    public String userList(Model model) {
        UserEntity user = memberService2.getUser();
        List<UserEntity> allUsers = memberService2.getAllUsers();

        model.addAttribute("admin", user);
        model.addAttribute("users", allUsers);
        return "userList";
    }

    @GetMapping("/store-list")
    public String storeList(Model model) {
        List<TempStore> allTempStore = temporaryStoreService.getAllTempStore();
        UserEntity user = memberService2.getUser();

        model.addAttribute("admin", user);
        model.addAttribute("pendingBusinesses", allTempStore);
        return "tempStoreList";
    }
}
