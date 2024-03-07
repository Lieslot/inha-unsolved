package com.project.internal.controller;

import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.service.ProblemService;
import com.project.inhaUnsolved.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {


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


}
