const express = require('express');
const cors = require('cors');
const  mongoose = require('mongoose');
let Floor = require('./model/Floor.model');

const app = express();
const port = process.env.PORT || 5000
app.use(express.json());
app.use(cors());

mongoose.connect('mongodb+srv://raveen:1234@cluster0-j27bg.mongodb.net/test?retryWrites=true&w=majority',
    {useNewUrlParser:true, useCreateIndex:true,useUnifiedTopology: true});
const connection = mongoose.connection;

//Starting REST ful API
connection.once('open',()=>{
    console.log("REST API started!");
})

const FloorRouter = require('./routes/Floor');
app.use('/',FloorRouter);



app.listen(port,()=>{
    console.log(`Server ${port}`);
})