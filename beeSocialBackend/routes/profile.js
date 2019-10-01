const express = require('express');
const profileRouter = express.Router();
profileRouter.use(express.json());
var User = require('../models/user');
var authenticate = require('../middleware/auth');

profileRouter.route('/:id')
    .get(authenticate.verifyUser, (req, res, next) => {
        var userId = req.params.id;
        User.findById({id: userId})
        .then((user) => {
            res.statusCode = 200;
            res.setHeader('Content-Type', 'application/json');
            res.json(user);
        }, (err) => {
            res.statusCode = 500;
            res.setHeader('Content-Type', 'application/json');
            res.json({err: err});
        })
        .catch((err) => {
            res.statusCode = 404;
            res.setHeader('Content-Type', 'application/json');
            res.json({err: err});
        })
    })
    // .post(authenticate.verifyUser, (req, res, next) => {

    // })
    // .put()
    // .delete()

