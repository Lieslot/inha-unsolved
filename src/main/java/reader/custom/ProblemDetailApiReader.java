package reader.custom;

import com.project.inhaUnsolved.domain.problem.api.ProblemRequestByNumber;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.dto.ProblemDetailCompare;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import reader.QuerydslNoOffsetPagingItemReader;
import reader.QuerydslPagingItemReader;
import reader.options.QuerydslNoOffsetOptions;

public class ProblemDetailApiReader<T> extends QuerydslPagingItemReader<UnsolvedProblem> {
    private QuerydslNoOffsetOptions<UnsolvedProblem> options;

    private final ProblemRequestByNumber request = new ProblemRequestByNumber(new RestTemplateBuilder());

    private ProblemDetailApiReader() {
        super();
        setName(ClassUtils.getShortName(QuerydslNoOffsetPagingItemReader.class));
    }

    public ProblemDetailApiReader(EntityManagerFactory entityManagerFactory,
                                  int pageSize,
                                  QuerydslNoOffsetOptions<UnsolvedProblem> options,
                                  Function<JPAQueryFactory, JPAQuery<UnsolvedProblem>> queryFunction) {
        super(entityManagerFactory, pageSize, queryFunction);
        setName(ClassUtils.getShortName(QuerydslNoOffsetPagingItemReader.class));
        this.options = options;
    }
    @Override
    @SuppressWarnings("unchecked")
    protected void doReadPage() {

        EntityTransaction tx = getTxOrNull();

        JPQLQuery<UnsolvedProblem> query = createQuery().limit(getPageSize());

        initResults();

        List<UnsolvedProblem> items = fetchItems(query, tx);


        resetCurrentIdIfNotLastPage();
    }

    @Override
    protected JPAQuery<UnsolvedProblem> createQuery() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        JPAQuery<UnsolvedProblem> query = queryFunction.apply(queryFactory);
        options.initKeys(query, getPage()); // 제일 첫번째 페이징시 시작해야할 ID 찾기

        return options.createQuery(query, getPage());
    }

    private List<UnsolvedProblem> fetchItems(JPQLQuery<UnsolvedProblem> query, EntityTransaction tx) {

        List<UnsolvedProblem> items = new ArrayList<>();

        if (transacted) {
            items.addAll(query.fetch());
            if(tx != null) {
                tx.commit();
            }
        } else {
            List<UnsolvedProblem> queryResult = query.fetch();
            for (UnsolvedProblem entity : queryResult) {
                entityManager.detach(entity);
                items.add(entity);
            }
        }

        return items;
    }


    private void resetCurrentIdIfNotLastPage() {
        if (isNotEmptyResults()) {
            options.resetCurrentId(getLastItem());
        }
    }

    // 조회결과가 Empty이면 results에 null이 담긴다
    private boolean isNotEmptyResults() {
        return !CollectionUtils.isEmpty(results) && results.get(0) != null;
    }

    private UnsolvedProblem getLastItem() {
        return results.get(results.size() - 1);
    }

}
