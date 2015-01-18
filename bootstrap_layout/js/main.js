require.config({
    paths: {
        'data.content': '../content',
        'data.images': '../img',
        // javascript library
        jquery: 'lib/jquery/jquery-2.1.3.min',
        text: 'lib/require/text',
        underscore: 'lib/underscore/underscore-min',
        backbone: 'lib/backbone/backbone-min',
        // none amd compliance
        bootstrap: 'lib/bootstrap/bootstrap.min',
        d3: 'lib/d3/d3.min',
        nvd3: 'lib/nvd3/nv.d3.min'
    },
    shim: {
        'bootstrap': {
            deps: ['jquery'],
            exports: '$.fn.popover'
        },
        'bootstrap/affix': {
            deps: ['jquery'],
            exports: '$.fn.affix'
        },
        'bootstrap/alert': {
            deps: ['jquery'],
            exports: '$.fn.alert'
        },
        'bootstrap/button': {
            deps: ['jquery'],
            exports: '$.fn.button'
        },
        'bootstrap/carousel': {
            deps: ['jquery'],
            exports: '$.fn.carousel'
        },
        'bootstrap/collapse': {
            deps: ['jquery'],
            exports: '$.fn.collapse'
        },
        'bootstrap/dropdown': {
            deps: ['jquery'],
            exports: '$.fn.dropdown'
        },
        'bootstrap/modal': {
            deps: ['jquery'],
            exports: '$.fn.modal'
        },
        'bootstrap/popover': {
            deps: ['jquery'],
            exports: '$.fn.popover'
        },
        'bootstrap/scrollspy': {
            deps: ['jquery'],
            exports: '$.fn.scrollspy'
        },
        'bootstrap/tab': {
            deps: ['jquery'],
            exports: '$.fn.tab'
        },
        'bootstrap/tooltip': {
            deps: ['jquery'],
            exports: '$.fn.tooltip'
        },
        'bootstrap/transition': {
            deps: ['jquery'],
            exports: '$.fn.transition'
        },
        nvd3: {
            exports: 'nv',
            deps: ['d3.global']
        }
    },
    enforceDefine: true
});

define("d3.global", ["d3"], function(_) {
      d3 = _;
});

define([
    'bootstrap',
    'router'
], function (Bootstrap, Router) {
    "use strict";
    Router.initialize();
});

