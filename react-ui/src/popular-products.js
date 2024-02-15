import {CircularProgress, Container, List, ListItem} from "@mui/material";
import React from "react";
import {PopularProductCard} from "./popular-product-card";

export function PopularProducts(props) {

    const popularProducts = props.products.map(product => {
        return (
            <PopularProductCard key={product.id} product={product}></PopularProductCard>
        )
    });

    if (props.loading) {
        return (
            <Container>
                <h2>Our Best Selling Products</h2>
                <CircularProgress></CircularProgress>
            </Container>
        )
    }

    return (
        <Container>
            <h2>Our Best Selling Products</h2>
            <List>
                {popularProducts}
            </List>
        </Container>
    )

}