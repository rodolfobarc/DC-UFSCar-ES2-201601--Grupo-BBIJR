package net.sf.jabref.logic.search.matchers;


public class MatcherSets {

    public static MatcherSet build(MatcherType ruleSet) {
        if (ruleSet == MatcherType.AND) {
            return new AndMatcher();
        } else {
            return new OrMatcher();
        }
    }

    public enum MatcherType {
        AND,
        OR
    }

}
