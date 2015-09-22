var express = require('express');
var router = express.Router();

var mongoose = require('mongoose');
var db = require('../config/db');
var JitsiDashboard = db.JitsiDashboard;

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Jitsi Dashboard' });
});

module.exports = router;

