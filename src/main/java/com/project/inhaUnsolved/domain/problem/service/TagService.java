package com.project.inhaUnsolved.domain.problem.service;

import com.project.inhaUnsolved.domain.problem.domain.Tag;
import com.project.inhaUnsolved.domain.problem.dto.TagDetail;
import com.project.inhaUnsolved.domain.problem.repository.TagRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TagService {


    private final TagRepository tagRepository;
    private final TagRequestService request;

    public void addTags() {
        List<Tag> tagDetails = request.getTagDetails();

        List<Tag> newTags = tagDetails.stream()
                                   .filter(tag -> !tagRepository.existsByNumber(tag.getNumber()))
                                   .toList();

        tagRepository.saveAll(newTags);

    }


}
