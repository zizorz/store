import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';

import {Container} from "@mui/material";
import {ProductList} from "./product-list";
import {Store} from "./store";


function Main() {

    return (
        <Container align="center">
            <h1>Store Application</h1>
            <Store></Store>
        </Container>
    )

}

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(<Main/>);