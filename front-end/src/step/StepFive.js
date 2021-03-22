import { FormControl, InputAdornment, InputLabel, OutlinedInput, TextField, Typography } from '@material-ui/core';
import React from 'react';

class StepFive extends React.Component {

    constructor() {
        super();
        this.state = {
            url: ""
        }
    }

    textChange(event) {
        this.setState({ url: event.target.value })
    }

    render() {
        return (
            <div>
                <div style={{ padding: "20px", textAlign: "center" }}>
                    <Typography variant="h5">Aşama 5</Typography>
                </div>
                <TextField
                    fullWidth
                    size="small"
                    variant="outlined"
                    label="url"
                    id="outlined-adornment-amount"
                    value={this.state.url}
                    onChange={this.textChange.bind(this)}
                />
            </div>
        );
    }
} export default StepFive;