package com.project.inhaUnsolved.domain.problem.service;

import com.project.inhaUnsolved.domain.problem.api.TagRequest;
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
    private final TagRequest request;

    public void renewTagDetails() {
        List<Tag> tagDetails = request.getTagDetails();

        List<Tag> tags = tagRepository.findAll();

        for (Tag tag : tags) {
            int number = tag.getNumber();
            String name = tag.getName();
        }


    }


    @Transactional(readOnly = true)
    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    public void saveAll(Collection<? extends Tag> tags) {
        tagRepository.saveAll(tags);
    }

}
