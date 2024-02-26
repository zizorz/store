
const baseUrl = process.env.REACT_APP_BASE_URL || "http://localhost:8080";

class Api {


    getProducts() {
        return fetch(baseUrl + '/products')
            .then(response => response.json());
    }

    buyProduct(productId) {
        return fetch(baseUrl + '/purchases', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({productId: productId, creditCardNumber: '1234-1234-1234-1234'}),
        });
    }

    getPopularProducts() {
        return fetch(baseUrl + '/analytics/popular-products')
            .then(response => response.json());
    }
}

export default new Api();