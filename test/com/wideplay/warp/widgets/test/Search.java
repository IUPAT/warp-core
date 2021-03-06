package com.wideplay.warp.widgets.test;

import com.wideplay.warp.widgets.At;
import com.wideplay.warp.widgets.Get;
import com.wideplay.warp.widgets.On;
import com.wideplay.warp.widgets.rendering.resources.Export;

import java.util.Collection;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@At("/wiki/search") @On("event") @Export(at = "/my.js", resource = "my.js")
public class Search {   //defaults to @Show("Search.xhtml"), or @Show("Search.html")

    private int counter;
    private String query;   //"get" param
    private Collection<Movie> movies;

    public Collection<Movie> getMovies() {
        return movies;
    }


    public static class Movie {
        public String getMovieName() {
            return "thing";
        }

    }

    @Get("results")
    public void showResults() { //called after parameters are bound
    }


    //how about a search bar widget?
    @Get("widget")
    public void showSearchWidget() {
        //don't need to do anything but you could set up some contextual info on the widget here
    }

    public int getCounter() {
        return counter;
    }

    public String getQuery() {
        return query;
    }
}
