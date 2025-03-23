package CrudPractice.demo.controller;

import CrudPractice.demo.domain.ReportedEntity;
import CrudPractice.demo.domain.UserEntity;
import CrudPractice.demo.service.MemberService2;
import CrudPractice.demo.service.ReviewReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final MemberService2 memberService2;
    private final ReviewReportService reportService;

    public AdminController(MemberService2 memberService2, ReviewReportService reportService) {
        this.memberService2 = memberService2;
        this.reportService = reportService;
    }

    @GetMapping
    public String adminPage(Model model) {

        Long userCount = memberService2.getUserCount();
        UserEntity user = memberService2.getUser();
        Long reportedCount = reportService.getTotal();
        List<ReportedEntity> reportedList = reportService.getRecentReportedList();


        model.addAttribute("admin", user);
        model.addAttribute("totalUsers", userCount);
        model.addAttribute("reportedReviews", reportedCount);
        model.addAttribute("recentReportedReviews", reportedList);

        return "admin";
    }

    @GetMapping("/reported-reviews")
    public String reportedList(Model model) {

        UserEntity user = memberService2.getUser();
        List<ReportedEntity> reportedList = reportService.getReportedList();

        model.addAttribute("admin", user);
        model.addAttribute("reportedReviews", reportedList);

        return "reportList";
    }
}
