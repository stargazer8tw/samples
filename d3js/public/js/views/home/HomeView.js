define([
    'jquery',
    'underscore',
    'backbone',
    'showdown',
    'text!data.templates/default.html',
    'text!data.content/home/home.md'
], function ($, _, Backbone, Showdown, template, MD) {
    "use strict";
    var HomeView = Backbone.View.extend({
        el: $("#page"),

        render: function () {

            $('.nav li').removeClass('active');
            $('.nav li a[href="#"]').parent().addClass('active');
            var content = this.$el.html(template);
//            var hiddenBox = $( "#banner" );
//            hiddenBox.show();
//            this.$el.find("#main").html(C);
            var converter = new Showdown.converter();
            $('#main').html(converter.makeHtml(MD));
        }

    });

    return HomeView;

});
