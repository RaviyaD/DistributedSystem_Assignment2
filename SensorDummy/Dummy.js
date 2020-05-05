const axios = require('axios');

//Repeating after 15 seconds and update 2 rooms' smoke level and CO2 level
setInterval( function () {
    for (i=0;i<2;i++) {
        axios.get(`http://localhost:5000/all`).then(async (response) => {
            const Floors = response.data;

            //choosing random a Floor and a room
            let FNo = Math.floor(Math.random() * Floors.length);
            let RNo = Math.floor(Math.random() * Floors[FNo].Rooms.length);

            //choosing random numbers between 1-10
            let col2L = Math.floor(Math.random() * 10) + 1;
            let smL = Math.floor(Math.random() * 10) + 1;

            console.log(`Changing Smoke Level & CO2 Level at Floor no: ${Floors[FNo].FloorNo} - Room no: ${Floors[FNo].Rooms[RNo].RoomNo} - Smoke Level: ${smL} - CO2 Level: ${col2L}`)

            try {
                const res = await axios.post('http://localhost:5000/update', {
                    "FloorNo": Floors[FNo].FloorNo,
                    "RoomNo": Floors[FNo].Rooms[RNo].RoomNo,
                    "co2L": col2L,
                    "smL": smL
                });
            } catch (err) {
                console.error(err);
            }

        }).catch((err) => {
            console.error(err);
        });
    }
    //set time as ms
}, 15000);

