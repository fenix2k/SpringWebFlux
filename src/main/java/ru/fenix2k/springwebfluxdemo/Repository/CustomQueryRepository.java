package ru.fenix2k.springwebfluxdemo.Repository;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import ru.fenix2k.springwebfluxdemo.Components.CustomSqlQueryBuilder;

import java.util.function.BiFunction;

public interface CustomQueryRepository {

    <T> Flux<T> execute(CustomSqlQueryBuilder query, BiFunction<Row, RowMetadata, T> MAPPING_FUNCTION, Pageable pageable);
    <T> Flux<T> execute(CustomSqlQueryBuilder query, String criteriaString, BiFunction<Row, RowMetadata, T> MAPPING_FUNCTION, Pageable pageable);

}
