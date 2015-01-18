define([
    'jquery',
    'underscore',
    'backbone',
    'text!data.content/example.html'
], function ($, _, Backbone, example) {
    "use strict";
    var ExampleView = Backbone.View.extend({
        render: function () {
            $('nav li').removeClass('active');
            $('nav li a[href="#"]').parent().addClass('active');
            $('#chartmain').html(example);
        }
    });

    return ExampleView;

});
