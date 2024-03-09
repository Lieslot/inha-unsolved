package com.project.internal.controller;

import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.service.ProblemService;
import com.project.inhaUnsolved.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MainController {


    private final ProblemService problemService;
    private final UserService userService;


    @GetMapping("/home")
    public String home(Model model) {
        Long problemCount = problemService.getSolvedProblemCount();
        Long userCount = userService.getUserCount();
        List<UnsolvedProblem> randomProblems = problemService.findRandomUnsolvedProblems(10);

        model.addAttribute("problemCount", problemCount);
        model.addAttribute("userCount", userCount);
        model.addAttribute("randomProblems", randomProblems);
        return "home";

    }

    @GetMapping("/problems")
    public String problems(Model model,
                           @RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "title", required = false, defaultValue = "") String title) {
        Page<UnsolvedProblem> paging;
        if (title.isEmpty()) {
            paging = problemService.getPageOf(page, 50);
        } else {
            model.addAttribute("title", title);
            paging = problemService.getPageOf(page, 50, title);
        }
        model.addAttribute("paging", paging);

        return "problems";
    }

}
