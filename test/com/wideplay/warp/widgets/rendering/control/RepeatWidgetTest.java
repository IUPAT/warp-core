package com.wideplay.warp.widgets.rendering.control;

import com.wideplay.warp.widgets.Evaluator;
import com.wideplay.warp.widgets.MvelEvaluator;
import com.wideplay.warp.widgets.Respond;
import com.wideplay.warp.widgets.RespondersForTesting;
import com.wideplay.warp.widgets.compiler.ExpressionCompileException;
import com.wideplay.warp.widgets.rendering.DynTypedMvelEvaluatorCompiler;
import org.mvel.optimizers.OptimizerFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public class RepeatWidgetTest {
    private static final String LISTS_AND_TIMES = "listsAndTimes";
    private static final String EXPRS_AND_OBJECTS = "exprsNObjs";

    private static final String A_NAME = "Dhanji";

    @DataProvider(name = LISTS_AND_TIMES)
    public Object[][] getlistsAndTimes() {
        return new Object[][] {
            { 5, Arrays.asList(1,2,3,4,4) },
            { 4, Arrays.asList(1,2,3,4) },
            { 16, Arrays.asList(1,2,3,2,2,2,2,1,2,1,2,1,2,1,2,2) },
            { 0, Arrays.asList() },
        };
    }


    @Test(dataProvider = LISTS_AND_TIMES)
    public final void repeatNumberOfTimes(int should, final Collection<Integer> ints) {

        final int[] times = new int[1];
        final WidgetChain mockChain = new ProceedingWidgetChain() {
            @Override
            public void render(Object bound, Respond respond) {
                times[0]++;
            }
        };


        new RepeatWidget(mockChain, "items=beans", new MvelEvaluator())
                .render(new HashMap<String, Object>() {{
                    put("beans", ints);
                }}, RespondersForTesting.newRespond());

        assert times[0] == should : "Did not run expected number of times: " + should;
    }

    @DataProvider(name = EXPRS_AND_OBJECTS)
    public Object[][] getExpressionsAndObjects() {
        return new Object[][] {
                { "items=things, var='thing', pageVar='page'", new HashMap<String, Object>() {{
                    put("things", Arrays.asList(new Thing(), new Thing(), new Thing()));
                }}, 3, "thing"
                },
                { "items=things, pageVar='page'", new HashMap<String, Object>() {{
                    put("things", Arrays.asList(new Thing(), new Thing(), new Thing()));
                }}, 3, "__this"
                },
                { "items=things, var='thingy', pageVar='page'", new HashMap<String, Object>() {{
                    put("things", Arrays.asList(new Thing(), new Thing(), new Thing()));
                }}, 3, "thingy"
                }
        };
    }

//    @Test(dataProvider = EXPRS_AND_OBJECTS)
    public final void repeatNumberOfTimesWithVars(String expression, Object page, int should, final String exp) throws ExpressionCompileException {
        OptimizerFactory.setDefaultOptimizer(OptimizerFactory.SAFE_REFLECTIVE);
        final int[] times = new int[1];
        final Evaluator evaluator = new DynTypedMvelEvaluatorCompiler(null).compile(exp);
        final WidgetChain mockChain = new ProceedingWidgetChain() {
            @Override
            public void render(final Object bound, Respond respond) {
                times[0]++;

                final Object thing = evaluator.evaluate(exp, bound);
                assert thing instanceof Thing : "Contextual (current) var not set: " + thing;
                assert A_NAME.equals(((Thing)thing).getName());
            }
        };


        new RepeatWidget(mockChain, expression, evaluator)
                .render(page, RespondersForTesting.newRespond());

        assert times[0] == should : "Did not run expected number of times: " + should;
    }

    public static class Thing {
        private String name = A_NAME;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
