var https = require('https');

// database
var mongoose = require('mongoose');
mongoose.connect('mongodb://localhost/JitsiDashboard');

// Test if Database is Connected
var db = mongoose.connection;
db.on('error', console.error.bind(console, 'connection error:'));
db.once('open', function(callback) {
	console.log("Database JitsiDashboard is Connected.");
});

// Construct Database Schema
var JitsiDashboardSchema = new mongoose.Schema({
	cpu_usage: Number,
	used_memory: Number,
	rtp_loss: Number,
	bit_rate_download: Number,
	audiochannels: Number,
	bit_rate_upload: Number,
	conferences: Number,
	participants: Number,
	current_timestamp: String,
	threads: Number,
	total_memory: Number,
	videochannels: Number
});

var JitsiDashboard = mongoose.model('JitsiDashboard', JitsiDashboardSchema);



// rest API
var options = {
	host: '52.88.34.121',
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
	var stats1 = JSON.parse('{"cpu_usage":"0.03015", "used_memory":3732, "rtp_loss":"6", "audiochannels":0, "conferences":2, "participants":20, "current_timestamp":"2015-12-06 09:26:14.782", "videochannels":20}');
	var stats2 = JSON.parse('{"cpu_usage":"0.04213", "used_memory":4252, "rtp_loss":"22", "audiochannels":60, "conferences":2, "participants":60, "current_timestamp":"2015-12-06 09:26:14.782", "videochannels":0}');
	var stats3 = JSON.parse('{"cpu_usage":"0.02003", "used_memory":3288, "rtp_loss":"15", "audiochannels":20, "conferences":1, "participants":40, "current_timestamp":"2015-12-06 09:26:14.782", "videochannels":20}');
	var stats4 = JSON.parse('{"cpu_usage":"0.01088", "used_memory":1893, "rtp_loss":"8", "audiochannels":100, "conferences":3, "participants":100, "current_timestamp":"2015-12-06 09:26:14.782", "videochannels":100}');
	var stats5 = JSON.parse('{"cpu_usage":"0.50346", "used_memory":3846, "rtp_loss":"33", "audiochannels":50, "conferences":2, "participants":200, "current_timestamp":"2015-12-06 09:26:14.782", "videochannels":150}');
	var stats = [stats1, stats2, stats3, stats4, stats5];
	for (var i = 0; i < 5; i++) {
		JitsiDashboard.create(
			{
				cpu_usage: stats[i].cpu_usage,
				used_memory: stats[i].used_memory,
				rtp_loss: stats[i].rtp_loss,
				audiochannels: stats[i].audiochannels,
				conferences: stats[i].conferences,
				participants: stats[i].participants,
				current_timestamp: stats[i].current_timestamp,
				videochannels: stats[i].videochannels
			},
			 function(err, createItem) {
				if(err) {
					return consold.log("MongoDB Error: " + err);
				}
			}
		);
	}
});

module.exports = db;
