// Build Database
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
	endpoints: Number,
	amountsentmb: Number,
	sendbitrate: Number,
	amountrevceivemb: Number,
	receivebitrate: Number,
	audiopackagelost: Number,
	vidwopackagelost: Number
});


// exports.JitsiDashboard = mongoose.model('JitsiDashboard', JitsiDashboardSchema);

var JitsiDashboard = mongoose.model('JitsiDashboard', JitsiDashboardSchema);

var endpoints = [37, 14, 15, 85, 126];
var amountsentmb = [19980, 2730, 3150, 107100, 236250];
var sendbitrate = [22.2, 3.03, 3.5, 119, 262.5];
var amountrevceivemb = [555, 210, 225, 1275, 1890];
var receivebitrate = [0.617, 0.233, 0.25, 1.417, 2.1];
var audiopackagelost = [0.9, 0.4, 0.29, 0.31, 0.24];
var vidwopackagelost = [0.17, 0.3, 0.41, 0.39, 0.76];


for (var i = 0; i < 5; i++) {
	JitsiDashboard.create(
		{
			endpoints: endpoints[i],
			amountsentmb: amountsentmb[i],
			sendbitrate: sendbitrate[i],
			amountrevceivemb: amountrevceivemb[i],
			receivebitrate: receivebitrate[i],
			audiopackagelost: audiopackagelost[i],
			vidwopackagelost: vidwopackagelost[i]
		},
		 function(err, createItem) {
			if(err) {
				return consold.log("MongoDB Error: " + err);
			}
		}
	);
}

module.exports = db;
