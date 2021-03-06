require.config({
    paths: {
        'data.content': '../content',
        'data.templates': '../templates',
        'data.images': '../img',
        // javascript library
        jquery: 'lib/jquery/jquery-2.1.1.min',
        text: 'lib/require/text',
        underscore: 'lib/underscore/underscore-min',
        backbone: 'lib/backbone/backbone-min',
        // none amd compliance
        modernizr: 'lib/modernizr/modernizr-2.6.2.min',
        bootstrap: 'lib/bootstrap/bootstrap.min',
        showdown: 'lib/showdown/showdown',
        d3: 'lib/d3/d3.min',
        nvd3: 'lib/nvd3/nv.d3.min'
//        markdownConverter: "../lib/pagedown/Markdown.Converter",
//        markdownSanitizer: '../lib/pagedown/Markdown.Sanitizer'
        //        mdown: '../lib/require/mdown'
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
//        },
//        // markdown
//        'markdown': {
//            deps: ['markdownConverter'],
//            exports: 'markdownSanitizer'
        },
        nvd3: {
            exports: 'nv',
            deps: ['d3.global']
        }
    },
    enforceDefine: true
});

//define("markdown", ["markdownConverter", "markdownSanitizer"], function (mc, ms) {
//    "use strict";
//    return window.Markdown;
//});
//
//define("markdown", function (markdown) {
//    "use strict";
//    var converter = new Markdown.Converter();
//    return {
//        converter: converter
//    }
//});

define("d3.global", ["d3"], function(_) {
      d3 = _;
});

require([
 //     'jquery',
 //     'modernizr',
    'bootstrap',
 //    'plugins',
    'app'
], function (Bootstrap, App) {
    "use strict";
    // The "app" dependency is passed in as "App"
    // Again, the other dependencies passed in are not "AMD" therefore don't pass a parameter to this function

    App.initialize();
});
