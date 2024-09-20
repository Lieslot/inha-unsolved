package com.project.batch.dto;

import com.project.inhaUnsolved.domain.problem.domain.Problem;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewUnsolvedProblems implements Iterable<Problem> {

    private List<Problem> problems;

    public NewUnsolvedProblems(List<Problem> problems) {
        this.problems = problems;
    }


    @Override
    public Iterator<Problem> iterator() {
        return problems.iterator();
    }

    @Override
    public void forEach(Consumer<? super Problem> action) {
        problems.forEach(action);
    }

    @Override
    public Spliterator<Problem> spliterator() {
        return problems.spliterator();
    }
}
