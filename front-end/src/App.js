import { AppBar, Button, Toolbar } from "@material-ui/core";
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link
} from "react-router-dom";
import StepOne from "./step/StepOne";
import StepTwo from "./step/StepTwo";
import StepThree from "./step/StepThree";
import StepFour from "./step/StepFour";
import StepFive from "./step/StepFive";

function App() {
  return (
    <div>
      <AppBar position="static" style={{margin: "0px", padding: "0px"}}>
        <Toolbar justifyContent="center" style={{justifyContent: "space-between", paddingLeft: "30x",paddingRight: "30px"}}>
          <Button color="inherit" href="/stepOne">Aşama 1</Button>
          <Button color="inherit" href="/stepTwo">Aşama 2</Button>
          <Button color="inherit" href="/stepThree">Aşama 3</Button>
          <Button color="inherit" href="/stepFour">Aşama 4</Button>
          <Button color="inherit" href="/stepFive">Aşama 5</Button>
        </Toolbar>
      </AppBar>
      <div style={{padding: "10px"}}>
      <Router>
          <Route exact path="/">
            <StepOne />
          </Route>
          <Route path="/stepTwo">
            <StepTwo />
          </Route>
          <Route path="/stepThree">
            <StepThree />
          </Route>
          <Route path="/stepFour">
            <StepFour />
          </Route>
          <Route path="/stepFive">
            <StepFive />
          </Route>
          <Route path="/stepOne">
            <StepOne />
          </Route>
      </Router>
      </div>
    
    </div>
  );
}export default App;
