const express = require('express');
const http = require('http');
const morgan = require('morgan');
const bodyParser = require('body-parser');
const mongoose = require('mongoose');
const cors = require('cors');
var CONFIG = require('./config.json');
var PORT = process.env.PORT || parseInt(CONFIG.server.port, 10);
var HOST_NAME = process.env.HOST || CONFIG.server.hostName;
// var tokenMiddleware = require('./middleware/token');
var passport = require('passport');
require('dotenv').config();

// connecting to the database
const url = process.env.MONGODB_URI || 'mongodb://' + process.env.HOST + ':27017/' + process.env.DB_NAME;
const connect = mongoose.connect(url, { useNewUrlParser: true, useUnifiedTopology: true, useCreateIndex: true });
connect
    .then((db) => {
        console.log("Connected correctly to the database");
    }, (err) => {
        console.log(err);
    });

// middleware
const app = express();
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
    extended: false
}));
app.use(cors());
app.use(morgan('dev'));
app.use(passport.initialize());
// routes
const mainRoutes = require('./routes/index');
const usersRoutes = require('./routes/users');
const profileRoutes = require('./routes/profile');
const eventRoutes = require('./routes/event');
app.use('/', mainRoutes);
app.use('/api/users', usersRoutes);
app.use('/api/profile', profileRoutes);
app.use('/api', eventRoutes);


//starup the server
var server = app.listen(PORT, function () {
    var host = server.address().address;
    var port = server.address().port;

    console.log('Server listening at http://%s:%s', HOST_NAME, port);
});