package com.project.internal.controller;

import com.project.inhaUnsolved.domain.problem.domain.Problem;
import com.project.inhaUnsolved.service.ProblemService;
import com.project.inhaUnsolved.service.UserService;
import java.util.List;

import com.project.internal.dto.ProblemSearchCriteria;
import com.project.internal.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {


    private final ProblemService problemService;
    private final UserService userService;
    private final SearchService searchService;


    @GetMapping("/home")
    public String home(Model model) {
        Integer solvedCount = problemService.getSolvedProblemCount();
        Long userCount = userService.getUserCount();
        List<Problem> randomProblems = problemService.findDailyRandomProblems();

        model.addAttribute("solvedCount", solvedCount);
        model.addAttribute("userCount", userCount);
        model.addAttribute("randomProblems", randomProblems);
        return "home";

    }

    @GetMapping("/problems")
    public String problems(ProblemSearchCriteria criteria, Model model) {
        Page<Problem> paging;

        // 문제 검색을 위한 조건을 처리합니다.
        if (!criteria.getKw().isEmpty()|| criteria.isNotSolved()) {
            paging = searchService.search(criteria);
        } else {
            paging = problemService.getPageOf(criteria.getPage(), 50);
        }
        model.addAttribute("problems", paging.getContent());
        model.addAttribute("page", paging.getNumber());
        model.addAttribute("totalPage", paging.getTotalPages());
        model.addAttribute("criteria", criteria);


        return "problems";
    }

}
