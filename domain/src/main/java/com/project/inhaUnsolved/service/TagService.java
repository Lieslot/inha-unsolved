package com.project.inhaUnsolved.service;

import com.project.inhaUnsolved.domain.problem.domain.Tag;
import com.project.inhaUnsolved.domain.problem.repository.TagRepository;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TagService {


    private final TagRepository tagRepository;


    @Transactional(readOnly = true)
    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    public void saveAll(Collection<? extends Tag> tags) {
        tagRepository.saveAll(tags);
    }

}
