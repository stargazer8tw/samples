http: //tongwen.openfoundry.org/
// 簡 to 繁
javascript: (function () {
    var c = document.getElementById("tongwen_core");
    if (c != null) {
        document.body.removeChild(s);
    } else {
        var c = document.createElement("script");
        c.language = "javascript";
        c.type = "text/javascript";
        c.src = "http://tongwen.openfoundry.org/src/web/tongwen_core.js";
        c.id = "tongwen_core";
    }
    document.body.appendChild(c);
    var s = document.getElementById("tongwen_s2t");
    if (s != null) {
        document.body.removeChild(s);
    }
    var s = document.createElement("script");
    s.language = "javascript";
    s.type = "text/javascript";
    s.src = "http://tongwen.openfoundry.org/src/web/tongwen_table_s2t.js";
    s.id = "tongwen_s2t";
    document.body.appendChild(s);
    TongWen.trans2Trad(window.document);
})();
// min
javascript: (function () {var s = document.getElementById("tongwen_s2t");if (s != null) {document.body.removeChild(s);}var s = document.createElement("script");s.language = "javascript";s.type = "text/javascript";s.src = "http://tongwen.openfoundry.org/src/web/tongwen_table_s2t.js";s.id = "tongwen_s2t";document.body.appendChild(s);var c = document.getElementById("tongwen_core");if (c != null) {document.body.removeChild(s);}var c = document.createElement("script");c.language = "javascript";c.type = "text/javascript";c.src = "http://tongwen.openfoundry.org/src/web/tongwen_core.js";c.id = "tongwen_core";document.body.appendChild(c);TongWen.trans2Trad(window.document);})();

// 繁 to 簡
javascript: (function () {
    var c = document.getElementById("tongwencore");
    if (c != null) {
        document.body.removeChild(s);
    }
    var c = document.createElement("script");
    c.language = "javascript";
    c.type = "text/javascript";
    c.src = "http://tongwen.openfoundry.org/src/web/tongwen_core.js";
    c.id = "tongwencore";
    document.body.appendChild(c);
    var s = document.getElementById("tongwenlet_t2s");
    if (s != null) {
        document.body.removeChild(s);
    }
    var s = document.createElement("script");
    s.language = "javascript";
    s.type = "text/javascript";
    s.src = "http://tongwen.openfoundry.org/src/web/tongwen_table_t2s.js";
    s.id = "tongwenlet_t2s";
    document.body.appendChild(s);
    TongWen.trans2Simp(document);
})();

// min
javascript: (function () {var s = document.getElementById("tongwen_t2s");if (s != null) {document.body.removeChild(s);}var s = document.createElement("script");s.language = "javascript";s.type = "text/javascript";s.src = "http://tongwen.openfoundry.org/src/web/tongwen_table_t2s.js";s.id = "tongwen_t2s";document.body.appendChild(s);var c = document.getElementById("tongwen_core");if (c != null) {document.body.removeChild(s);}var c = document.createElement("script");c.language = "javascript";c.type = "text/javascript";c.src = "http://tongwen.openfoundry.org/src/web/tongwen_core.js";c.id = "tongwen_core";document.body.appendChild(c);TongWen.trans2Simp(window.document);})();

// other
javascript: (function () {
    var s = document.createElement("script");
    s.language = "javascript";
    s.type = "text/javascript";
    s.src = "http://tongwen.openfoundry.org/src/web/tongwen_table_ps2t.js";
    s.id = "tongwenlet_ps2t";
    TongWen.trans2Trad(document);
})();

javascript: (function () {
    var s = document.createElement("script");
    s.language = "javascript";
    s.type = "text/javascript";
    s.src = "http://tongwen.openfoundry.org/src/web/tongwen_table_pt2s.js";
    s.id = "tongwenlet_pt2s";
    TongWen.trans2Simp(document);
})();

javascript: (function () {
    var s = document.getElementById("tongwenlet_tw");
    if (s != null) {
        document.body.removeChild(s);
    }
    var s = document.createElement("script");
    s.language = "javascript";
    s.type = "text/javascript";
    s.src = "http://tongwen.openfoundry.org/NewTongWen/tools/bookmarklet_tw.js";
    s.id = "tongwenlet_tw";
    document.body.appendChild(s);
})();

javascript: (function () {
    var s = document.getElementById("tongwenlet_s2t");
    if (s != null) {
        document.body.removeChild(s);
    }
    var s = document.createElement("script");
    s.language = "javascript";
    s.type = "text/javascript";
    s.src = "http://tongwen.openfoundry.org/src/web/tongwen_table_s2t.js";
    s.id = "tongwenlet_s2t";
    document.body.appendChild(s);
    TongWen.trans2Trad(window.document.body.innerHTML);
})();

javascript: (function () {
    var s = document.getElementById("tongwenlet_tw");
    if (s != null) {
        document.body.removeChild(s);
    }
    var s = document.createElement("script");
    s.language = "javascript";
    s.type = "text/javascript";
    s.src = "http://tongwen.openfoundry.org/NewTongWen/tools/bookmarklet_tw.js";
    s.id = "tongwenlet_tw";
    document.body.appendChild(s);
})();
