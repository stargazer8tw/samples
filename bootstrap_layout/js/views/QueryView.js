define([
    'jquery',
    'underscore',
    'backbone',
    'text!data.content/query.html'
], function ($, _, Backbone, query) {
    "use strict";
    var MainView = Backbone.View.extend({
        render: function () {
            $('nav li').removeClass('active');
            $('nav li a[href="#query"]').parent().addClass('active');
            $('#chartmain').html(query);

        }

    });

    return MainView;

});
