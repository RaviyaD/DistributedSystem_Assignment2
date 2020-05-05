const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const FloorSchema = new Schema({
    FloorNo:Number,
    Rooms: []
});

const Floor = mongoose.model('Floors',FloorSchema);
module.exports = Floor;