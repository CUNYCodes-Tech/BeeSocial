const mongoose = require('mongoose');
const schema = mongoose.Schema;

const pointSchema = new schema({
    type: {
        type: String,
        enum: ['Point'],
        required: true
    },
    coordinates: {
        type: [Number],
        required: true
    }
});

const eventSchema = new schema({
    name: {
        type: String,
        require: true // must be require so the other user can search it up
    },
    createdBy: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User'
    },
    interested: [{
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User'
    }],
    //Note that longitude comes first in a GeoJSON coordinate array, not latitude.
    location: { 
        type: pointSchema,
        // default: {
        //     'type': 'Point',
        //     'coordiantes': [
        //         0, //longitude
        //         0 // latitude
        //     ]
        // }
        require: true // because we will need the lcoation in order for it to show up
    },
    time: {
        type: Date,
        default: ""
    },
    description: {
        type: String,
        default: ""
    },
    closed: {
        type: Boolean,
        default: false
    }
}, {
    timestamps: true
})

eventSchema.index({location: "2dsphere"})

module.exports = mongoose.model('Event', eventSchema);