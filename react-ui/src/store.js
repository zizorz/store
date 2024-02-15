import {ProductList} from "./product-list";
import React, {useEffect, useState} from "react";
import api from "./api";
import {PopularProducts} from "./popular-products";
import {Container} from "@mui/material";

export function Store() {

    const [products, setProducts] = useState([]);
    const [popularProducts, setPopularProducts] = useState([]);
    const [loadingProducts, setLoadingProducts] = useState(true);
    const [loadingPopularProducts, setLoadingPopularProducts] = useState(true);

    useEffect(() => {
        api.getProducts().then(products => {
            setProducts(products);
            console.log(products)
        }).finally(() => {
            setLoadingProducts(false);
        });
    }, []);


    useEffect(() => {
        refreshPopularProducts();
    }, []);

    const refreshPopularProducts = () => {
        setLoadingPopularProducts(true);
        api.getPopularProducts().then(response => {
            setPopularProducts(response.popularProducts);
            console.log(response.popularProducts)
        }).finally(() => {
            setLoadingPopularProducts(false);
        });
    }

    const buyProduct = (productId) => {
        setLoadingProducts(true);
        api.buyProduct(productId).then(() => {
            console.log("Product bought");
            return refreshPopularProducts();
        }).finally(() => {
            setLoadingProducts(false);
        });
    }

    return(
        <Container>
            <ProductList products={products} buyProduct={buyProduct} loading={loadingProducts}></ProductList>
            <PopularProducts products={popularProducts} loading={loadingPopularProducts}></PopularProducts>
        </Container>
    )
}