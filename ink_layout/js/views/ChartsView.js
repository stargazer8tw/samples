define([
    'jquery',
    'underscore',
    'backbone',
    'd3',
    'nvd3'
], function ($, _, Backbone, d3, nv) {
    "use strict";
    var charts = Backbone.View.extend({
        drawBar: function (data) {
            nv.addGraph(function () {
                var chart = nv.models.discreteBarChart()
                    .x(function (d) {
                        return d.label
                    })
                    .y(function (d) {
                        return d.value
                    })
                    .staggerLabels(true)
                    .tooltips(false)
                    .showValues(true);

                d3.select('#chart svg')
                        .datum(data)
                        .transition().duration(500)
                        .call(chart);
                nv.utils.windowResize(chart.update);
                return chart;
            });
        }
    });
    return charts;
});
