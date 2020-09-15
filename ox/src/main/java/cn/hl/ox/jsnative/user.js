(function () {
    var i = 1000;

    someFunction = function (p) {
        var j = (i * Math.random()).toFixed(2), k = j.length;
        for (; k <= 6; k++) {
            j = ' ' + j;
        }
        return 'RANDOM:' + j + ', ' + eval(p);
    };
})();