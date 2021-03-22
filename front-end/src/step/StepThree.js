import { Backdrop, Button, CircularProgress, Divider, FormControl, Grid, InputAdornment, InputLabel, OutlinedInput, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TextField, Typography } from '@material-ui/core';
import React from 'react';
import axios from "axios";

class StepThree extends React.Component {

    constructor() {
        super();
        this.state = {
            url1: "",
            url2: "",
            open: false,
            data: {
                urlOne: "",
                urlTwo: "",
                score: 0.0,
                keywordsUrlOne: [],
                frequenciesUrlOne: [],
                keywordsUrlTwo: [],
                frequenciesUrlTwo: []
            }
        }
    }

    textChange1(event) {
        this.setState({ url1: event.target.value })
    }

    textChange2(event) {
        this.setState({ url2: event.target.value })
    }

    getSimilarity() {
        this.setState({ open: true })
        var config = {
            method: 'post',
            url: 'http://www.localhost:8080/stepThree?url1=' + this.state.url1 + '&url2=' + this.state.url2 + '',
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
                    <Typography variant="h5">Aşama 3 - Benzerlik</Typography>
                </div>
                <Divider></Divider>
                <br></br>
                <Grid container spacing={1}
                    direction="row"
                    justify="space-between"
                    alignItems="center">
                    <Grid item xs={12} sm={12}>
                        <TextField
                            fullWidth
                            size="small"
                            variant="outlined"
                            label="url1"
                            id="outlined-adornment-amount"
                            value={this.state.url1}
                            onChange={this.textChange1.bind(this)}
                        />
                    </Grid>
                    <Grid item xs={12} sm={11}>
                        <TextField
                            fullWidth
                            size="small"
                            variant="outlined"
                            label="url2"
                            id="outlined-adornment-amount"
                            value={this.state.url2}
                            onChange={this.textChange2.bind(this)}
                        />
                    </Grid>
                    <Grid item xs={12} sm={1}>
                        <Button size="large" fullWidth variant="outlined" onClick={this.getSimilarity.bind(this)}>Benzerlik</Button>
                    </Grid>
                </Grid>
                <br></br>
                <Divider></Divider>
                <div>
                    <Grid container spacing={1}
                        direction="row"
                        justify="space-between"
                        alignItems="flex-start">
                        <Grid item xs={12} sm={12}>
                            <div style={{ padding: "5px", textAlign: "center" }}>
                                <Typography variant="h6">Benzerlik: {this.state.data.score}</Typography>
                            </div>
                            <Divider></Divider>
                        </Grid>
                        <Grid item xs={12} sm={6}>
                            <Grid item xs={12} sm={12}>
                                <div style={{ padding: "5px", textAlign: "center" }}>
                                    <Typography variant="h6">{this.state.url1}</Typography>
                                </div>
                                <Divider></Divider>
                                <br></br>
                            </Grid>
                            <Grid item xs={12} sm={12}>
                                <TableContainer component={Paper}>
                                    <Table aria-label="simple table">
                                        <TableHead>
                                            <TableRow>
                                                <TableCell align="left">Index</TableCell>
                                                <TableCell align="right">Anahtar Kelime</TableCell>
                                            </TableRow>
                                        </TableHead>
                                        <TableBody>
                                            {this.state.data.keywordsUrlOne.map((row, index) => (
                                                <TableRow key={index}>
                                                    <TableCell align="left" >
                                                        {index + 1}
                                                    </TableCell>
                                                    <TableCell align="right">{row.name}</TableCell>
                                                </TableRow>
                                            ))}
                                        </TableBody>
                                    </Table>
                                </TableContainer>
                            </Grid>
                            <br></br>
                            <Grid item xs={12} sm={12}>
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
                                            {this.state.data.frequenciesUrlOne.map((row, index) => (
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
                            </Grid>
                        </Grid>
                        <Grid item xs={12} sm={6}>
                            <Grid item xs={12} sm={12}>
                                <div style={{ padding: "5px", textAlign: "center" }}>
                                    <Typography variant="h6">{this.state.url2}</Typography>
                                </div>
                                <Divider></Divider>
                                <br></br>
                            </Grid>
                            <Grid item xs={12} sm={12}>
                                <TableContainer component={Paper}>
                                    <Table aria-label="simple table">
                                        <TableHead>
                                            <TableRow>
                                                <TableCell align="left">Index</TableCell>
                                                <TableCell align="right">Anahtar Kelime</TableCell>
                                            </TableRow>
                                        </TableHead>
                                        <TableBody>
                                            {this.state.data.keywordsUrlTwo.map((row, index) => (
                                                <TableRow key={index}>
                                                    <TableCell align="left" >
                                                        {index + 1}
                                                    </TableCell>
                                                    <TableCell align="right">{row.name}</TableCell>
                                                </TableRow>
                                            ))}
                                        </TableBody>
                                    </Table>
                                </TableContainer>
                            </Grid>
                            <br></br>
                            <Grid item xs={12} sm={12}>
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
                                            {this.state.data.frequenciesUrlTwo.map((row, index) => (
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
                            </Grid>
                        </Grid>
                    </Grid>

                </div>
            </div>
        );
    }
} export default StepThree;