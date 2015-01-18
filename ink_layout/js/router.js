define([
    'jquery',
    'underscore',
    'backbone',
    'd3',
    'views/D3Chart',
    'views/D3ChartExample',
    'views/MainView',
    'views/SummaryView',
    'views/QueryView',
    'views/ExampleView'
], function ($, _, Backbone, d3, D3Chart, D3ChartExample, MainView, SummaryView, QueryView, ExampleView) {
    "use strict";
    var AppRouter = Backbone.Router.extend({
        routes: {
            // Define some URL routes
            //            'demo': 'showDemo',
            //            "examples/:id": "getExample",
            'query': 'showQuery',
            'example': 'showExample',
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
        app_router.on('route:showExample', function (actions) {
            var exampleView = new ExampleView();
            exampleView.render();
            var chart = new D3ChartExample();
            d3.json("../data/bar.json", function (error, data) {
                //                    console.log(data);
                chart.drawBar(data);
            });
            chart.drawSunburstSample();

        });
        app_router.on('route:defaultAction', function (actions) {
            var summaryView = new SummaryView();
            summaryView.render();
            var chart = new D3Chart();

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
