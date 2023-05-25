import React, { useState, useEffect } from 'react';
import { Link, Form } from 'react-router-dom';
import classes from './ProductList.module.css';
import { getAuthToken } from './auth';

const Products = () => {
    const [products, setProducts] = useState([]);
    const [cartProducts, setCartProducts] = useState([]);
    const token = getAuthToken();

    useEffect(() => {
        fetch('http://localhost:8080/products', {
          method: 'GET',
          headers: {
            'Authorization': 'Bearer ' + token
          }})
        .then(response => response.json())
        .then(data => setProducts(data))
        .catch(error => console.error('Error:', error));
    }, [token]);

    const addToCart = (product) => {
    setCartProducts([...cartProducts, product]);
    }
    
    return (
        <div className={classes.products}>
          <h1>All Products</h1>
          {token && (
              <Form action="/logout" method="post">
                <button>Logout</button>
              </Form>
          )}
          <Link to='/cart' state={ cartProducts } >Cart</Link>
          <ul className={classes.list}>
            {products.map((product) => (
              <li key={product.ID} className={classes.item}>
                <Link to={`/${product.ID}`}>
                  <div className={classes.content}>
                    <h2>{product.Name}</h2>
                    <h3>{product.Price} $</h3>
                    <h3>{product.Category.Name}</h3>
                  </div>
                </Link>
                <button onClick={()=>addToCart(product)}>Add to cart</button>
              </li>
            ))}
          </ul>
        </div>
      );
    }

   
export default Products;
