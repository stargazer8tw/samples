define([
    'jquery',
    'underscore',
    'backbone',
    //'models/blog/BlogModel',
    'text!data.templates/default.html',
    'text!data.content/demo/demo.html',
    'd3',
    'nvd3'
], function ($, _, Backbone, defaultTemplate, demo) {
    "use strict";
    var DemoView = Backbone.View.extend({
        el: $("#page"),
        render: function () {
            $('.nav li').removeClass('active');
            $('.nav li a[href="' + window.location.hash + '"]').parent().addClass('active');

            this.$el.html(defaultTemplate);
            $('#main').html(demo);
        }
    });
    return DemoView;
});
