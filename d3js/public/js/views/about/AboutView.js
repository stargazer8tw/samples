define([
    'jquery',
    'underscore',
    'backbone',
    'showdown',
    'text!data.templates/default.html',
    'text!data.content/about/about.md'
], function ($, _, Backbone, Showdown, template, MD) {
    "use strict";
    var AboutView = Backbone.View.extend({
        el: $("#page"),
        render: function () {
            $('.nav li').removeClass('active');
            $('.nav li a[href="' + window.location.hash + '"]').parent().addClass('active');
            this.$el.html(template);
//            var hiddenBox = $( "#banner" );
//            hiddenBox.show();
            var converter = new Showdown.converter();
            $('#main').html(converter.makeHtml(MD));
        }
    });
    return AboutView;
});
