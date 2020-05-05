import React, {Component} from 'react';
import axios from 'axios';
import './App.css';
import '../node_modules/bootstrap/dist/css/bootstrap.min.css';
import Block from "./Components/block";


class App extends Component{
  constructor(props) {
    super(props);
    this.state={
      FloorNo:0,
      NoofRooms:0,
      NoofRoomsList:[],
      RoomsCount:0,
      blocks:[{Rooms:[{Active:false},{Active: false}, {Active: false}, {Active: false}, {Active: false}, {Active: false}],},
              {Rooms:[{Active:false},{Active: false}, {Active: false}, {Active: false}, {Active: false}, {Active: false}],},
              {Rooms:[{Active:false},{Active: false}, {Active: false}, {Active: false}, {Active: false}, {Active: false}],},
              {Rooms:[{Active:false},{Active: false}, {Active: false}, {Active: false}, {Active: false}, {Active: false}],},
              {Rooms:[{Active:false},{Active: false}, {Active: false}, {Active: false}, {Active: false}, {Active: false}],},
      ]}
  }
  getDetails() {
      //Get all the details from the REST API
    axios.get("http://localhost:5000/all").then(res => {
      this.setState({
        blocks: res.data
      });
      this.getFloorCount();
      this.getNoofRooms();

    });

  }
  getFloorCount(){
      //Getting the floor count from the REST API
    axios.get(`http://localhost:5000/getFloorCount`).then((response) => {
      this.setState({
        FloorNo:response.data.count + 1
      });
    });
  }

  getNoofRooms(){
      //Getting the maximum number of rooms compared to rooms in each floor
      axios.get(`http://localhost:5000/MaxRoomCount/`).then((response) => {
          this.setState({
            NoofRooms:response.data.maximumRoom,
          }, ()=>{
            for(var i=0; i<response.data.maximumRoom; i++){
              this.state.NoofRoomsList.push(i);
            }
          });
          console.log(this.state.NoofRooms);
      });
    }

  componentDidMount() {
      //Getting details when the component is mounted
      //Calling the getDetails method after 15s
    this.getDetails();
    setInterval(()=>{
      this.getDetails()
    }, 15000)
  }


  render() {

    return (
        <div>
          <table className="table table-bordered">
            <thead>
            <tr className="table-dark">
              <th>Number</th>
              {this.state.blocks.map((block,i)=>{
                return(
                    <th>
                      Room {i+1}
                    </th>
                )
              })}
              <th>Room 8</th>
            </tr>
            </thead>
            <tbody>
            {/*Sending data in a floor to the Block.js */}
            {this.state.blocks.map((block,i)=>{
              return(
                  <tr>
                    <td className="table-dark" >Floor {i+1}</td>
                <Block o={this.state.blocks[i]} header={i} />
              </tr>
              )
            })}
            </tbody>
          </table>
          <div className="text-dark">
            Details:
            <p>If the sensor is active : It will be displayed in Green Color</p>
            <p>If the sensor is not active : It will be displayed in Yellow Color</p>
              <p>If there are no more rooms in the floor, rest will be displayed in White Color</p>
            <p>If the CO2(Carbondioxide) level is higher than or equal to level 5 : It will be displayed in Red Color</p>
            <p>If the Smoke level is higher than or equal to level 5 : It will be displayed in Red Color</p>
          </div>
        </div>
    );
  }


}

export default App;
