(function() {
	var i = 1000;

	test = function(p) {
		var j = (i * Math.random()).toFixed(2);
		var k = j.length;
		for (; k <= 6; k++) {
			j = ' ' + j;
		}
		return 'RANDOM:' + j + ', ' + eval(p);
	};
})();