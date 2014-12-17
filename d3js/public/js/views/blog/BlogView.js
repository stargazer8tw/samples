define([
    'jquery',
    'underscore',
    'backbone',
    //'models/blog/BlogModel',
    'text!data.templates/default.html'
], function ($, _, Backbone, defaultTemplate) {
    "use strict";
    var BlogView = Backbone.View.extend({
        el: $("#page"),
        render: function () {
            $('.nav li').removeClass('active');
            $('.nav li a[href="' + window.location.hash + '"]').parent().addClass('active');
            this.$el.html(defaultTemplate);
        }
    });
    return BlogView;
});
