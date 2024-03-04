package ru.clevertec.comment.search;

import jakarta.validation.constraints.NotNull;
import org.apache.lucene.analysis.charfilter.HTMLStripCharFilterFactory;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.apache.lucene.analysis.snowball.SnowballPorterFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurationContext;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurer;

/**
 * Configuration class for Lucene analysis.
 *
 * @author Sergey Leshkevich
 * @version 1.0
 */
public class LuceneConfig implements LuceneAnalysisConfigurer {

    /**
     * Constant representing the English language.
     */
    public static final String ENGLISH = "english";

    /**
     * Configures Lucene analysis with specified context.
     *
     * @param context Lucene analysis configuration context.
     */
    @Override
    public void configure(@NotNull LuceneAnalysisConfigurationContext context) {
        context.analyzer(ENGLISH)
                .custom()
                .tokenizer(StandardTokenizerFactory.class)
                .charFilter(HTMLStripCharFilterFactory.class)
                .tokenFilter(LowerCaseFilterFactory.class)
                .tokenFilter(SnowballPorterFilterFactory.class)
                .param("language", "English")
                .tokenFilter(ASCIIFoldingFilterFactory.class);
    }
}
