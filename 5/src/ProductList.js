import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import classes from './ProductList.module.css';
const Products = () => {
    const [products, setProducts] = useState([]);
    const [cartProducts, setCartProducts] = useState([]);

    useEffect(() => {
        fetch('http://localhost:8080/products')
        .then(response => response.json())
        .then(data => setProducts(data))
        .catch(error => console.error('Error:', error));
    }, []);

    const addToCart = (product) => {
    setCartProducts([...cartProducts, product]);
    }
    
    return (
        <div className={classes.products}>
          <h1>All Products</h1>
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
