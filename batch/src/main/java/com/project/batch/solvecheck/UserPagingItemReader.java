package com.project.batch.solvecheck;

import com.project.inhaUnsolved.domain.user.User;

import java.util.List;
import java.util.Stack;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

public class UserPagingItemReader extends AbstractPagingItemReader<User> {

    private NewSolvedProblemService newSolvedProblemService;
    private final Stack<User> userBuffer = new Stack<>();

    public UserPagingItemReader() {
        setName(ClassUtils.getShortName(UserPagingItemReader.class));
    }

    public UserPagingItemReader(int chunkSize, NewSolvedProblemService newSolvedProblemService) {
        setPageSize(chunkSize);
        this.newSolvedProblemService = newSolvedProblemService;

    }

    @Override
    protected void doOpen() throws Exception {
        super.doOpen();
        List<User> users = newSolvedProblemService.requestAndFilterRenewedUsers();

        for (User user : users) {
            userBuffer.push(user);
        }

    }

    @Override
    protected void doReadPage() {

        if (CollectionUtils.isEmpty(results)) {
            results = new CopyOnWriteArrayList<>();
        } else {
            results.clear();
        }

        int currentPageSize = Math.min(getPageSize(), userBuffer.size());
        for (int i = 0; i < currentPageSize; i++) {
            results.add(userBuffer.pop());
        }
    }


    @Override
    protected void doClose() throws Exception {
        super.doClose();
    }

}
