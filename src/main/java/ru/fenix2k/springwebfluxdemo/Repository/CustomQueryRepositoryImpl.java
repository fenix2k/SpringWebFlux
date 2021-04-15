package ru.fenix2k.springwebfluxdemo.Repository;

import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.fenix2k.springwebfluxdemo.Components.CustomCriteriaBuilder;
import ru.fenix2k.springwebfluxdemo.Components.CustomSqlQueryBuilder;

import java.text.ParseException;
import java.util.function.BiFunction;

/**
 * Allows to perform simple search queries
 */
@Repository
public class CustomQueryRepositoryImpl implements CustomQueryRepository {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private CustomCriteriaBuilder criteriaBuilder;


    public <T> Flux<T> execute(CustomSqlQueryBuilder query, BiFunction<Row, RowMetadata, T> MAPPING_FUNCTION) {
        return this.execute(query, null, MAPPING_FUNCTION, null);
    }

    public <T> Flux<T> execute(CustomSqlQueryBuilder query, BiFunction<Row, RowMetadata, T> MAPPING_FUNCTION, Pageable pageable) {
        return this.execute(query, null, MAPPING_FUNCTION, pageable);
    }

    public <T> Flux<T> execute(CustomSqlQueryBuilder query, String criteriaString, BiFunction<Row, RowMetadata, T> MAPPING_FUNCTION, Pageable pageable) {

        if (query == null || MAPPING_FUNCTION == null) return Flux.just();

        try {
            if (criteriaString != null && !criteriaString.isEmpty()) {
                Criteria criteria = criteriaBuilder.parseAndBuild(criteriaString);
                query.where(criteria.toString());
            }

            R2dbcEntityTemplate dbcEntityTemplate = new R2dbcEntityTemplate(connectionFactory);

            if(pageable != null) {
                String sort = pageable.getSort().toString().replace(": ", " ");
                if(!sort.equals("UNSORTED"))
                    query.orderBy(sort);

                query.limit(pageable.getPageSize());
                query.offset(pageable.getOffset());
            }
            else {
                query.limit(20);
            }

            return dbcEntityTemplate.getDatabaseClient()
                    .sql(query.getQuery())
                    .map(MAPPING_FUNCTION)
                    .all();

        } catch (ParseException ex) {
            return Flux.error(new ParseException(ex.getMessage(), ex.getErrorOffset()));
        } catch (ConversionFailedException ex) {
            return Flux.error(new ParseException(ex.getMessage(), 0));
        } catch (Exception ex) {
            return Flux.error(new ParseException("Parsing query error: cannot parse query string", 0));
        }

    }

}
