define([
    'jquery',
    'underscore',
    'backbone',
    //'models/blog/BlogModel',
    'text!data.templates/default.html',
    'text!data.content/demo/demo.html',
    'text!data.content/demo/demo-sidebar.html',
    'd3',
    'nvd3'
], function ($, _, Backbone, defaultTemplate, demo, sidebar) {
    "use strict";
    var DemoView = Backbone.View.extend({
        el: $("#page"),
        render: function () {
            $('.nav li').removeClass('active');
            $('.nav li a[href="' + window.location.hash + '"]').parent().addClass('active');

            this.$el.html(defaultTemplate);
            $('#main').html(demo);
            $('#sidebar').html(sidebar);
        }
    });
    return DemoView;
});
