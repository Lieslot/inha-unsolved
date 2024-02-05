package com.project.inhaUnsolved.scheduler.dto;

import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewUnsolvedProblems implements Iterable<UnsolvedProblem> {

    private List<UnsolvedProblem> unsolvedProblems;

    public NewUnsolvedProblems(List<UnsolvedProblem> unsolvedProblems) {
        this.unsolvedProblems = unsolvedProblems;
    }


    @Override
    public Iterator<UnsolvedProblem> iterator() {
        return unsolvedProblems.iterator();
    }

    @Override
    public void forEach(Consumer<? super UnsolvedProblem> action) {
        unsolvedProblems.forEach(action);
    }

    @Override
    public Spliterator<UnsolvedProblem> spliterator() {
        return unsolvedProblems.spliterator();
    }
}
