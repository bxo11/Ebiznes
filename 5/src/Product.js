import React from 'react';
import { useRouteLoaderData, json} from 'react-router-dom';

const Product = () => {
    const data = useRouteLoaderData('product-detail');
    return (
        <div>
            <h2>Product with id: {data.ID}</h2>
        </div>
    );
};

export async function loader({ request, params }) {
    const id = params.productId;
  
    const response = await fetch('http://localhost:8080/products/' + id);
  
    if (!response.ok) {
      throw json(
        { message: 'Could not fetch details for selected product.' },
        {
          status: 404,
        }
      );
    } else {
      return response;
    }
  }

export default Product;