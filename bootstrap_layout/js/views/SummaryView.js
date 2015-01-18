define([
    'jquery',
    'underscore',
    'backbone',
    'text!data.content/summary.html'
], function ($, _, Backbone, summary) {
    "use strict";
    var SummaryView = Backbone.View.extend({
        render: function () {
            $('nav li').removeClass('active');
            $('nav li a[href="#"]').parent().addClass('active');
            $('#chartmain').html(summary);
        }
    });

    return SummaryView;

});
