import CardContent from "@mui/material/CardContent";
import Card from "@mui/material/Card";


export function PopularProductCard(props) {

    return (
        <Card sx={{ minWidth: 200, maxWidth: 250, marginBottom: 1 }}>
            <CardContent>
                <h2>{props.product.name}</h2>
                <p>Bought {props.product.purchasedCount} times</p>
            </CardContent>
        </Card>
    )

}