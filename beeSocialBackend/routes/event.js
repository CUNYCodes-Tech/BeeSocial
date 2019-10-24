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
    const long = req.query.longitude;
    const lati = req.query.latitude;
    const minD = req.query.minDistance;
    const maxD = req.query.maxDistance;
    const d = new Date();
    Event.find({
        location: {
            $near: {
                $geometry: { type: "Point", coordinates: [long, lati] },
                $minDistance: minD,
                $maxDistance: maxD
            }
        },
        time: {
            $gte: d
        }
    })
        .then((events) => {
            console.log(events);
            res.statusCode = 200;
            res.setHeader('Content-Type', 'application/json');
            res.json(events);
        })
        .catch((err) => {
            res.statusCode = 500;
            res.setHeader('Content-Type', 'application/json');
            res.json({ err: err });
        })

});

// find all event by name
eventRouter.get('/events/name', (req, res, next) => {
    const name = req.query.name;
    const d = new Date();
    Event.find({
        name: {
            $regex: name,
            $options: 'i'
        },
        time: {
            $gte: d
        }
    })
        .then((events) => {
            console.log(events);
            res.statusCode = 200;
            res.setHeader('Content-Type', 'application/json');
            res.json(events);
        })
        .catch((err) => {
            res.statusCode = 500;
            res.setHeader('Content-Type', 'application/json');
            res.json({ err: err });
        })

});

// find event by date
eventRouter.get('/events', (req, res, next) => {
    // get the user location from the query param
    // find all the events within that location
    const d = req.query.date ? new Date(req.query.date) : new Date()
    Event.find({
        time: {
            $gte: d
        }
    })
        .then((events) => {
            console.log(events);
            res.statusCode = 200;
            res.setHeader('Content-Type', 'application/json');
            res.json(events);
        })
        .catch((err) => {
            res.statusCode = 500;
            res.setHeader('Content-Type', 'application/json');
            res.json({ err: err });
        })

});

// create and event using post
eventRouter.post('/events', authenticate.verifyUser, (req, res, next) => {
    let newEvent = req.body;
    // newEvent.time = new Date(req.body.time);
    Event.create(newEvent)
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
    // get the neccessary information and update
    const update = req.body;
    Event.findByIdAndUpdate();
});

// show interest event
eventRouter.put('', authenticate.verifyUser, (req, res, next) => {
    // find the event, push the userid
});
// withdraw interest event
eventRouter.put('', authenticate.verifyUser, (req, res, next) => {
    // find the event update the interest
});


// delete event
eventRouter.delete('/events', authenticate.verifyUser, (req, res, next) => {
    Event.findByIdAndRemove(req.body.eventId)
        .then((event) => {
            res.statusCode = 200;
            res.setHeader('Content-Type', 'application/json');
            res.json(event);
        })
        .catch((err) => {
            res.statusCode = 500;
            res.setHeader('Content-Type', 'application/json');
            res.json({ err: err });
        })

});


module.exports = eventRouter;