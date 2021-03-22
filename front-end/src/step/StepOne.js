import { Backdrop, Button, CircularProgress, Divider, FormControl, Grid, InputAdornment, InputLabel, OutlinedInput, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TextField, Typography } from '@material-ui/core';
import React from 'react';
import axios from "axios";

class StepOne extends React.Component {

    constructor() {
        super();
        this.state = {
            url: "",
            open: false,
            data: []
        }
    }

    textChange(event) {
        this.setState({ url: event.target.value })
    }

    getFrequency() {
        this.setState({ open: true })
        var config = {
            method: 'post',
            url: 'http://www.localhost:8080/stepOne?url=' + this.state.url,
            headers: {
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*'
            },
        };
        axios(config)
            .then(response => {
                this.setState({ data: response.data })
                console.log(response.data);
                this.setState({ open: false })
            })
            .catch(error => {
                console.log(error);   
                this.setState({ open: false })
            });
    }

    render() {
        return (
            <div>
                <Backdrop open={this.state.open} style={{ zIndex: 100 }}>
                    <CircularProgress color="inherit" />
                </Backdrop>
                <div style={{ padding: "20px", textAlign: "center" }}>
                    <Typography variant="h5">Aşama 1 - Frekans</Typography>
                </div>
                <Divider></Divider>
                <br></br>
                <Grid container spacing={1}
                    direction="row"
                    justify="space-between"
                    alignItems="center">
                    <Grid item xs={12} sm={11}>
                        <TextField
                            fullWidth
                            size="small"
                            variant="outlined"
                            label="url"
                            id="outlined-adornment-amount"
                            value={this.state.url}
                            onChange={this.textChange.bind(this)}
                        />
                    </Grid>
                    <Grid item xs={12} sm={1}>
                        <Button size="large" fullWidth variant="outlined" onClick={this.getFrequency.bind(this)}>ARA</Button>
                    </Grid>
                </Grid>
                <br></br>
                <Divider></Divider>
                <br></br>
                <div>
                    <TableContainer component={Paper}>
                        <Table aria-label="simple table">
                            <TableHead>
                                <TableRow>
                                    <TableCell align="left">Index</TableCell>
                                    <TableCell align="center">Kelime</TableCell>
                                    <TableCell align="center">Adet</TableCell>
                                    <TableCell align="right">Frekans</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {this.state.data.map((row, index) => (
                                    <TableRow key={index}>
                                        <TableCell align="left" >
                                            {index + 1}
                                        </TableCell>
                                        <TableCell align="center">{row.name}</TableCell>
                                        <TableCell align="center">{row.count}</TableCell>
                                        <TableCell align="right">{row.frequency}</TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </div>
            </div>
        );
    }
} export default StepOne;