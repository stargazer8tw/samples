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
        ink: 'lib/ink/ink.min',
        d3: 'lib/d3/d3.min',
        nvd3: 'lib/nvd3/nv.d3.min'
    },
    shim: {
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
    'router'
], function (Router) {
    "use strict";
    Router.initialize();
});

