const express = require('express');
const eventRouter = express.Router();
const bodyParser = require('body-parser');
eventRouter.use(bodyParser.json());
var User = require('../models/user');
var Event = require('../models/event');
var authenticate = require('../middleware/auth');

// find all the event by location
eventRouter.get('/events/location', (req, res, next) => {
    // get the user location from the query param
    // find all the events within that location
    const long = req.query.longitude
    const lati = req.query.latitude
    const minD = req.query.minDistance
    const maxD = req.query.maxDistance
    Event.find({
        location: {
            $near: {
                $geometry: { type: "Point", coordinates: [long, lati] },
                $minDistance: minD,
                $maxDistance: maxD
            }
        }
    })
        .then((events) => {

        })
        .catch((err) => {

        })

});

// find all event by name
eventRouter.get('/events/name', (req, res, next) => {
    // get the user location from the query param
    // find all the events within that location
    const long = req.query.longitude
    const lati = req.query.latitude
    const minD = req.query.minDistance
    const maxD = req.query.maxDistance
    Event.find({
        location: {
            $near: {
                $geometry: { type: "Point", coordinates: [long, lati] },
                $minDistance: minD,
                $maxDistance: maxD
            }
        }
    })
        .then((events) => {

        })
        .catch((err) => {

        })

});

// create and event using post
eventRouter.post('/events', authenticate.verifyUser, (req, res, next) => {
    Event.create(req.body)
        .then((event) => {
            //console.log(event);
            User.findById(event.createdBy)
            .then((user) => {
                console.log(event._id);
                user.postedEvent.push(event._id);
                return user.save()
            })
            .then((user) => {
                console.log(user);
                res.statusCode = 200;
                res.setHeader('Content-Type', 'application/json');
                res.json(user);
            })
            .catch((err) => {
                res.statusCode = 500;
                res.setHeader('Content-Type', 'application/json');
                res.json({ err: err });
            })
        })
        // .then((user) => {
        //     console.log(event._id);
        //     user.postedEvent.push(event._id);
        //     return user.save()
        // })
        .catch((err) => {
            res.statusCode = 500;
            res.setHeader('Content-Type', 'application/json');
            res.json({ err: err });
        })
});

// update event
eventRouter.put('', authenticate.verifyUser, (req, res, next) => {

});

// show interest event
eventRouter.put('', authenticate.verifyUser, (req, res, next) => {

});
// withdraw interest event
eventRouter.put('', authenticate.verifyUser, (req, res, next) => {

});
// delete event
eventRouter.delete('', authenticate.verifyUser, (req, res, next) => {

});


module.exports = eventRouter;