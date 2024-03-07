package com.project.batch.reader;

import static org.mockito.Mockito.when;

import com.project.batch.solvecheck.NewSolvedProblemReader;
import com.project.batch.solvecheck.NewSolvedProblemService;
import com.project.batch.dto.ProblemAndUser;
import com.project.inhaUnsolved.domain.problem.domain.Tier;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.user.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
public class NewSolvedProblemReaderTest {

    @MockBean
    private NewSolvedProblemService newSolvedProblemService;

    @Test
    void reader에서_새롭게_해결한_문제가_writer에_전달되는지_테스트() throws Exception {

        User testUser = new User("test", 100);

        List<Integer> storedSolvedProblemNumbers = new ArrayList<>();
        List<UnsolvedProblem> newSolvedProblems = new ArrayList<>();

        for (int i = 1000; i < 1100; i++) {
            storedSolvedProblemNumbers.add(i);
        }
        for (int i = 1000; i < 1400; i++) {
            newSolvedProblems.add(UnsolvedProblem.builder()
                                                 .number(i)
                                                 .tags(new HashSet<>())
                                                 .name(String.valueOf(i))
                                                 .tier(Tier.BRONZE_IV)
                                                 .build());
        }

        when(newSolvedProblemService.requestAndFilterRenewedUsers()).thenReturn(List.of(testUser));
        when(newSolvedProblemService.getProblemsSolvedBy(testUser)).thenReturn(newSolvedProblems);
        when(newSolvedProblemService.findAllSolvedProblemNumber()).thenReturn(storedSolvedProblemNumbers);

        NewSolvedProblemReader reader = new NewSolvedProblemReader(newSolvedProblemService);
        reader.init();

        ProblemAndUser result = reader.read();
        newSolvedProblems.removeIf(problem -> storedSolvedProblemNumbers.contains(problem.getNumber()));

        List<UnsolvedProblem> expectedProblems = new ArrayList<>(newSolvedProblems);

        List<UnsolvedProblem> unsolvedProblems = result.getUnsolvedProblems();

        expectedProblems.forEach(problem ->
                Assertions.assertThat(unsolvedProblems)
                          .anyMatch(
                                  unsolvedProblem -> problem.hasEqual(unsolvedProblem.getNumber()))
        );
        Assertions.assertThat(expectedProblems.size())
                  .isEqualTo(300);


    }


}
