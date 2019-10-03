const express = require('express');
const profileRouter = express.Router();
const bodyParser = require('body-parser');
profileRouter.use(bodyParser.json());
var User = require('../models/user');
var authenticate = require('../middleware/auth');
var jwt = require('jsonwebtoken');

profileRouter.route('/:userId')
    .get(authenticate.verifyUser, (req, res, next) => {
        var userId = req.params.userId;
        User.findById(userId)
            .then((user) => {
                if (user) {
                    res.statusCode = 200;
                    res.setHeader('Content-Type', 'application/json');
                    res.json(user);
                }
                else {
                    res.statusCode = 404;
                    res.setHeader('Content-Type', 'application/json');
                    res.json({ err: "User doesn't exist" });
                }
            }, (err) => {
                if (err.name === "CastError") {
                    res.statusCode = 404;
                    res.setHeader('Content-Type', 'application/json');
                    res.json({ err: "Invalid user id" });
                }
                else {
                    res.statusCode = 500;
                    res.setHeader('Content-Type', 'application/json');
                    res.json({ err: err });
                }
            })
            .catch((err) => {
                res.statusCode = 404;
                res.setHeader('Content-Type', 'application/json');
                res.json({ err: err });
            })
    })
    .post((req, res, next) => {
        res.statusCode = 403;
        res.end('POST operation not supported on /profile');
    })
    .put(authenticate.verifyUser, (req, res, next) => {
        var userId = req.params.userId;
        var currentUser = req.headers;
        var token = currentUser.authorization.split(" ")[1];
        currentUser = jwt.decode(token)._id;
        if (currentUser !== userId) {
            res.statusCode = 403;
            res.setHeader('Content-Type', 'application/json');
            res.json({ status: 403, message: "Not allowed"});
        }
        User.findByIdAndUpdate(userId, {
            // TODO
            $set: req.body
        }, { new: true })
            .then((user) => {
                res.statusCode = 200;
                res.setHeader('Content-Type', 'application/json');
                res.json(user)
            }, (err) => {
                res.statusCode = 500;
                res.setHeader('Content-Type', 'application/json');
                res.json({ err: err });
            })
            .catch((err) => {
                res.statusCode = 500;
                res.setHeader('Content-Type', 'application/json');
                res.json({ err: err });
            })
    })
    .delete((req, res, next) => {
        res.statusCode = 403;
        res.end('delete operation not supported on /profile');
    })

module.exports = profileRouter;