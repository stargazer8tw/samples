define([
    'jquery',
    'underscore',
    'backbone',
    'views/home/HomeView',
    'views/blog/BlogView',
    'views/about/AboutView'
], function ($, _, Backbone, HomeView, BlogView, AboutView) {
//    "use strict";
    var AppRouter = Backbone.Router.extend({
        routes: {
            // Define some URL routes
            'blog': 'showBlog',
            'about': 'showAbout',
            // Default
            '*actions': 'defaultAction'
        }
    });

    var initialize = function () {

        var app_router = new AppRouter();

        app_router.on('route:showBlog', function () {

            // Call render on the module we loaded in via the dependency array
            var blogView = new BlogView();
            blogView.render();

        });

        app_router.on('route:showAbout', function () {
            var aboutView = new AboutView();
            aboutView.render();
        });

        app_router.on('route:defaultAction', function (actions) {

            // We have no matching route, lets display the home page
            var homeView = new HomeView();
            homeView.render();

        });

        // Unlike the above, we don't call render on this view as it will handle
        // the render call internally after it loads data. Further more we load it
        // outside of an on-route function to have it loaded no matter which page is
        // loaded initially.

        Backbone.history.start();
    };

    return {
        initialize: initialize
    };
});
