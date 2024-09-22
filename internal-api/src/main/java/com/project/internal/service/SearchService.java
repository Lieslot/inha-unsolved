package com.project.internal.service;


import com.project.inhaUnsolved.domain.problem.domain.Problem;
import com.project.inhaUnsolved.domain.problem.domain.QProblem;
import com.project.inhaUnsolved.domain.problem.repository.ProblemRepository;
import com.project.internal.dto.ProblemSearchCriteria;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class SearchService {
    private static final int  DEFAULT_PAGE_SIZE = 50;

    private final ProblemRepository problemRepository;


    public Page<Problem> search(ProblemSearchCriteria criteria) {
        Predicate predicate = buildPredicate(criteria);
        Pageable pageable = buildPageable(criteria);
        return problemRepository.findAll(predicate, pageable);
    }

    private Predicate buildPredicate(ProblemSearchCriteria criteria) {

        BooleanBuilder builder = new BooleanBuilder();

        if (criteria.isNotSolved()) {
            builder.and(QProblem.problem.isSolved.isFalse());
        }

        String keyword = criteria.getKw();
        if (keyword != null && !keyword.trim().isEmpty()) {
            builder.and(buildKeywordCondition(criteria.getSearchBy(), keyword));
        }

        return builder;
    }

    private BooleanExpression buildKeywordCondition(String searchBy, String keyword) {
        switch (searchBy.toLowerCase()) {
            case "name":
                return QProblem.problem.name.containsIgnoreCase(keyword);
            case "id":
                try {
                    int number = Integer.parseInt(keyword);
                    return QProblem.problem.number.eq(number);
                } catch (NumberFormatException e) {
                    return null;
                }
            case "all":
            default:
                try {
                    int number = Integer.parseInt(keyword);
                    return QProblem.problem.name.containsIgnoreCase(keyword)
                            .or(QProblem.problem.number.eq(number));
                } catch (NumberFormatException e) {
                    return QProblem.problem.name.containsIgnoreCase(keyword);
                }
        }
    }

    private Pageable buildPageable(ProblemSearchCriteria criteria) {
        Sort.Direction direction = Sort.Direction.ASC;
        if ("desc".equalsIgnoreCase(criteria.getOrder())) {
            direction = Sort.Direction.DESC;
        }

        Sort sort = Sort.by(direction, "id");
        return PageRequest.of(criteria.getPage(), DEFAULT_PAGE_SIZE, sort);
    }


}
