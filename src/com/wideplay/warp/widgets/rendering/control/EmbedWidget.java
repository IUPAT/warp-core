package com.wideplay.warp.widgets.rendering.control;

import com.google.inject.Inject;
import com.wideplay.warp.widgets.Evaluator;
import com.wideplay.warp.widgets.Renderable;
import com.wideplay.warp.widgets.Respond;
import com.wideplay.warp.widgets.compiler.Parsing;
import com.wideplay.warp.widgets.routing.PageBook;
import net.jcip.annotations.Immutable;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@Immutable
class EmbedWidget implements Renderable {
    private final Map<String, String> bindExpressions;
    private final Map<String, ArgumentWidget> arguments;
    private final Evaluator evaluator;
    private final PageBook pageBook;
    private final String targetPage;

    private EmbeddedRespondFactory factory;

    public EmbedWidget(Map<String, ArgumentWidget> arguments, String expression, Evaluator evaluator, PageBook pageBook, String targetPage) {
        this.arguments = arguments;

        this.evaluator = evaluator;
        this.pageBook = pageBook;
        this.targetPage = targetPage.toLowerCase();

        //parse expression list
        this.bindExpressions = Parsing.toBindMap(expression);
    }



    public void render(Object bound, Respond respond) {
        final PageBook.Page page = pageBook.forName(targetPage);

        //create an instance of the embedded page
        final Object pageObject = page.instantiate();

        //bind parameters to it as necessary
        for (Map.Entry<String, String> entry : bindExpressions.entrySet()) {
            evaluator.write(entry.getKey(), pageObject, evaluator.evaluate(entry.getValue(), bound));
        }

        //chain to embedded page (widget), with arguments
        final EmbeddedRespond embed = factory.get(arguments);
        page.widget().render(pageObject, embed);

        //extract and write embedded response to enclosing page's respond
        respond.writeToHead(embed.toHeadString()); //TODO only write @Require tags
        respond.write(embed.toString());
    }

    public <T extends Renderable> Set<T> collect(Class<T> clazz) {
        return Collections.emptySet();
    }

    //TODO improve to constructor injected
    @Inject
    public void setFactory(EmbeddedRespondFactory factory) {
        this.factory = factory;
    }
}
