package com.project.inhaUnsolved.scheduler.tag;

import com.project.inhaUnsolved.domain.problem.api.TagRequest;
import com.project.inhaUnsolved.domain.problem.domain.Tag;
import com.project.inhaUnsolved.domain.problem.service.TagService;
import jakarta.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class NewTagDetailWriter implements ItemWriter<Tag> {

    private final TagRequest tagRequest;
    private final TagService tagService;
    private Map<Integer, Tag> newTagDetails;

    public NewTagDetailWriter(TagRequest tagRequest, TagService tagService) {

        this.tagRequest = tagRequest;
        this.tagService = tagService;
        newTagDetails = new HashMap<>();
    }

    @PostConstruct
    void getNewTagDetails() {
        newTagDetails = tagRequest.getTagDetails()
                                  .stream()
                                  .collect(Collectors.toMap(Tag::getNumber, Function.identity()));
    }

    @Override
    public void write(Chunk<? extends Tag> chunk) throws Exception {

        List<? extends Tag> existingTags = chunk.getItems();

        existingTags.forEach(this::renewTagDetail);
        tagService.saveAll(existingTags);
        addNewTags();

    }

    private void renewTagDetail(Tag existingTag) {

        Tag newTagDetail = newTagDetails.get(existingTag.getNumber());
        existingTag.renewName(newTagDetail.getName());
        newTagDetails.remove(existingTag.getNumber()); // 이미 존재하는 Tag 삭제
    }

    private void addNewTags() {
    }


}
