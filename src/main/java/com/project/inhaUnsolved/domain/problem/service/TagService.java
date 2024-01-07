package com.project.inhaUnsolved.domain.problem.service;

import com.project.inhaUnsolved.domain.problem.api.TagRequestService;
import com.project.inhaUnsolved.domain.problem.domain.Tag;
import com.project.inhaUnsolved.domain.problem.repository.TagRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TagService {


    private final TagRepository tagRepository;
    private final TagRequestService request;

    public void renewTagDetails() {
        List<Tag> tagDetails = request.getTagDetails();

        tagRepository.saveAll(tagDetails);
    }

}
