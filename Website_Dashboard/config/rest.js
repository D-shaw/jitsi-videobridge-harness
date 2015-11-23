var https = require('https');

var options = {
	host: '52.88.34.121',
	port: 443,
	path: '/colibri/stats',
	method: 'GET'
};

console.log("Getting Rest API");

var req = https.request(options, function(res) {
	var body = '';
	console.log("start getting JSON");
	res.on('data', function(d) {
		body += d;
	});
	res.on('end', function() {
		var stats = JSON.parse(body);
		console.log("Print Stats: ", stats.picture);
	});
});

req.end();
req.on('error', function(e) {
	console.error("Fail To Get REST JSON");
	console.error(e);
});

module.exports = req;