var express = require('express');
var router = express.Router();

// for database
var mongoose = require('mongoose');
var db = require('../config/db');
var JitsiDashboard = db.JitsiDashboard;

// for render .html file.
var path = require("path");

/* GET home page. */
router.get('/', function(req, res, next) {
  // res.render('../views/index.ejs', { title: 'Jitsi Dashboard' });
  res.sendFile(path.join(__dirname + '/../views/Frontend/dashboard.html'));
});

module.exports = router;

