import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import Button from "@mui/material/Button";
import React from "react";

export function ProductCard(props) {
    return (
        <Card sx={{ minWidth: 200, maxWidth: 250, marginBottom: 1 }}>
            <CardContent>
                <h2>{props.product.name}</h2>
                <Button variant="contained" color="primary" onClick={() => props.buyProduct(props.product.id)}>Buy</Button>
            </CardContent>
        </Card>
    )
}