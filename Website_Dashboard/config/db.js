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


exports.JitsiDashboard = mongoose.model('JitsiDashboard', JitsiDashboardSchema);