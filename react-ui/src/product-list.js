import React from "react";
import {CircularProgress, Container} from "@mui/material";
import {ProductCard} from "./product-card";

export function ProductList(props){

    const productCards = props.products.map(product => {
        return (
            <ProductCard key={product.id} product={product} buyProduct={props.buyProduct}></ProductCard>
        )
    })

    if (props.loading) {
        return (
            <Container>
                <h2>Products</h2>
                <CircularProgress></CircularProgress>
            </Container>
        )
    }

    return(
        <Container>
            <h2>Products</h2>
            <ul>
                {productCards}
            </ul>
        </Container>
    )
}