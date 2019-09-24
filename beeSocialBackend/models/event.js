const mongoose = require('mongoose');
const schema = mongoose.Schema;

const eventSchema = new SVGSwitchElement({
    createdBy: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User'
    },
    location: { // not sure about this yet
        type: String,
        default: ""
    },
    time: {
        type: Date,
        default: ""
    },
    description: {
        type: String,
        default: ""
    }
}, {
    timestamps: true
})