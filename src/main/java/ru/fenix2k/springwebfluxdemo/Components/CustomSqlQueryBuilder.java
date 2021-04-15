package ru.fenix2k.springwebfluxdemo.Components;

import java.util.ArrayList;
import java.util.List;

public class CustomSqlQueryBuilder {

    private List<String> selectUnit = new ArrayList<>();
    private List<String> fromUnit = new ArrayList<>();
    private List<String> joinUnit = new ArrayList<>();
    private List<String> leftJoinUnit = new ArrayList<>();
    private List<String> rightJoinUnit = new ArrayList<>();
    private List<String> fullJoinUnit = new ArrayList<>();
    private List<String> whereUnit = new ArrayList<>();
    private List<String> groupByUnit = new ArrayList<>();
    private List<String> orderByUnit = new ArrayList<>();
    private int limit = 10;
    private long offset = 0L;


    private CustomSqlQueryBuilder() {
    }

    public static CustomSqlQueryBuilder builder() {
        return new CustomSqlQueryBuilder();
    }

    public CustomSqlQueryBuilder select(String expression) {
        this.selectUnit.add(expression.trim());
        return this;
    }

    public CustomSqlQueryBuilder from(String expression) {
        this.fromUnit.add(expression.trim());
        return this;
    }

    public CustomSqlQueryBuilder join(String expression) {
        this.joinUnit.add(expression.trim());
        return this;
    }

    public CustomSqlQueryBuilder leftJoin(String expression) {
        this.leftJoinUnit.add(expression.trim());
        return this;
    }

    public CustomSqlQueryBuilder rightJoin(String expression) {
        this.rightJoinUnit.add(expression.trim());
        return this;
    }

    public CustomSqlQueryBuilder fullJoin(String expression) {
        this.fullJoinUnit.add(expression.trim());
        return this;
    }

    public CustomSqlQueryBuilder where(String expression) {
        this.whereUnit.add(expression.trim());
        return this;
    }

    public CustomSqlQueryBuilder groupBy(String expression) {
        this.groupByUnit.add(expression.trim());
        return this;
    }

    public CustomSqlQueryBuilder orderBy(String expression) {
        this.orderByUnit.add(expression.trim());
        return this;
    }

    public CustomSqlQueryBuilder limit(int expression) {
        this.limit = expression;
        return this;
    }

    public CustomSqlQueryBuilder offset(long expression) {
        this.offset = expression;
        return this;
    }

    public String getQuery() {
        StringBuilder query = new StringBuilder();
        query.append("SELECT " + selectUnit.stream().reduce((s, s2) -> s + ", " + s2).orElse("*"));

        query.append(" FROM " + fromUnit.stream().reduce((s, s2) -> s + ", " + s2)
                .orElseThrow(() -> {
                    throw new IllegalArgumentException("FROM statement cannot be empty");
                }));

        if (!joinUnit.isEmpty())
            query.append(" JOIN " + joinUnit.stream().reduce((s, s2) -> s + " " + s2).orElse(""));
        if (!leftJoinUnit.isEmpty())
            query.append(" LEFT JOIN " + leftJoinUnit.stream().reduce((s, s2) -> s + " " + s2).orElse(""));
        if (!rightJoinUnit.isEmpty())
            query.append(" RIGHT JOIN " + rightJoinUnit.stream().reduce((s, s2) -> s + " " + s2).orElse(""));
        if (!fullJoinUnit.isEmpty())
            query.append(" FULL JOIN " + fullJoinUnit.stream().reduce((s, s2) -> s + " " + s2).orElse(""));
        if (!whereUnit.isEmpty())
            query.append(" WHERE " + whereUnit.stream().reduce((s, s2) -> s + " AND " + s2).orElse(""));
        if (!groupByUnit.isEmpty())
            query.append(" GROUP BY " + groupByUnit.stream().reduce((s, s2) -> s + ", " + s2).orElse(""));
        if (!orderByUnit.isEmpty())
            query.append(" ORDER BY " + orderByUnit.stream().reduce((s, s2) -> s + ", " + s2).orElse(""));

        query.append(" LIMIT " + limit);
        query.append(" OFFSET " + offset);

        return query.toString();
    }

}
