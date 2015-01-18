define([
    'jquery',
    'underscore',
    'backbone',
    'text!data.content/main.html'
], function ($, _, Backbone, main) {
    "use strict";
    var MainView = Backbone.View.extend({
        el: $("#main"),

        render: function () {
            this.$el.html(main);
            $('nav li').removeClass('active');
            $('nav li a[href="#"]').parent().addClass('active');
        }

    });

    return MainView;

});
