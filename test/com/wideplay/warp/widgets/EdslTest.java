package com.wideplay.warp.widgets;

import org.testng.annotations.Test;
import com.google.inject.*;
import static org.easymock.EasyMock.*;

import javax.servlet.ServletContext;
import java.util.Set;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail com)
 */
public class EdslTest {

    @Test
    public final void edsl() {
        Package aPackage = Package.getPackage("com.wideplay.warp.widgets.example");
        Package anotherPackage = Package.getPackage("com.wideplay.warp.widgets.example");

        final Module module = Widgets.configure()
                .with(aPackage)
                .with(anotherPackage)

                .buildModule();

        assert null != module : "nothing built out of config";

        final Set<Package> packages = Guice.createInjector(module, new AbstractModule() {
            protected void configure() {
                bind(ServletContext.class).toInstance(createNiceMock(ServletContext.class));
            }
        })
                .getInstance(Key.get(new TypeLiteral<Set<Package>>() {}, Packages.class));

        assert null != packages;
        assert packages.contains(aPackage) : "package binding incorrect!";
        assert packages.contains(anotherPackage) : "package binding incorrect!";
    }
}