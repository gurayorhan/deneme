import { Accordion, AccordionDetails, AccordionSummary, Backdrop, Button, CircularProgress, Divider, FormControl, Grid, InputAdornment, InputLabel, OutlinedInput, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TextField, Typography } from '@material-ui/core';
import React from 'react';
import axios from "axios";
import ExpandMoreIcon from '@material-ui/icons/ExpandMore'

class StepFour extends React.Component {

    constructor() {
        super();
        this.state = {
            url: "",
            urltext: "",
            open: false,
            data: {
                url: "",
                keywords: [],
                deepSimilarities: []
            }
        }
    }

    textChange(event) {
        this.setState({ url: event.target.value })
    }

    textListChange(event) {
        this.setState({ urltext: event.target.value })
    }

    getDeep() {
        this.setState({ open: true })
        var urlList = this.state.urltext.split(" ");
        urlList.forEach(element => console.log(element));

        var send = {
            url: this.state.url,
            urlList: urlList
        }

        var config = {
            method: 'post',
            url: 'http://www.localhost:8080/stepFour',
            headers: {
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*'
            },
            data: JSON.stringify(send)
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
                <Backdrop open={this.state.open} style={{zIndex: 100}}>
                    <CircularProgress color="inherit" />
                </Backdrop>
                <div style={{ padding: "20px", textAlign: "center" }}>
                    <Typography variant="h5">Aşama 4 - Derinlik İle Benzerlik</Typography>
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
                        <Button size="large" fullWidth variant="outlined" onClick={this.getDeep.bind(this)}>ARA</Button>
                    </Grid>
                    <Grid item xs={12} sm={12}>
                        <TextField
                            fullWidth
                            id="outlined-multiline-static"
                            label="url Listesi"
                            multiline
                            rows={4}
                            variant="outlined"
                            value={this.state.urltext}
                            onChange={this.textListChange.bind(this)}
                        />
                    </Grid>
                </Grid>
                <br></br>
                <Divider></Divider>
                <div style={{ padding: "5px", textAlign: "center" }}>
                    <Typography variant="h6">Benzerlik Listesi</Typography>
                </div>
                <Divider></Divider>
                <br></br>
                <div>
                    <TableContainer component={Paper}>
                        <Table aria-label="simple table">
                            <TableHead>
                                <TableRow>
                                    <TableCell align="left">Index</TableCell>
                                    <TableCell align="center">Skor</TableCell>
                                    <TableCell align="right">Url</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {this.state.data.deepSimilarities.map((row, index) => (
                                    <TableRow key={index}>
                                        <TableCell align="left" >
                                            {index + 1}
                                        </TableCell>
                                        <TableCell align="center">{row.score}</TableCell>
                                        <TableCell align="right">{row.levelOne.uri}</TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </div>
                <br></br>
                <Divider></Divider>
                <div style={{ padding: "5px", textAlign: "center" }}>
                    <Typography variant="h6">Ağaç Yapısı</Typography>
                </div>
                <Divider></Divider>
                <br></br>
                <div style={{ width: "100%" }}>
                    {this.state.data.deepSimilarities.map((row1, index1) => (
                        <div key={index1}>
                            <Accordion>
                                <AccordionSummary
                                    expandIcon={<ExpandMoreIcon />}
                                    aria-controls="panel1a-content"
                                    id="panel1a-header"
                                >
                                    1. Derinlik {row1.levelOne.uri}
                                </AccordionSummary>
                                <AccordionDetails>
                                    <div style={{ width: "100%" }}>
                                        <Accordion>
                                            <AccordionSummary
                                                expandIcon={<ExpandMoreIcon />}
                                                aria-controls="panel1a-content"
                                                id="panel1a-header"
                                            >
                                                Anahtar Kelimeleri - {row1.levelOne.uri}
                                            </AccordionSummary>
                                            <AccordionDetails>
                                                <TableContainer component={Paper}>
                                                    <Table aria-label="simple table">
                                                        <TableHead>
                                                            <TableRow>
                                                                <TableCell align="left">Index</TableCell>
                                                                <TableCell align="right">Anahtar Kelime</TableCell>
                                                            </TableRow>
                                                        </TableHead>
                                                        <TableBody>
                                                            {row1.levelOne.keywords.map((row, index) => (
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
                                            </AccordionDetails>
                                        </Accordion>
                                        {row1.levelOne.levelTwoList.map((row2, index2) => (
                                            <Accordion key={index2}>
                                                <AccordionSummary
                                                    expandIcon={<ExpandMoreIcon />}
                                                    aria-controls="panel1a-content"
                                                    id="panel1a-header"
                                                >
                                                    2. Derinlik {row2.uri}
                                                </AccordionSummary>
                                                <AccordionDetails>
                                                    <div style={{ width: "100%" }}>
                                                        <Accordion>
                                                            <AccordionSummary
                                                                expandIcon={<ExpandMoreIcon />}
                                                                aria-controls="panel1a-content"
                                                                id="panel1a-header"
                                                            >
                                                                Anahtar Kelimeleri - {row2.uri}
                                                            </AccordionSummary>
                                                            <AccordionDetails>
                                                                <TableContainer component={Paper}>
                                                                    <Table aria-label="simple table">
                                                                        <TableHead>
                                                                            <TableRow>
                                                                                <TableCell align="left">Index</TableCell>
                                                                                <TableCell align="right">Anahtar Kelime</TableCell>
                                                                            </TableRow>
                                                                        </TableHead>
                                                                        <TableBody>
                                                                            {row2.keywords.map((row, index) => (
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
                                                            </AccordionDetails>
                                                        </Accordion>
                                                        {row2.levelThreeList.map((row3, index3) => (
                                                            <Accordion key={index3}>
                                                                <AccordionSummary
                                                                    expandIcon={<ExpandMoreIcon />}
                                                                    aria-controls="panel1a-content"
                                                                    id="panel1a-header"
                                                                >
                                                                    3. Derinlik {row3.uri}
                                                                </AccordionSummary>
                                                                <AccordionDetails>
                                                                    <div style={{ width: "100%" }}>
                                                                        <Accordion>
                                                                            <AccordionSummary
                                                                                expandIcon={<ExpandMoreIcon />}
                                                                                aria-controls="panel1a-content"
                                                                                id="panel1a-header"
                                                                            >
                                                                                Anahtar Kelimeleri - {row3.uri}
                                                                            </AccordionSummary>
                                                                            <AccordionDetails>
                                                                                <TableContainer component={Paper}>
                                                                                    <Table aria-label="simple table">
                                                                                        <TableHead>
                                                                                            <TableRow>
                                                                                                <TableCell align="left">Index</TableCell>
                                                                                                <TableCell align="right">Anahtar Kelime</TableCell>
                                                                                            </TableRow>
                                                                                        </TableHead>
                                                                                        <TableBody>
                                                                                            {row3.keywords.map((row, index) => (
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
                                                                            </AccordionDetails>
                                                                        </Accordion>
                                                                    </div>
                                                                </AccordionDetails>
                                                            </Accordion>
                                                        ))}
                                                    </div>
                                                </AccordionDetails>
                                            </Accordion>
                                        ))}
                                    </div>
                                </AccordionDetails>
                            </Accordion>
                            <br></br>

                        </div>
                    ))}
                </div>
            </div >
        );
    }
} export default StepFour;