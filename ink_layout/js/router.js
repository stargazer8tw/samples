define([
    'jquery',
    'underscore',
    'backbone',
    'views/MainView',
    'views/SummaryView',
    'views/QueryView'
], function ($, _, Backbone, MainView, SummaryView, QueryView) {
    "use strict";
    var AppRouter = Backbone.Router.extend({
        routes: {
            // Define some URL routes
            //            'demo': 'showDemo',
            //            "examples/:id": "getExample",
            'query': 'showQuery',
            // Default
            '*actions': 'defaultAction'
        }
    });

    var initialize = function () {
        var mainView = new MainView();
        mainView.render();

        var app_router = new AppRouter();
        app_router.on('route:showQuery', function () {
            var queryView = new QueryView();
            queryView.render();

        });

        app_router.on('route:defaultAction', function (actions) {
            var summaryView = new SummaryView();
            summaryView.render();

        });
        //        app_router.on('route:getExample', function (id) {
        //            var hiddenBox = $("#banner");
        //            hiddenBox.hide();
        //
        //            alert("get example id " +id);
        //        });
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
