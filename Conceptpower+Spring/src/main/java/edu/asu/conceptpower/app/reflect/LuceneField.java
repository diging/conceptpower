package edu.asu.conceptpower.app.reflect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LuceneField {

    String lucenefieldName();
    boolean isTokenized();
    boolean isMultiple();

    /**
     * This will be set to true if we need to index the field twice (first
     * tokenized, second non tokenized) to support short word search.
     * 
     * @return
     */
    boolean isShortWordSearchAllowed() default false;

    String luceneShortFieldName() default "";
}
