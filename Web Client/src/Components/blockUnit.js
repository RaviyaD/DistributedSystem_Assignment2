import React, {Component} from "react";

export default class BlockUnit extends Component{
    constructor(props) {
        super(props);
        this.state={
            object:this.props.obj,
            co2danger:false,
            smokedanger:false,
            length:0,
            blocks:[]
        }
    }

    componentWillReceiveProps(nextProps, nextContext) {
        //Getting the properties from Block
        this.setState({
            co2danger:false,
            smokedanger:false,
            blocks:nextProps.obj,
            /*length:nextProps.o.length*/
        });
        if(nextProps.obj.CO2Level>=5){
            this.setState({
                co2danger:true
            })
        }
        if(nextProps.obj.SmokeLevel>=5){
            this.setState({
                smokedanger:true
            })
        }
    }
    render() {
        const {  header, h , obj, co2, smoke}=this.props;
        return(
                <td className={this.state.blocks.Active? 'table-success':'table-warning'} >
                    <div style={{height: "150px", width:"150px"}}>
                        {this.state.blocks.Active ?
                            (<div>
                                <p>Floor = {this.props.h+1}  </p>
                                <p>Room = {this.props.obj.RoomNo}  </p>
                                <p className={this.state.co2danger ? 'table-danger' : 'none'}>CO2 Level = {this.props.obj.CO2Level}  </p>
                                <p className={this.state.smokedanger ? 'table-danger' : 'none'}>Smoke Level = {this.props.obj.SmokeLevel}</p>
                            </div>) : (<div>
                                <p>Not Active</p>
                            </div>)
                        }
                    </div>
                </td>
        );
    }
}