const express = require('express');
const http = require('http');
const morgan = require('morgan');
const bodyParser = require('body-parser');
const mongoose = require('mongoose');
const cors = require('cors');
var CONFIG = require('./config.json');
var PORT = parseInt(CONFIG.server.port, 10);
var HOST_NAME = CONFIG.server.hostName;
var DATABASE_NAME = CONFIG.database.name;
// var tokenMiddleware = require('./middleware/token');
var passport = require('passport');

// connecting to the database
const url = 'mongodb://' + HOST_NAME + ':27017/' + DATABASE_NAME;
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
app.use('/', mainRoutes);
app.use('/api/users', usersRoutes);
app.use('/api/profile', profileRoutes);


//starup the server
var server = app.listen(PORT, function () {
    var host = server.address().address;
    var port = server.address().port;

    console.log('Server listening at http://%s:%s', HOST_NAME, port);
});