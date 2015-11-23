var express = require('express');
var router = express.Router();

// for database
var mongoose = require('mongoose');
var db = require('../config/db');
var JitsiDashboard = mongoose.model('JitsiDashboard');
var rest = require('../config/rest');

// // for render .html file.
// var path = require("path");

/* GET home page. */
router.get('/', function(req, res, next) {
	JitsiDashboard.find(
		function(error, boardRows) {
			res.render('index.ejs', {boardRows:boardRows});
	});
  /*res.render('../views/index.ejs'*//*, { title: 'Jitsi Dashboard' }*//*);*/
  // res.sendFile(path.join(__dirname + '/../views/Frontend/dashboard.html'));
});

module.exports = router;

