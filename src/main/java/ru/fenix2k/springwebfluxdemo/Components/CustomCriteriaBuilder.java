package ru.fenix2k.springwebfluxdemo.Components;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CustomCriteriaBuilder {

    public Criteria parseAndBuild(String queryString) throws ParseException {
        Criteria criteria;

        try {
            criteria = recursiveParser(queryString);
            if (criteria == null)
                return null;

        } catch (ParseException ex) {
            throw new ParseException(ex.getMessage(), ex.getErrorOffset());
        } catch (Exception ex) {
            throw new ParseException("Parsing query error: cannot parse query string", 0);
        }

        return criteria;
    }

    /**
     * Parse a queryString into instance of Criteria class
     *
     * @param queryString query string. Type String.
     * @return instance of Criteria class
     * @throws ParseException parsing error
     */
    private Criteria recursiveParser(String queryString) throws ParseException {

        String query = trimByValue(queryString, "(", ")");
        Criteria criteria = Criteria.empty();

        int parenthesis = 0;
        int doubleQuote = 0;
        int singleQuote = 0;

        for (int i = 0; i < query.length(); i++) {
            if (query.charAt(i) == '(') parenthesis++;
            else if (query.charAt(i) == ')') parenthesis--;

            if (query.charAt(i) == '"' && doubleQuote == 0) doubleQuote++;
            else if (query.charAt(i) == '"' && doubleQuote > 0) doubleQuote--;

            if (query.charAt(i) == '\'' && singleQuote == 0) singleQuote++;
            else if (query.charAt(i) == '\'' && singleQuote > 0) singleQuote--;


            if (parenthesis != 0 && parenthesis != 1
                    || doubleQuote != 0 && doubleQuote != 1
                    || singleQuote != 0 && singleQuote != 1)
                throw new ParseException("Parsing query error: missing brackets", i);

            if (parenthesis == 0 && doubleQuote == 0 && singleQuote == 0) {
                String operator = getOperatorByPosition(query, i);
                if (operator.isEmpty()) continue;

                String[] queryParts = query.split(operator, 2);
                List<Criteria> criteriaList = new ArrayList<>();

                for (String part : queryParts) {
                    if (part.contains("AND") || part.contains("OR"))
                        criteriaList.add(recursiveParser(part));
                    else {
                        criteriaList.add(getCriteriaFromPart(part));
                    }
                }

                if (operator.equals("AND"))
                    criteria = criteriaList.stream().reduce(Criteria::and).get();
                else if (operator.equals("OR"))
                    criteria = criteriaList.stream().reduce(Criteria::or).get();

                return criteria;
            }
        }

        return getCriteriaFromPart(query);
    }

    /**
     * Trim string by specified string
     *
     * @param s     target string
     * @param begin string that must be removed
     * @return result string
     * @throws ParseException
     */
    private String trimByValue(String s, String begin) throws ParseException {
        return trimByValue(s, begin, begin);
    }

    /**
     * Trim string by specified string
     *
     * @param s
     * @param begin string that must be removed at begin of string
     * @param end   string that must be removed at end of string
     * @return result string
     * @throws ParseException
     */
    private String trimByValue(String s, String begin, String end)
            throws ParseException {
        String str = s.trim();
        StringBuilder sb = new StringBuilder(str);

        int len = sb.length();
        int firstValueIndex = sb.indexOf(begin);
        int lastValueIndex = sb.lastIndexOf(end);

        if (firstValueIndex == -1 && lastValueIndex == -1)
            return sb.toString().trim();

        if (firstValueIndex != 0
                || lastValueIndex != len - begin.length())
            throw new ParseException("Parsing query error: missing closing " + end, 0);

        sb.delete(lastValueIndex, sb.length());
        sb.delete(0, begin.length());

        return sb.toString().trim();

    }

    /**
     * Determines whether the AND OR OR operator is further defined at the current position
     *
     * @param s        source string
     * @param position current position of string array
     * @return AND, OR, ""
     */
    private String getOperatorByPosition(String s, int position) {
        int len = s.length();
        if (s.charAt(position) == 'A' && (len > position + 2)
                && s.charAt(position + 1) == 'N' && s.charAt(position + 2) == 'D')
            return "AND";

        if (s.charAt(position) == 'O' && (len > position + 1)
                && s.charAt(position + 1) == 'R')
            return "OR";

        return "";
    }

    /**
     * Creates a query criteria from source query string
     *
     * @param part source string
     * @return instance of Criteria class
     * @throws ParseException
     */
    private Criteria getCriteriaFromPart(String part) throws ParseException {
        String operator = "";
        if (part.contains("!=")) operator = "!=";
        else if (part.contains("=")) operator = "=";
        else if (part.contains("!~")) operator = "!~";
        else if (part.contains("~")) operator = "~";
        else if (part.contains(">")) operator = ">";
        else if (part.contains(">=")) operator = ">=";
        else if (part.contains("<")) operator = "<";
        else if (part.contains("<=")) operator = "<=";

        String[] pair = part.split(operator);
        if (pair[0].isEmpty())
            throw new ParseException("Parsing query error: key cannot be empty", 0);

        String key = pair[0];
        String value = trimByValue(trimByValue(pair[1], "\""), "'");
        Criteria criteria = Criteria.empty();

        switch (operator) {
            case "!=":
                criteria = Criteria.where(key).not(value).ignoreCase(true);
                break;
            case "=":
                criteria = Criteria.where(key).is(value).ignoreCase(true);
                break;
            case "!~":
                criteria = Criteria.where(key).notLike("%" + value + "%").ignoreCase(true);
                break;
            case "~":
                criteria = Criteria.where(key).like("%" + value + "%").ignoreCase(true);
                break;
            case ">":
                criteria = Criteria.where(key).greaterThan(value);
                break;
            case ">=":
                criteria = Criteria.where(key).greaterThanOrEquals(value);
                break;
            case "<":
                criteria = Criteria.where(key).lessThan(value);
                break;
            case "<=":
                criteria = Criteria.where(key).lessThanOrEquals(value);
                break;
        }
        return criteria;
    }
}
