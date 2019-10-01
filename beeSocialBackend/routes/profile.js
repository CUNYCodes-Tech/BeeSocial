const express = require('express');
const profileRouter = express.Router();
const bodyParser = require('body-parser');
profileRouter.use(bodyParser.json());
var User = require('../models/user');
var authenticate = require('../middleware/auth');

profileRouter.route('/:id')
    .get(authenticate.verifyUser, (req, res, next) => {
        var userId = req.params.id;
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
// .post(authenticate.verifyUser, (req, res, next) => {

// })
// .put()
// .delete()

module.exports = profileRouter;