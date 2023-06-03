import { useParams} from 'react-router-dom';
import { getAuthToken } from './auth';
import React, { useState, useEffect } from 'react';

const Product = () => {
    const [products, setProducts] = useState([]);
    const token = getAuthToken();
    const { productId } = useParams()

    useEffect(() => {
      fetch(`${process.env.REACT_APP_API_BASE_URL}/products/` +productId, {
        method: 'GET',
        headers: {
          'Authorization': 'Bearer ' + token
        }})
      .then(response => response.json())
      .then(data => setProducts(data))
      .catch(error => console.error('Error:', error));
    }, [token, productId]);

    return (
        <div>
            <h2>Product with id: {products.ID}</h2>
        </div>
    );
};


export default Product;